package com.example.bondoman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.ActivityEditTransactionBinding
import com.example.bondoman.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTransactionActivity : AppCompatActivity() {
    private val locationClient by lazy { LocationClient(this, this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")
        binding.titleEditText.text = Editable.Factory.getInstance().newEditable(title.toString())

        val nominal = intent.getIntExtra("nominal", 0)
        binding.editTextNominal.text = Editable.Factory.getInstance().newEditable(nominal.toString())

        val category = intent.getStringExtra("category")
        val date = intent.getStringExtra("date")

        //default
        var latitude = intent.getDoubleExtra("latitude", 0.0)
        var longitude = intent.getDoubleExtra("longitude", 0.0)
        binding.editLocationButton.setOnClickListener {
            locationClient.requestLocationPermissions()
            if(locationClient.haveLocationPermissions() && locationClient.inGPSActive()){
                CoroutineScope(Dispatchers.IO).launch{
                    val location = locationClient.getLocationUpdates()
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.v("lat", latitude.toString())
                    Log.v("long", longitude.toString())
                }
                Toast.makeText(applicationContext, "Location Updated", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Turn on your GPS and Grant location permissions", Toast.LENGTH_SHORT).show()
            }
        }

        binding.insertTransactionBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val nominal = binding.editTextNominal.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                if (title.isNotEmpty() && nominal.isNotEmpty()) {
                    val repository = MainRepository(applicationContext)
                    val transactionEntity = TransactionEntity(
                        _id = id,
                        title = title,
                        category = category.toString(),
                        latitude = latitude,
                        longitude = longitude,
                        nominal = nominal.toInt(),
                        date = date.toString()
                    )

                    repository.updateTransaction(transactionEntity)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Success update transaction", Toast.LENGTH_SHORT).show()
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

    }
}