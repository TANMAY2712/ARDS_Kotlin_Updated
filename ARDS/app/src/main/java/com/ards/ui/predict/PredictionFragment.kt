package com.ards.ui.predict

import Fault
import FaultAdapter
import FaultData
import FaultImageAdapter
import FaultResponses
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ards.databinding.FragmentProcessedBinding
import com.ards.ui.predict.adapter.PredictFaultAdapter
import com.ards.ui.predict.adapter.PredictFaultImageAdapter
import com.ards.ui.predict.model.PredictFaultResponces
import com.ards.utils.ArdsConstant
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.json.JSONObject

class PredictionFragment : Fragment() {

    private var _binding: FragmentProcessedBinding? = null
    private val binding get() = _binding!!


    private lateinit var url: String
    private val faultList = mutableListOf<Fault>()
    private val faulImagetList = mutableListOf<Fault>()
    private lateinit var faultAdapter: FaultAdapter
    private lateinit var faultImageAdapter: FaultImageAdapter
    private var player: ExoPlayer? = null
    private lateinit var requestQueue: RequestQueue
     var notification_id:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessedBinding.inflate(inflater, container, false)
        val view = binding.root
        requestQueue = Volley.newRequestQueue(requireContext())
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        notification_id=arguments?.getInt("notification_id_key")!!
        // Set up RecyclerView
        binding.rvFaultList.layoutManager = LinearLayoutManager(requireContext())

        faultAdapter = FaultAdapter(
            faultList,
            onTimestampClick = { timestamp, position ->
                seekToTimestamp(timestamp) // Function to seek video to timestamp
                faultImageAdapter.highlightImage(position) // Highlight image


                // Scroll RecyclerView to the selected fault
                binding.rvFaultFrames.smoothScrollToPosition(position)
            },
        )

        binding.rvFaultList.adapter = faultAdapter

        binding.rvFaultFrames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        faultImageAdapter = FaultImageAdapter(
            faultList,
            onImageClick = { position ->
                // Highlight selected fault item in the list
                val previousSelected = faultAdapter.selectedPosition
                faultAdapter.selectedPosition = position
                faultAdapter.notifyItemChanged(previousSelected) // Unselect previous
                faultAdapter.notifyItemChanged(position) // Highlight new selection

                // Scroll fault list to corresponding item
                binding.rvFaultList.smoothScrollToPosition(position)
            },
            onTimestampClick = { timestamp ->
                seekToTimestamp(timestamp) // Seek video to timestamp when image clicked
            }
        )

        binding.rvFaultFrames.adapter = faultImageAdapter




        fetchFaultData()
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Set up RecyclerView


        //fetchFaultData()
        return view
    }
    private fun fetchFaultData() {
        val jsonRequestBody = JSONObject().apply {
            put("APIKey", ArdsConstant.ARDS_APIKEY)
            put("TrainName", "")
            put("PartName", "")
            put("PageNo", 0)
            put("NotificationId", notification_id)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, ArdsConstant.ARDS_BASE_URL, jsonRequestBody,
            Response.Listener { response ->
                Log.d(TAG, "Response: $response")
                try {
                    binding.progress.visibility = View.GONE

                    val faultResponse = FaultResponses(response)
                    faultList.clear()
                    faultList.addAll(faultResponse.data.faults)
                    faultAdapter.notifyDataSetChanged()

                    val faultImageResponse = FaultResponses(response)
                    faulImagetList.clear()
                    faulImagetList.addAll(faultImageResponse.data.faults)
                    faultImageAdapter.notifyDataSetChanged()

                    val mp4Url = extractMp4Url(faultResponse.data.outputUrl!!)
                    // Load Video (if available)
                    val videoUrl = mp4Url
                    if (!videoUrl.isNullOrEmpty()) {
                        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                        player?.apply {
                            setMediaItem(mediaItem)
                            prepare()
                            play()
                            setPlaybackSpeed(0.5f)
                        }
                    } else {
                        Log.w(TAG, "No video URL available")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Parsing error: ${e.message}")
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error: ${error.message}")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
    fun extractMp4Url(url: String): String {
        return url.substringBefore("?") // Removes everything after "?"
    }
    // Function to seek video when timestamp is clicked
    private fun seekToTimestamp(timestamp: String) {
        val timeParts = timestamp.split(":")
        if (timeParts.size == 3) {
            val hours = timeParts[0].toInt()
            val minutes = timeParts[1].toInt()
            val seconds = timeParts[2].toInt()
            val timeInMillis = ((hours * 3600) + (minutes * 60) + seconds) * 1000L

            player?.seekTo(timeInMillis) // Seek ExoPlayer to the clicked time
            player?.play() // Resume playback
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        _binding = null
    }

    companion object {
        private const val TAG = "ProcessedFragment"
    }
}