package com.ards.ui.info

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ards.R
import com.ards.databinding.FragmentUploadInfoBinding
import com.ards.ui.scan.ScanViewModel
import com.ards.utils.ArdsConstant
import java.io.File

class UploadInfoFragment : Fragment() {

    private var _binding: FragmentUploadInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var type: String
    private val PICK_VIDEO_REQUEST = 100
    private val STORAGE_PERMISSION_REQUEST = 101
    private var videoUri: Uri? = null
    private var stationName: String = ""
    private var sidePosition: String = ""

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

        val sideList = listOf("Select Side", "Left", "Right")

        val adapterSide = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sideList)
        binding.spnScanSide.adapter = adapterSide
        binding.spnScanSide.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val side = sideList[position]
                if (position != 0) {
                    sidePosition =  side
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        val cityList = listOf("Select Station", "New Delhi", "Mumbai Central", "Howrah Junction", "Chennai Central", "Varanasi Junction", "Bangalore City", "Lucknow Charbagh")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityList)
        binding.spinnerStationName.adapter = adapter
        binding.spinnerStationName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cityList[position]
                if (position != 0) { // Avoid selecting "Select City" as a choice
                    //tvSelectedCity.text =
                    stationName =  selectedCity
                    if(stationName.equals("New Delhi")){
                        binding.stationCode.text="NDLS"
                    }
                    else if(stationName.equals("Mumbai Central")){
                        binding.stationCode.text="MMCT"
                    }
                    else if(stationName.equals("Howrah Junction")){
                        binding.stationCode.text="HWH"
                    }
                    else if(stationName.equals("Chennai Central")){
                        binding.stationCode.text="MAS"
                    }
                    else if(stationName.equals("Varanasi Junction")){
                        binding.stationCode.text="BSB"
                    }
                    else if(stationName.equals("Bangalore City")){
                        binding.stationCode.text="SBC"
                    }
                    else if(stationName.equals("Lucknow Charbagh")){
                        binding.stationCode.text="LKO"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.continueButton.setOnClickListener {
            if (binding.trainNumber.text.isNotEmpty() && sidePosition.isNotEmpty()
                && binding.stationCode.text.isNotEmpty() && stationName.isNotEmpty()
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
                ArdsConstant.showShortToast("Please enter required fields first.", requireContext())
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
            //val file = new File(videoUri?.getPath())
            val file = getFileFromUri(requireContext(), videoUri!!)
            val bundle = Bundle()
            bundle.putString("videoUri_key", videoUri.toString())
            Navigation.findNavController(binding.continueButton)
                .navigate(R.id.action_trainInfoFragment_to_uploadFragment, bundle)
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val tempFile = File.createTempFile("ards_video", ".mp4", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
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