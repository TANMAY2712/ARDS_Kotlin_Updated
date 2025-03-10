package com.ards.ui.processed

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.databinding.FragmentProcessedBinding
import com.ards.ui.processed.adapter.FaultListAdapter
import com.ards.ui.processed.model.FaultItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class ProcessedFragment : Fragment() {

    private var _binding: FragmentProcessedBinding? = null
    private val binding get() = _binding!!
    private var player: ExoPlayer? = null
    private lateinit var overlayText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        overlayText = binding.tvOverlayText // Get the TextView reference

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Load Video
        val videoUrl = "https://ards-data.s3.amazonaws.com/outputs/65c612e57db39ec6eb44017c.mp4"
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        player?.setPlaybackSpeed(0.5f)
        // Fault List with timestamps (Milliseconds)
        val faultList = listOf(
            FaultItem("Brakes", "0:32", 32_000),
            FaultItem("Suspensions", "0:45", 45_000),
            FaultItem("Springs", "1:32", 92_000),
            FaultItem("Axle Box", "1:52", 112_000)
        )

        // RecyclerView Setup
        binding.rvFaultList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FaultListAdapter(faultList, requireContext()) { seekTime ->
            player?.seekTo(seekTime) // Jump to timestamp
            player?.play()
        }
        binding.rvFaultList.adapter = adapter

        // Monitor playback position
        monitorPlaybackPosition()

        return root
    }

    private fun monitorPlaybackPosition() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                player?.let {
                    val currentTime = it.currentPosition

                    when {
                        currentTime in 32_000..40_000 -> {
                            overlayText.text = "Fault Detected: Brakes"
                            overlayText.visibility = View.VISIBLE
                        }
                        currentTime in 45_000..55_000 -> {
                            overlayText.text = "Fault Detected: Suspensions"
                            overlayText.visibility = View.VISIBLE
                        }
                        currentTime in 92_000..102_000 -> {
                            overlayText.text = "Fault Detected: Springs"
                            overlayText.visibility = View.VISIBLE
                        }
                        currentTime in 112_000..122_000 -> {
                            overlayText.text = "Fault Detected: Axle Box"
                            overlayText.visibility = View.VISIBLE
                        }
                        else -> {
                            overlayText.visibility = View.GONE
                        }
                    }

                    handler.postDelayed(this, 500) // Check every 500ms
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
    }
}
