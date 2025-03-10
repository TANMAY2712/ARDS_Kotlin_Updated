package com.ards.ui.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.databinding.FragmentPlaygroundBinding
import com.ards.ui.playground.adapter.PlaygroundAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlaygroundFragment : Fragment() {

    private var _binding: FragmentPlaygroundBinding? = null
    private val binding get() = _binding!!
    private lateinit var videoAdapter: PlaygroundAdapter
    private val viewModel: PlaygroundViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaygroundBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvTrainPlayground.layoutManager = LinearLayoutManager(requireContext())
        videoAdapter = PlaygroundAdapter(emptyList()) { video ->
            Toast.makeText(requireContext(), "Processing ${video.title}", Toast.LENGTH_SHORT).show()
        }
        binding.rvTrainPlayground.adapter = videoAdapter
    }

    private fun observeViewModel() {
        viewModel.videoList.observe(viewLifecycleOwner, Observer { videos ->
            binding.progress.visibility=View.GONE
            videoAdapter.updateData(videos)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
