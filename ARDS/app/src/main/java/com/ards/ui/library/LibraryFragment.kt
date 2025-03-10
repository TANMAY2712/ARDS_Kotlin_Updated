package com.ards.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ards.databinding.FragmentLibraryBinding
import com.ards.ui.library.adapter.LibraryAdapter

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val libraryViewModel: LibraryViewModel by viewModels()
    private lateinit var libraryAdapter: LibraryAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(LibraryViewModel::class.java)

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        libraryViewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.libraryProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        getLibrary("LibraryCategory",0)

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
    private fun getLibrary(type: String, Id: Int) {
        libraryViewModel.getLibrary(type, Id)
            .observe(viewLifecycleOwner) { result ->

                result.onSuccess { notifications ->
                    // Setting up RecyclerView
                    binding.rvTrainLibrary.layoutManager = GridLayoutManager(requireContext(), 1)
                    libraryAdapter = LibraryAdapter(requireContext(), notifications.Data)
                    binding.rvTrainLibrary.adapter = libraryAdapter
                }

                result.onFailure { error ->
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}