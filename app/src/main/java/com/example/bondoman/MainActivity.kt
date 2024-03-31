package com.example.bondoman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.NavController
import com.example.bondoman.ui.NavbarFragment
import com.example.bondoman.utils.NetworkConnectivityLiveData

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_RANDOMIZE_TRANSACTION") {
                val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Karakter yang mungkin
                val randomTitleValue = (1..10)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")

                val randomNominalValue = (1000..2000).random()

                val intent = Intent(context, AddTransactionActivity::class.java).apply {
                    putExtra("RANDOM_TITLE", randomTitleValue)
                    putExtra("RANDOM_NOMINAL", randomNominalValue)
                }
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()

        val filter = IntentFilter("ACTION_RANDOMIZE_TRANSACTION")
        registerReceiver(receiver, filter)

        setContentView(R.layout.activity_main)

        val fragment = NavbarFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentNavbar, fragment).commit()

        val networkConnectivityObserver = NetworkConnectivityLiveData(applicationContext)
        networkConnectivityObserver.observe(this) {
            if(it){
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}