package com.ards.ui.capture

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ards.databinding.FragmentCaptureBinding
import com.ards.ui.scan.ScanViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureFragment : Fragment() {

    private var _binding: FragmentCaptureBinding? = null
    private val binding get() = _binding!!
    var selected_quality: String = ""
    private var selectedFps: Int = 30
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var selectedQuality: Quality = Quality.FHD // Default quality
    private lateinit var cameraExecutor: ExecutorService
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentCaptureBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Request permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), requiredPermissions, 10)
        }

        binding.btnRecord.setOnClickListener {
            if (recording != null) {
                stopRecording()
            } else {
                startRecording()
            }
        }
        setupFpsSpinner()
        setupQualitySpinner()
        cameraExecutor = Executors.newSingleThreadExecutor()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    private fun allPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupFpsSpinner() {

        val fpsOptions = listOf("15", "24", "30", "60") // Available FPS values
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fpsOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerFps.adapter = adapter

        binding.spinnerFps.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFps = fpsOptions[position].toInt()
                restartCameraWithNewFps() // Restart camera when FPS changes
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun restartCameraWithNewFps() {
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder()
                .setTargetResolution(Size(1280, 720)) // Adjust resolution if needed
                .build()
                .also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Get user selection
            //val selectedFps = spinnerFps.selectedItem.toString()
            selected_quality = binding.spinnerQuality.selectedItem?.toString() ?: "HD" // Default to HD
            //val selectedQuality = getQualitySelector(selected_quality)
            val qualitySelector = QualitySelector.from(selectedQuality)
            val recorder = Recorder.Builder()
                .setQualitySelector(qualitySelector)
                .build()

            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, videoCapture)
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun setupQualitySpinner() {
        val qualityOptions = listOf("SD", "HD", "FULL HD", "4K") // Available video qualities
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, qualityOptions)
        binding.spinnerQuality.adapter = adapter

        binding.spinnerQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedQuality = getQualitySelector(qualityOptions[position])
                restartCameraWithNewQuality()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun restartCameraWithNewQuality() {
        startCamera()
    }
    private fun getQualitySelector(quality: String): Quality {
        return when (quality) {
            "SD" -> Quality.SD
            "HD" -> Quality.HD
            "FULL HD" -> Quality.FHD
            "4K" -> Quality.UHD
            else -> Quality.FHD // Default to Full HD
        }
    }


    private fun startRecording() {
        val file = File(requireContext().externalMediaDirs.firstOrNull(), "ards_${System.currentTimeMillis()}.mp4")
        val outputOptions = FileOutputOptions.Builder(file).build()

        val recorder = videoCapture?.output ?: return

        val pendingRecording = recorder.prepareRecording(requireActivity(), outputOptions)

        recording = pendingRecording.start(ContextCompat.getMainExecutor(requireActivity())) { event ->
            if (event is VideoRecordEvent.Finalize) {
                if (!event.hasError()) {
                    Toast.makeText(requireActivity(), "Video saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("CameraX", "Error: ${event.error}")
                }
                recording = null
                binding.btnRecord.text = "Start Recording"
            }
        }

        binding.btnRecord.text = "Stop Recording"
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        binding.btnRecord.text = "Start Recording"
    }
}