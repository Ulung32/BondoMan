package com.example.bondoman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.ActivityAddTransactionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.insertTransactionBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val nominal = binding.editTextNominal.text.toString()
            val category = binding.categoryEditText.text.toString()

            if(title.isNotEmpty() and nominal.isNotEmpty() and category.isNotEmpty()){
                val repository = MainRepository(applicationContext)
                val transactionEntity = TransactionEntity(
                    title = title,
                    category = category,
                    latitude = 0,
                    longitude = 0,
                    nominal = nominal.toInt(),
                    date = LocalDateTime.now().toString()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    repository.insertTransaction(transactionEntity)
                }
                val intent = Intent(applicationContext, MainActivity::class.java)
                applicationContext?.startActivity(intent)
            }else{

            }
        }
    }
}