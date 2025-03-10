package com.ards.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ards.R
import com.ards.databinding.FragmentLibraryBinding
import com.ards.ui.history.adapter.HistoryAdapter
import com.ards.ui.history.model.Recent
import com.ards.ui.library.adapter.LibraryAdapter
import com.ards.ui.library.model.Library

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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

        binding.rvTrainLibrary.layoutManager = GridLayoutManager(requireContext(), 1)

        val videoList = listOf(
            Library("Springs (Suspension Springs)", "Cracks, deformation, corrosion...", R.drawable.train_spring, 2),
            Library("Axle Box", "Overheating, cracks, oil leakage...", R.drawable.train_spring, 4),
            Library("WSP Cable (Wheel Slide Protection)", "Damage, disconnection, corrosion...", R.drawable.train_spring, 3),
            Library("Wheelsets", "Cracks, flat spots, wear...", R.drawable.train_spring, 2),
            Library("Bogies", "Structural cracks, loose fasteners...", R.drawable.train_spring, 4),
            Library("Brake Discs & Pads", "Wear, cracks, misalignment...", R.drawable.train_spring, 5)
        )

        libraryAdapter = LibraryAdapter(videoList)
        binding.rvTrainLibrary.adapter = libraryAdapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}