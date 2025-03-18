package com.ards.ui.capture

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
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

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var selectedQuality: Quality = Quality.FHD
    private lateinit var cameraExecutor: ExecutorService

    private var isRecording = false
    private var isPaused = false
    private var recordingTime = 0 // Time in seconds
    private val handler = Handler(Looper.getMainLooper())

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)

        _binding = FragmentCaptureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), requiredPermissions, 10)
        }

        setupButtons()
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

    private fun setupButtons() {
        binding.btnStart.setOnClickListener {
            if (!isRecording) {
                startRecording()
            }
        }

        binding.btnPause.setOnClickListener {
            pauseRecording()
        }

        binding.btnStop.setOnClickListener {
            stopRecording()
        }
    }

    private fun setupFpsSpinner() {
        val fpsOptions = listOf("15", "24", "30", "60")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fpsOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFps.adapter = adapter

        binding.spinnerFps.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                restartCamera()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupQualitySpinner() {
        val qualityOptions = listOf("SD", "HD", "FULL HD", "4K")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, qualityOptions)
        binding.spinnerQuality.adapter = adapter

        binding.spinnerQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedQuality = getQuality(qualityOptions[position])
                restartCamera()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun restartCamera() {
        startCamera()
    }

    private fun getQuality(quality: String): Quality {
        return when (quality) {
            "SD" -> Quality.SD
            "HD" -> Quality.HD
            "FULL HD" -> Quality.FHD
            "4K" -> Quality.UHD
            else -> Quality.FHD
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()
                .also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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
                resetUI()
            }
        }

        isRecording = true
        isPaused = false
        recordingTime = 0
        updateTimer()
        handler.post(timerRunnable)
        updateUI()
    }

    private fun pauseRecording() {
        if (!isPaused) {
            recording?.pause()
            isPaused = true
            handler.removeCallbacks(timerRunnable)
            binding.btnPause.text = "Resume"
        } else {
            recording?.resume()
            isPaused = false
            handler.post(timerRunnable)
            binding.btnPause.text = "Pause"
        }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        resetUI()
    }

    private fun updateUI() {
        binding.btnStart.isEnabled = !isRecording
        binding.btnPause.isEnabled = isRecording
        binding.btnStop.isEnabled = isRecording
    }

    private fun resetUI() {
        isRecording = false
        isPaused = false
        recordingTime = 0
        binding.tvTimer.text = "00:00"
        handler.removeCallbacks(timerRunnable)
        binding.btnPause.text = "Pause"
        updateUI()
    }

    private fun updateTimer() {
        val minutes = recordingTime / 60
        val seconds = recordingTime % 60
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (!isPaused) {
                recordingTime++
                updateTimer()
            }
            handler.postDelayed(this, 1000)
        }
    }
}
