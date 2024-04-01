package com.example.bondoman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

        // Mengambil data dari Intent
        val randomTitle = intent.getStringExtra("RANDOM_TITLE")
        val randomNominal = intent.getIntExtra("RANDOM_NOMINAL", 0)

        // Mengikat data ke view
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
                var latitude: Double
                var longitude: Double
                if(!locationClient.haveLocationPermissions()){
                    //default
                    latitude = -6.890430928361903
                    longitude = 107.61095236101004
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

        val items = listOf("PEMASUKAN", "PENGELUARAN")

        val autoComplete = findViewById<AutoCompleteTextView>(R.id.category)
        val adapter = ArrayAdapter(this, R.layout.list_item, items)

        autoComplete.setAdapter(adapter)

    }

}