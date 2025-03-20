package com.ards.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.R
import com.ards.databinding.FragmentLibraryBinding
import com.ards.ui.library.adapter.LibraryAdapter
import com.ards.ui.libvideo.adapter.LibVideoAdapter
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Button
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private val libraryViewModel: LibraryViewModel by viewModels()
    private lateinit var libraryAdapter: LibraryAdapter
    private lateinit var libraryDetailsAdapter: LibVideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup RecyclerView
        setupRecyclerView()

        // Observe Loading State
        libraryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.libraryProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Fetch Library Data
        getLibrary("LibraryCategory", 0)

        return root
    }

    private fun setupRecyclerView() {
        binding.rvTrainLibrary.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        libraryAdapter = LibraryAdapter(requireContext(), mutableListOf(), object : LibraryAdapter.Callback {
            override fun onItemClicked(catId: Int) {
                getVideoBycategory(catId.toString())
               /* val bundle = Bundle().apply {
                    putString("category_id_key", catId.toString())
                }
                Navigation.findNavController(binding.rvTrainLibrary)
                    .navigate(R.id.action_libraryFragment_to_videoFragment, bundle)*/
            }
        })
        binding.rvTrainLibrary.adapter = libraryAdapter
    }

    private fun getLibrary(type: String, Id: Int) {
        libraryViewModel.getLibrary(type, Id).observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                response.Data?.let {
                    libraryAdapter.updateData(it) // Update the RecyclerView with new data
                }
            }
            result.onFailure { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getVideoBycategory(catgoryId: String) {
        libraryViewModel.getVideoBycategory(catgoryId)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { videos ->
                    // Setting up RecyclerView
                    binding.rvTrainLibraryHorizontal.layoutManager = GridLayoutManager(requireContext(), 1)

                    libraryDetailsAdapter = LibVideoAdapter(requireContext(), videos.Data, object : LibVideoAdapter.Callback {

                        override fun onItemClickedVideo(videoUrl: String) {
                            val videoId = getYoutubeVideoId(videoUrl)
                            showVideoDialog(requireContext(), videoId)
                        }

                        override fun onLikeClicked(videoUrl: String, position: Int) {
                          //  Toast.makeText(requireContext(), "Liked: $videoUrl", Toast.LENGTH_SHORT).show()
                            // TODO: Implement like functionality (e.g., update database, UI, etc.)
                        }

                        override fun onCommentSubmitted(videoUrl: String, comment: String) {

                        }


                    })

                    binding.rvTrainLibraryHorizontal.adapter = libraryDetailsAdapter

                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun getYoutubeVideoId(url: String): String {
        return url.substringAfter("youtu.be/").substringBefore("?")
    }


    fun showVideoDialog(context: Context, videoUrl: String) {
        // Inflate custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_video_player, null)

        // Initialize YouTube Player View
        val youTubePlayerView = dialogView.findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        // Initialize YouTube Player
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoUrl, 0f) // Play video from start
            }
        })

        // Create AlertDialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Handle Cancel Button Click
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Show Dialog
        dialog.show()
    }

}
