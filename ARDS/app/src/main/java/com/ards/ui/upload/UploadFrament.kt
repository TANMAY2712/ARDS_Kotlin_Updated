package com.ards.ui.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ards.MainActivity
import com.ards.R
import com.ards.databinding.FragmentUploadBinding
import com.ards.sharedpreference.PreferenceHelper
import com.ards.utils.ArdsConstant
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var videoUri: String
    private lateinit var stationName: String
    private lateinit var sidePosition: String
    private lateinit var trainName: String
    private lateinit var trainNumber: String
    private val uploadViewModel: UploadViewModel by viewModels()
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)

        uploadViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.uploadVideoProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        videoUri = arguments?.getString("videoUri_key") ?: ""
        stationName = arguments?.getString("station_name_key") ?: ""
        sidePosition = arguments?.getString("rake_side_key") ?: ""
        trainName = arguments?.getString("train_name_key") ?: ""
        trainNumber = arguments?.getString("train_number_key") ?: ""

        // Ensure videoUri is not empty
        if (videoUri.isNotEmpty()) {
            initializePlayer(videoUri)
        } else {
            Toast.makeText(requireContext(), "Invalid video URI", Toast.LENGTH_SHORT).show()
        }

        binding.btnPredict.setOnClickListener {
            showLoadingAnimation()
            getPreSignedUrl(Uri.parse(videoUri), "14779", "New Delhi", "Left", requireContext())
        }
        return binding.root
    }

    private fun initializePlayer(uri: String) {
        player = ExoPlayer.Builder(requireContext()).build().apply {
            binding.uploadplayerView.player = this
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            play()
            setPlaybackSpeed(0.5f)
            volume = 0f
        }
    }

    private fun showLoadingAnimation() {
        binding.gifLoaderContainerUpload.visibility = View.VISIBLE
        Glide.with(this).asGif().load(R.drawable.train_loader).into(binding.gifLoader)
    }

    private fun getPreSignedUrl(fileUri: Uri, trainNo: String, station: String, recSide: String, ctx: Context) {
        val repository = UploadRepository(requireContext())

        repository.uploadFileToBlob(
            fileUri, trainNo, station, recSide, ctx,
            { response ->
                binding.gifLoaderContainerUpload.visibility = View.GONE
                Log.d("UploadSuccess", "Response: $response")

                try {
                    val outputUrl = response.getJSONObject("data").getString("output_url")
                    val jsonArray = response.getJSONObject("data").optJSONArray("faults") ?: JSONArray()
                    val faultsList = parseFaults(jsonArray)

                    sendFaultReport(ctx, faultsList,outputUrl)
                } catch (e: JSONException) {
                    Log.e("JSONError", "Error parsing response: ${e.message}")
                    Toast.makeText(ctx, "Error processing response", Toast.LENGTH_SHORT).show()
                }

            },
            {
                binding.gifLoaderContainerUpload.visibility = View.GONE
                Log.e("UploadError", "File upload failed")
                Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun parseFaults(jsonArray: JSONArray): ArrayList<HashMap<String, String>> {
        val faultsList = ArrayList<HashMap<String, String>>() // Ensure correct type

        for (i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)

                val faultMap = hashMapOf(
                    "part_name" to obj.optString("part_name", "Unknown"),
                    "coach_number" to obj.optString("coach_number", "Unknown"),
                    "coach_position" to obj.optString("coach_position", "Unknown"),
                    "Fault_ID" to "",
                    "faulty_details" to obj.optString("fault_info", "No details"),
                    "faulty_probablity" to obj.optString("fault_probability", "N/A"),
                    "Image_path" to obj.optString("image_url", "")
                )

                faultsList.add(faultMap) // This should now work fine
            } catch (e: JSONException) {
                Log.e("ParseError", "Error parsing JSON fault: ${e.message}")
            }
        }
        return faultsList
    }


    private fun sendFaultReport(context: Context, faultsList: ArrayList<HashMap<String, String>>,
                                outputUrl:String) {
        val url = "https://dev.workerunionsupport.com/api/ToolKit/SendNotification"
        val faultsArray = createFaultsArray(faultsList)

        val requestBody = JSONObject().apply {
            put("APIKey", ArdsConstant.ARDS_APIKEY)
            put("UserId", PreferenceHelper.getInstance(requireContext()).getUserId)
            put("title", trainName+"("+trainNumber+") Fault Report")
            put("body", "Detailed report of detected faults in train components.")
            put("station_name", stationName)
            put("train_number", trainNumber)
            put("train_name", trainName)
            put("coach_side", sidePosition)
            put("output_url", outputUrl)
            put("faults", faultsArray)
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                Log.d("VolleyResponse", "Response: $response")
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
                /*val bundle = Bundle()
                bundle.putInt("notification_id_key", notificationId)
                Navigation.findNavController(binding.recentTrainsRecyclerView)
                    .navigate(R.id.uploadFragment_to_predictFragment, bundle)*/
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.message}")
                Toast.makeText(context, "Error sending fault report", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun createFaultsArray(faultsList: ArrayList<HashMap<String, String>>): JSONArray {
        val faultsArray = JSONArray()
        for (fault in faultsList) {
            try {
                val faultObject = JSONObject()
                for ((key, value) in fault) {
                    faultObject.put(key, value)
                }
                faultsArray.put(faultObject)
            } catch (e: JSONException) {
                Log.e("JSONError", "Error creating fault JSON: ${e.message}")
            }
        }
        return faultsArray
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
        player = null
    }
}
