package com.example.bondoman.ui

import android.graphics.Color
import android.os.*
import android.view.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bondoman.databinding.FragmentTwibbonBinding
import com.example.bondoman.utils.CameraClient
import com.example.bondoman.R

class TwibbonFragment : Fragment() {
    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageCapture: ImageCapture
    private var previewFrozen: Boolean = false

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private val cameraClient by lazy { CameraClient(requireContext(), requireActivity()) }
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraClient.requestCameraPermission()
        var hasPermission = cameraClient.hasCameraPermission()
        if(hasPermission){
            startCamera()
            binding.captureBtn.setOnClickListener{
                freezePreview()
            }

            binding.changeTwibbonBtn.setOnClickListener {
                changeTwibbon()
            }

            binding.backBtn.setOnClickListener {
                findNavController().navigate(R.id.settingFragment)
            }
        }else{
            binding.cameraPv.setBackgroundColor(Color.WHITE)
        }
    }

    private fun freezePreview() {
        previewFrozen = !previewFrozen
        if (!previewFrozen) {
            binding.captureBtn.text = getString(R.string.capture)
        } else {
            binding.captureBtn.text = getString(R.string.retake)
        }
        startCamera()
    }

    private fun changeTwibbon() {
        val twibbonDrawables = intArrayOf(
            R.drawable.twibbon_1,
            R.drawable.twibbon_2,
            R.drawable.twibbon_3
        )
        binding.twibbonIv.setImageResource(twibbonDrawables[currentIndex])

        binding.changeTwibbonBtn.setOnClickListener {
            currentIndex = (currentIndex + 1) % twibbonDrawables.size
            binding.twibbonIv.setImageResource(twibbonDrawables[currentIndex])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUserCases() {
//        val preview = Preview.Builder()
//            .build()
//            .also {
//                it.setSurfaceProvider(binding.cameraPv.surfaceProvider)
//            }

        val preview = Preview.Builder().build().also {
            if (!previewFrozen) {
                it.setSurfaceProvider(binding.cameraPv.surfaceProvider)
            } else {
                it.setSurfaceProvider(null)
            }
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        try {
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
