package com.example.bondoman.ui

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
import com.example.bondoman.MainActivity
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.FragmentScannerBinding
import com.example.bondoman.service.BondoManApi
import com.example.bondoman.util.CameraClient
import com.example.bondoman.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageCapture: ImageCapture

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private val cameraClient by lazy { CameraClient(requireContext(), requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)

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

//                    CoroutineScope(Dispatchers.IO).launch {
//                        try {
//                            val tokenManager = TokenManager(requireContext())
//                            val response = BondoManApi.getInstance(tokenManager.getToken()!!).postReceipt(body)
//                            withContext(Dispatchers.Main) {
//                                val alertDialog = AlertDialog.Builder(requireContext())
//                                alertDialog.setTitle("Success")
//                                alertDialog.setMessage("Receipt uploaded successfully. \n$response")
//                                alertDialog.setPositiveButton("Recapture") { dialog, _ ->
//                                    dialog.dismiss()
//                                }
//                                alertDialog.setNegativeButton("Add to transaction list") { dialog, _ ->
//                                    val repository =
//                                        MainRepository(requireContext().applicationContext)
//                                    // Menambahkan response ke repository
//                                    response.items.items.forEach { item ->
//                                        val transactionEntity = TransactionEntity(
//                                            title = item.name,
//                                            category = "Penjualan",
//                                            latitude = 0.0,
//                                            longitude = 0.0,
//                                            nominal = (item.qty * item.price).toInt(),
//                                            date = LocalDateTime.now().toString()
//                                        )
//                                        CoroutineScope(Dispatchers.IO).launch {
//                                            repository.insertTransaction(transactionEntity)
//                                        }
//                                    }
//
//                                    val intent = Intent(requireContext(), MainActivity::class.java)
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    startActivity(intent)
//                                    requireActivity().finish()
//                                }
//                                alertDialog.show()
//                            }
//                        } catch (e: Exception) {
//                            withContext(Dispatchers.Main) {
//                                val alertDialog = AlertDialog.Builder(requireContext())
//                                alertDialog.setMessage("Something Wrong happened. \n${e.message}")
//                                alertDialog.setPositiveButton("OK") { dialog, _ ->
//                                    dialog.dismiss()
//                                }
//                                alertDialog.show()
//                            }
//                        }
//                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val tokenManager = TokenManager(requireContext())
                            val response = BondoManApi.getInstance(tokenManager.getToken()!!).postReceipt(body)
                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val items = response.body()?.items?.items
                                    val itemsString = items?.joinToString(separator = "\n") { item ->
                                        "Name: ${item.name}, Qty: ${item.qty}, Price: ${item.price}"
                                    }
                                    val alertDialog = AlertDialog.Builder(requireContext())
                                    alertDialog.setTitle("Success")
                                    alertDialog.setMessage("Receipt uploaded successfully. \n$itemsString")

//                                    val alertDialog = AlertDialog.Builder(requireContext())
//                                    alertDialog.setTitle("Success")
//                                    alertDialog.setMessage("Receipt uploaded successfully. \n${response.body()?.toString()}")

                                    alertDialog.setPositiveButton("Recapture") { dialog, _ ->
                                        dialog.dismiss()
                                    }
//                                    alertDialog.setNegativeButton("Add to transaction list") { dialog, _ ->
//                                        val repository =
//                                            MainRepository(requireContext().applicationContext)
//                                        // Menambahkan response ke repository
//                                        response.body()?.items?.items?.forEach { item ->
//                                            val transactionEntity = TransactionEntity(
//                                                title = item.name,
//                                                category = "PEMASUKAN",
//                                                latitude = 0.0,
//                                                longitude = 0.0,
//                                                nominal = (item.qty * item.price).toInt(),
//                                                date = LocalDateTime.now().toString()
//                                            )
//                                            CoroutineScope(Dispatchers.IO).launch {
//                                                repository.insertTransaction(transactionEntity)
//                                            }
//                                        }
//
//                                        val intent = Intent(requireContext(), MainActivity::class.java)
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        startActivity(intent)
//                                        requireActivity().finish()
//                                    }

                                    alertDialog.setNegativeButton("Add to transaction list") { dialog, _ ->
                                        val repository = MainRepository(requireContext().applicationContext)
                                        val random = (100000..999999).random() // Generate random number for receipt name
                                        val receiptName = "Receipt $random"

                                        var totalNominal = 0
                                        val items = response.body()?.items?.items
                                        items?.forEach { item ->
                                            totalNominal += (item.qty * item.price).toInt()
                                        }

                                        val transactionEntity = TransactionEntity(
                                            title = receiptName,
                                            category = "PEMASUKAN",
                                            latitude = 0.0,
                                            longitude = 0.0,
                                            nominal = totalNominal,
                                            date = LocalDateTime.now().toString()
                                        )

                                        CoroutineScope(Dispatchers.IO).launch {
                                            repository.insertTransaction(transactionEntity)
                                        }

                                        val intent = Intent(requireContext(), MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }

                                    alertDialog.show()
                                } else {
                                    throw IOException("Error occurred during upload. Response code: ${response.code()}")
                                }
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