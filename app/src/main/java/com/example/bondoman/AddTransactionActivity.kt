package com.example.bondoman

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.ActivityAddTransactionBinding
import com.example.bondoman.util.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.time.LocalDateTime

class AddTransactionActivity : AppCompatActivity() {
    private val locationClient by lazy { LocationClient(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationClient.requestLocationPermissions()

        binding.insertTransactionBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val nominal = binding.editTextNominal.text.toString()
            val category = binding.categoryEditText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                var latitude: Double
                var longitude: Double
                if(!locationClient.haveLocationPermissions()){
                    //default
                    latitude = 0.0
                    longitude = 0.0
                }else{
                    val location = locationClient.getLocationUpdates()
                    latitude = location.latitude
                    longitude = location.longitude
                }

                if (title.isNotEmpty() && nominal.isNotEmpty() && category.isNotEmpty()) {
                    val repository = MainRepository(applicationContext)
                    val transactionEntity = TransactionEntity(
                        title = title,
                        category = category,
                        latitude = latitude,
                        longitude = longitude,
                        nominal = nominal.toInt(),
                        date = LocalDateTime.now().toString()
                    )

                    repository.insertTransaction(transactionEntity)

                    withContext(Dispatchers.Main) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // Show a popup or handle empty fields
                }
            }
        }
    }

}