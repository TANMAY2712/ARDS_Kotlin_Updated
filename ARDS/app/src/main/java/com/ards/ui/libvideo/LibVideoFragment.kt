package com.ards.ui.libvideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.R
import com.ards.databinding.FragmentLibraryBinding
import com.ards.ui.library.adapter.LibraryAdapter
import com.ards.ui.libvideo.adapter.LibVideoAdapter

class LibVideoFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private lateinit var Cnumber: String
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val libraryViewModel: LibVideoViewModel by viewModels()
    private lateinit var libraryAdapter: LibVideoAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(LibVideoViewModel::class.java)

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (arguments != null) {
            Cnumber = requireArguments().getString("category_id_key")!!
        }

        libraryViewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.libraryProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        if(Cnumber.isNotEmpty()) {
            getVideoBycategory(Cnumber)
        }

        /*binding.rvTrainLibrary.layoutManager = GridLayoutManager(requireContext(), 1)

        val videoList = listOf(
            Library("Springs (Suspension Springs)", "Cracks, deformation, corrosion...", R.drawable.train_spring, 2),
            Library("Axle Box", "Overheating, cracks, oil leakage...", R.drawable.train_spring, 4),
            Library("WSP Cable (Wheel Slide Protection)", "Damage, disconnection, corrosion...", R.drawable.train_spring, 3),
            Library("Wheelsets", "Cracks, flat spots, wear...", R.drawable.train_spring, 2),
            Library("Bogies", "Structural cracks, loose fasteners...", R.drawable.train_spring, 4),
            Library("Brake Discs & Pads", "Wear, cracks, misalignment...", R.drawable.train_spring, 5)
        )

        libraryAdapter = LibraryAdapter(videoList)
        binding.rvTrainLibrary.adapter = libraryAdapter*/
        return root
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
                    binding.rvTrainLibrary.layoutManager = GridLayoutManager(requireContext(), 1)
                    libraryAdapter = LibVideoAdapter(requireContext(), videos.Data, object : LibVideoAdapter.Callback {
                        override fun onItemClicked(
                            videoUrl: String
                        ) {
                            val bundle = Bundle()
                            bundle.putString("category_id_key", videoUrl)
                            Navigation.findNavController(binding.rvTrainLibrary)
                                .navigate(R.id.libVideoFragment_to_processedFragment, bundle)
                        }
                    })
                    binding.rvTrainLibrary.adapter = libraryAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}