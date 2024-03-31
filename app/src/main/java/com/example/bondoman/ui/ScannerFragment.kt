package com.example.bondoman.ui

import ScannerViewModel
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bondoman.MainActivity
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.FragmentScannerBinding
import com.example.bondoman.service.BondoManApi
import com.example.bondoman.util.CameraClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class ScannerFragment : Fragment() {
//    private lateinit var binding: FragmentScannerBinding
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageCapture: ImageCapture

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.CAMERA
        )
    } else {
        arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
//    private var hasPermission: Boolean = false
    private lateinit var cameraSelector: CameraSelector
    private val listPermissionNeeded = arrayListOf<String>()
    private val cameraClient by lazy { CameraClient(requireContext(), requireActivity()) }
    private val cameraViewModel: ScannerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)


//        cameraViewModel.requestCameraPermission(cameraClient)
//
//        val permission_Observer = Observer<Boolean> { newValue: Boolean ->
//            Log.v("tes", hasPermission.toString())
//            if((hasPermission == false && newValue == true) || (hasPermission && newValue)) {
//                startCamera()
//                binding.sendBtn.setOnClickListener{
//                    takePhoto()
//                }
//            } else {
//                binding.cameraPv.setBackgroundColor(Color.WHITE)
//            }
//            hasPermission = newValue
//        }
//        cameraViewModel.hasCameraPermission.observe(viewLifecycleOwner, permission_Observer)


//        cameraViewModel.hasCameraPermission.observe(viewLifecycleOwner) { hasPermission: Boolean ->
//            if (hasPermission) {
//                startCamera()
////
//                binding.sendBtn.setOnClickListener {
//                    takePhoto()
//                }
//            } else {
//                binding.cameraPv.setBackgroundColor(Color.WHITE)
//            }
//        }



//        hasPermission = cameraClient.requestCameraPermission()
////        if (checkMultiplePermission()) {
//        if (hasPermission) {
//            startCamera()
//
//            binding.sendBtn.setOnClickListener {
//                takePhoto()
//            }
//        } else {
////            val alertDialogBuilder = AlertDialog.Builder(requireContext())
////            alertDialogBuilder.setTitle("Permission Required")
////            alertDialogBuilder.setMessage("This app requires camera permission to function properly. Please grant the permission in the app settings.")
////            alertDialogBuilder.setPositiveButton("Go to Settings") { dialog, _ ->
////                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
////                val uri = Uri.fromParts("package", requireActivity().packageName, null)
////                intent.data = uri
////                startActivity(intent)
////            }
////            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
////                dialog.dismiss()
////            }
////            val alertDialog = alertDialogBuilder.create()
////            alertDialog.show()
//            binding.cameraPv.setBackgroundColor(Color.WHITE)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraClient.requestCameraPermission()
        var hasPermission = cameraClient.hasCameraPermission()
        if(hasPermission){
            startCamera()
            binding.sendBtn.setOnClickListener{
                takePhoto()
            }
        }else{
            binding.cameraPv.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun checkMultiplePermission(): Boolean {
//        for (permission in multiplePermissionNameList) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    permission
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                listPermissionNeeded.add(permission)
//            }
//        }
//        if (listPermissionNeeded.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                listPermissionNeeded.toTypedArray(),
//                multiplePermissionId
//            )
//            return false
//        }
//        return true
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == multiplePermissionId) {
//            if (grantResults.isNotEmpty()) {
//                var isGrant = true
//                for (element in grantResults) {
//                    if (element == PackageManager.PERMISSION_DENIED) {
//                        isGrant = false
//                        break
//                    }
//                }
//                if (isGrant) {
//                    startCamera()
//                } else {
//                    // Handle permission denied
//                    val someDenied = listPermissionNeeded.any { permission ->
//                        !ActivityCompat.shouldShowRequestPermissionRationale(
//                            requireActivity(),
//                            permission
//                        )
//                    }
//                    if (someDenied) {
//                        // Permission denied with "never ask again" option
//                        // You can open settings manually here
//                        // Or display a dialog explaining the user how to grant the permission
//                        val alertDialogBuilder = AlertDialog.Builder(requireContext())
//                        alertDialogBuilder.setTitle("Permission Required")
//                        alertDialogBuilder.setMessage("This app requires camera permission to function properly. Please grant the permission in the app settings.")
//                        alertDialogBuilder.setPositiveButton("Go to Settings") { dialog, _ ->
//                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
//                            intent.data = uri
//                            startActivity(intent)
//                        }
//                        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                            dialog.dismiss()
//                        }
//                        val alertDialog = alertDialogBuilder.create()
//                        alertDialog.show()
//                        binding.cameraPv.setBackgroundColor(Color.WHITE)
//                    } else {
//                        // Permission denied
//                        // You can display a warning dialog or request the permissions again
//                        binding.cameraPv.setBackgroundColor(Color.WHITE)
//                    }
//                }
//            }
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == multiplePermissionId) {
//            if (grantResults.isNotEmpty()) {
//                var isGrant = true
//                for (element in grantResults) {
//                    if (element == PackageManager.PERMISSION_DENIED) {
//                        isGrant = false
//                    }
//                }
//                if (isGrant) {
//                    // here all permission granted successfully
//                    startCamera()
//                } else {
//                    var someDenied = false
//                    for (permission in permissions) {
//                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                                requireActivity(),
//                                permission
//                            )
//                        ) {
//                            if (ActivityCompat.checkSelfPermission(
//                                    requireContext(),
//                                    permission
//                                ) == PackageManager.PERMISSION_DENIED
//                            ) {
//                                someDenied = true
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUserCases() {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(binding.cameraPv.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
//            setUpZoomTapToFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun takePhoto() {
        val imageFolder = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "Images"
        )
        if (!imageFolder.exists()) {
            imageFolder.mkdir()
        }

        val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(System.currentTimeMillis()) + ".jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val fileUri = outputFileResults.savedUri ?: return
                    val requestFile = requireContext().contentResolver.openInputStream(fileUri)
                    val requestBody =
                        requestFile?.readBytes()?.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    val body = requestBody!!.let {
                        MultipartBody.Part.createFormData(
                            "file",
                            "image.jpg",
                            it
                        )
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = BondoManApi.getInstance().postReceipt(body)
                            withContext(Dispatchers.Main) {
                                val alertDialog = AlertDialog.Builder(requireContext())
                                alertDialog.setTitle("Success")
                                alertDialog.setMessage("Receipt uploaded successfully. \n$response")
                                alertDialog.setPositiveButton("Recapture") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.setNegativeButton("Add to transaction list") { dialog, _ ->
                                    val repository =
                                        MainRepository(requireContext().applicationContext)
                                    // Menambahkan response ke repository
                                    response.items.items.forEach { item ->
                                        val transactionEntity = TransactionEntity(
                                            title = item.name,
                                            category = "Penjualan",
                                            latitude = 0.0,
                                            longitude = 0.0,
                                            nominal = (item.qty * item.price).toInt(),
                                            date = LocalDateTime.now().toString()
                                        )
                                        CoroutineScope(Dispatchers.IO).launch {
                                            repository.insertTransaction(transactionEntity)
                                        }
                                    }

                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    requireActivity().finish()

//                                    val supportFragmentManager = requireActivity().supportFragmentManager
////                                    supportFragmentManager.replace(R.id._firstFragment, TransactionFragment())
//                                    supportFragmentManager.beginTransaction().replace(R.id.fragmentNavbar, TransactionFragment()).commit()

//                                    supportFragmentManager.addToBackStack(null) // Optional, untuk menambahkan ke back stack
//                                    supportFragmentManager.commit()
                                }
                                alertDialog.show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                val alertDialog = AlertDialog.Builder(requireContext())
                                alertDialog.setMessage("Something Wrong happened. \n${e.message}")
                                alertDialog.setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        exception.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}