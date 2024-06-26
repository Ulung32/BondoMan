package com.example.bondoman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.ActivityAddTransactionBinding
import com.example.bondoman.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class AddTransactionActivity : AppCompatActivity() {
    private val locationClient by lazy { LocationClient(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationClient.requestLocationPermissions()

        if(!locationClient.haveLocationPermissions() || !locationClient.inGPSActive()){
            Toast.makeText(applicationContext, "location set to default", Toast.LENGTH_SHORT).show()
        }

        // Mengambil data dari Intent
        val randomTitle = intent.getStringExtra("RANDOM_TITLE")
        val randomNominal = intent.getIntExtra("RANDOM_NOMINAL", 0)

        if (!randomTitle.isNullOrEmpty()) {
            binding.titleEditText.setText(randomTitle)
        }

        if (randomNominal != 0) {
            binding.editTextNominal.setText(randomNominal.toString())
        }

        binding.insertTransactionBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val nominal = binding.editTextNominal.text.toString()
            val category = binding.category.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                //default
                var latitude: Double = -6.890430928361903
                var longitude: Double = 107.61095236101004

                if(locationClient.haveLocationPermissions() && locationClient.inGPSActive()){
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
                        Toast.makeText(applicationContext, "Success add transaction", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val items = listOf("PEMASUKAN", "PENGELUARAN")

        val autoComplete = findViewById<AutoCompleteTextView>(R.id.category)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)

        autoComplete.setAdapter(adapter)

    }

}