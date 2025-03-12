import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ards.databinding.FragmentProcessedBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ProcessedsFragment : Fragment() {

    private var _binding: FragmentProcessedBinding? = null
    private val binding get() = _binding!!

    private lateinit var faultAdapter: FaultAdapter
    private lateinit var faultImageAdapter: FaultImageAdapter
    private val faultList = mutableListOf<Fault>()
    private val faulImagetList = mutableListOf<Fault>()
    private lateinit var url: String
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessedBinding.inflate(inflater, container, false)
        val view = binding.root

        url = arguments?.getString("url").orEmpty()

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Set up RecyclerView
        binding.rvFaultList.layoutManager = LinearLayoutManager(requireContext())
        faultAdapter = FaultAdapter(faultList) { timestamp -> seekToTimestamp(timestamp) }
        binding.rvFaultList.adapter = faultAdapter


        binding.rvFaultFrames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        faultImageAdapter = FaultImageAdapter(faultList) { timestamp -> seekToTimestamp(timestamp) }
        binding.rvFaultFrames.adapter = faultImageAdapter


        fetchFaultData()
        return view
    }

    private fun fetchFaultData() {
        val queue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d(TAG, "Response: $response")
                try {
                    binding.progress.visibility=View.GONE
                    val faultResponse = FaultResponses(response)
                    faultList.clear()
                    faultList.addAll(faultResponse.data.faults)
                    faultAdapter.notifyDataSetChanged()

                    val faultImageResponse = FaultResponses(response)
                    faulImagetList.clear()
                    faulImagetList.addAll(faultImageResponse.data.faults)
                    faultImageAdapter.notifyDataSetChanged()

                    // Load Video (if available)
                    val videoUrl = faultResponse.data.outputUrl
                    if (!videoUrl.isNullOrEmpty()) {
                        val mediaItem = MediaItem.fromUri(Uri.parse(faultResponse.data.outputUrl))
                        player?.setMediaItem(mediaItem)
                        player?.prepare()
                        player?.play()
                        player?.setPlaybackSpeed(0.5f)
                    } else {
                        Log.w(TAG, "No video URL available")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Parsing error: ${e.message}")
                }
            },
            { error ->
                Log.e(TAG, "Error: ${error.message}")
            })

        queue.add(jsonObjectRequest)
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
