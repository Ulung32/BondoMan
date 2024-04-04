package com.example.bondoman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.NavController
import com.example.bondoman.service.LoginService
import com.example.bondoman.ui.NavbarFragment
import com.example.bondoman.utils.NetworkConnectivityLiveData
import com.example.bondoman.utils.TokenManager

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "ACTION_RANDOMIZE_TRANSACTION") {
                val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
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

        val intent = Intent(this, LoginService::class.java)
        startService(intent)

        val tokenManager = TokenManager(this)
        if(tokenManager.getToken().isNullOrEmpty()){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear back stack
            startActivity(intent)
        }

        val filter = IntentFilter("ACTION_RANDOMIZE_TRANSACTION")
        registerReceiver(receiver, filter)

        setContentView(R.layout.activity_main)

        val fragment = NavbarFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentNavbar, fragment).commit()

        val networkConnectivityObserver = NetworkConnectivityLiveData(applicationContext)
        val alert = findViewById<TextView>(R.id.alert_no_network)
        networkConnectivityObserver.observe(this) {
            if(it){
                alert.visibility = View.GONE
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                alert.visibility = View.VISIBLE
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}