package com.ards.ui.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ards.databinding.FragmentUploadBinding
import com.ards.ui.scan.ScanViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class UploadFrament: Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var videoUri: String
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        binding.btnPredict.setOnClickListener{

        }
        return root
    }
}