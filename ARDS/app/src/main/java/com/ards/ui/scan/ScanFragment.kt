package com.ards.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ards.R
import com.ards.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnPlayground.setOnClickListener {
            findNavController().navigate(R.id.playgroundFragment)
        }
        binding.btnScanVideo.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("scan_type", "cameraxapi")
            Navigation.findNavController(binding.btnScanVideo)
                .navigate(R.id.action_scanFragment_to_captureFragment, bundle)
        }
        binding.btnUploadVideo.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("scan_type", "gallery")
            Navigation.findNavController(binding.btnUploadVideo)
                .navigate(R.id.action_scanFragment_to_uploadFragment, bundle)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}