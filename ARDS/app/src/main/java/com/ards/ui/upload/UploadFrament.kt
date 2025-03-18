package com.ards.ui.upload

import android.content.Context
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
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.R
import com.ards.databinding.FragmentUploadBinding
import com.ards.remote.remote.ApiFactory
import com.ards.ui.notification.adapter.NotificationAdapter
import com.ards.utils.ArdsConstant
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import okhttp3.ResponseBody
import java.io.File

class UploadFrament : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var videoUri: String
    private val uploadViewModel: UploadViewModel by viewModels()
    private var player: ExoPlayer? = null
    //private val repository = UploadRepository(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       /* val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)*/

        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        uploadViewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.uploadVideoProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        if (arguments != null) {
            videoUri = requireArguments().getString("videoUri_key")!!
        }

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(requireContext()).build()
        binding.uploadplayerView.player = player

        val mediaItem = MediaItem.fromUri(videoUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        player?.setPlaybackSpeed(0.5f)

        binding.btnPredict.setOnClickListener {
            //uploadVideo(videoUri)
            getPreSignedUrl(Uri.parse(videoUri), "12397","NDLS","LEFT", requireContext())
        }
        return root
    }

    private fun getPreSignedUrl(fileUri: Uri, trainNo: String, station: String, recSide: String, ctx: Context) {
        /*uploadViewModel.getPreSignedUrl(fileUri, trainNo, station, recSide, ctx)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { uploads ->
                    // Setting up RecyclerView
                    performModelInference(uploads.presigned_url, trainNo, station, recSide)
                    //uploadFileToS3(uploads.presigned_url, uploads.resource_id, fileUri, trainNo, station, recSide, context)
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }*/

        val repository = UploadRepository(requireContext())

        repository.uploadFileToBlob(
            fileUri = fileUri,
            trainNo = trainNo,
            station = station,
            recSide = recSide,
            context = ctx,
            { response ->
                Log.d("uploadDone", "CaptureScreen: $response")
                val outputUrlvalue = response.getJSONObject("data").getString("output_url")

                val predictedDataList = ArrayList<HashMap<String, String?>>()
                val jsonArry = response.getJSONObject("data").getJSONArray("faults")
                var faultDetails: String = ""
                var stnName: String = ""
                var trNumber: String = ""
                var trName: String = ""
                var partName: String = ""
                var outputURL: String = response.getJSONObject("data").getString("output_url")
                var recordedSide: String = ""
                val listFaults = ArrayList<HashMap<String, Any>>()
                for (i in 0 until jsonArry.length()) {
                    val predictedData = HashMap<String, String?>()
                    val obj = jsonArry.getJSONObject(i)
                    faultDetails = obj.getString("fault_info")
                    recordedSide = obj.getString("recorded_side")
                    stnName = obj.getString("station_name")
                    trNumber = obj.getString("train_number")
                    trName = obj.getString("train_name")
                    partName = obj.getString("part_name")

                    val hmMasters = HashMap<String, Any>()
                    hmMasters[ArdsConstant.ToolkitApiKey.PART_NAME] = obj.getString("part_name")
                    hmMasters[ArdsConstant.ToolkitApiKey.COACH_NUMBER] = obj.getString("coach_number")
                    hmMasters[ArdsConstant.ToolkitApiKey.COACH_POSITION] =
                        obj.getString("coach_position")
                    hmMasters[ArdsConstant.ToolkitApiKey.FAULT_ID] = ""
                    hmMasters[ArdsConstant.ToolkitApiKey.FAULTY_DETAILS] = obj.getString("fault_info")
                    hmMasters[ArdsConstant.ToolkitApiKey.FAULTY_PROBABLITY] =
                        obj.getString("fault_probability")
                    hmMasters[ArdsConstant.ToolkitApiKey.IMAGE_PATH] = obj.getString("image_url")
                    listFaults.add(hmMasters)
                }

                val bundle = Bundle();
                bundle.putString("predicted_output_url_key", outputUrlvalue)
                bundle.putSerializable("predicted_faut_list_key", listFaults)
                Navigation.findNavController(binding.btnPredict)
                    .navigate(R.id.uploadFragment_to_predictFragment,bundle)
            },
            {
                Log.d("uploadNotDone", "No: ")
            }
        )
    }

    private fun performModelInference(presignedUrl:String, trainNo: String, station: String, recSide: String) {
        uploadViewModel.modelInference(presignedUrl,trainNo, station, recSide)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { prediction ->
                    // Setting up RecyclerView
                    if(prediction.statusCode.equals(200)){
                        Log.d("prediction_img", prediction.data.toString())
                    val bundle = Bundle();
                    bundle.putString("predicted_output_url_key", prediction.data.output_url)
                    //bundle.putString("predicted_output_url", prediction.data.faults)
                    bundle.putSerializable("predicted_faut_list_key", ArrayList(prediction.data.faults))
                    Navigation.findNavController(binding.btnPredict)
                        .navigate(R.id.uploadFragment_to_predictFragment,bundle)
                    }
                    else{
                        ArdsConstant.showShortToast(prediction.data.progress, requireContext())
                    }
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}