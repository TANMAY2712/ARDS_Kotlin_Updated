package com.ards.ui.playground

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.ards.R
import com.ards.databinding.FragmentPlaygroundDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlaygroundDetailsFragment : Fragment() {

    private var _binding: FragmentPlaygroundDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaygroundViewModel by viewModels()
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaygroundDetailsBinding.inflate(inflater, container, false)

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val videoUrl =
            arguments?.getString("video_url")!!
        binding.trainName.text = arguments?.getString("train_name")!!
        if (videoUrl.isNotEmpty()) {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            player?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true  // Auto-play
            }
        } else {

        }

        binding.applyArds.setOnClickListener {
            // Show blurred background
            binding.cardView.visibility=View.GONE

            // Show GIF using Glide
            Glide.with(this)
                .asGif()
                .load(R.drawable.train_loader)  // Replace with your actual GIF drawable
                .into(binding.gifLoader)

            binding.applyArds.postDelayed({
                binding.gifLoader.visibility = View.GONE
                binding.blurredBackground.visibility = View.GONE // Hide blur effect

                val bundle = Bundle().apply {
                    putString("url", arguments?.getString("url"))
                }
                Navigation.findNavController(binding.applyArds)
                    .navigate(R.id.processedsFragment, bundle)
            }, 5000)
        }


    }

    private fun createBlurredBackground(): Bitmap {
        val view = requireActivity().window.decorView.rootView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val rs = RenderScript.create(requireContext())
        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(15f)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)

        rs.destroy()
        return bitmap
    }


    override fun onStop() {
        super.onStop()
        player?.release() // Release player properly
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
