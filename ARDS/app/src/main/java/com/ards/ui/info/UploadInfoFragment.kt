package com.ards.ui.info

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ards.R
import com.ards.databinding.FragmentUploadInfoBinding
import com.ards.ui.scan.ScanViewModel
import com.ards.utils.Constant

class UploadInfoFragment : Fragment() {

    private var _binding: FragmentUploadInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var type: String
    private val PICK_VIDEO_REQUEST = 100
    private val STORAGE_PERMISSION_REQUEST = 101
    private var videoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentUploadInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (arguments != null) {
            type = requireArguments().getString("scan_type")!!
        }


        binding.continueButton.setOnClickListener {
            if (binding.trainNumber.text.isNotEmpty() && binding.scanSide.text.isNotEmpty()
                && binding.stationCode.text.isNotEmpty() && binding.stationName.text.isNotEmpty()
            ) {
                if (type.equals("cameraxapi")) {
                    Navigation.findNavController(binding.continueButton)
                        .navigate(R.id.action_trainInfoFragment_to_captureFragment)
                } else if (type.equals("gallery")) {
                    if (checkStoragePermission()) {
                        openGalleryToPickVideo()
                    } else {
                        requestStoragePermission()
                    }
                }
            } else {
                Constant.showShortToast("Please enter required fields first.", requireContext())
            }
        }

        return root
    }

    // Request storage permission
    private fun requestStoragePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            STORAGE_PERMISSION_REQUEST
        )
    }

    // Check if storage permission is granted
    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Open gallery to pick video
    private fun openGalleryToPickVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryToPickVideo()
            } else {
                Toast.makeText(requireActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle video selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            videoUri = data.data
            val bundle = Bundle()
            bundle.putString("videoUri_key", videoUri.toString())
            Navigation.findNavController(binding.continueButton)
                .navigate(R.id.action_trainInfoFragment_to_uploadFragment, bundle)
            Toast.makeText(requireActivity(), "Video Selected: $videoUri", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXVideoCapture"
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}