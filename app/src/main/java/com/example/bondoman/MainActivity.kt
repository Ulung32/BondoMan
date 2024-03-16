package com.example.bondoman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.ui.Transaction.TransactionAdapter
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.transaction)

        val rv: RecyclerView = findViewById(R.id.transaction_rv)
        rv.layoutManager = LinearLayoutManager(this)

//        val data = arrayOf(
//            TransactionEntity(1, "title1", "pemasukan", 10000, 1, 1, LocalDateTime.now() ),
//            TransactionEntity(2, "title2", "pemasukan", 10000, 1, 1, LocalDateTime.now() ),
//            TransactionEntity(3, "title3", "pemasukan", 10000, 1, 1, LocalDateTime.now() )
//        )
        val data = listOf(
            TransactionEntity(1, "title1", "pemasukan", 10000, 1, 1, LocalDateTime.now()),
            TransactionEntity(2, "title2", "pemasukan", 10000, 1, 1, LocalDateTime.now()),
            TransactionEntity(3, "title3", "pemasukan", 10000, 1, 1, LocalDateTime.now())
        )


        rv.adapter = TransactionAdapter(data)
    }
}