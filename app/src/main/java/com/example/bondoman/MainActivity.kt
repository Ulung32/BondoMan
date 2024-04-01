package com.example.bondoman

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.NavController
import com.example.bondoman.ui.NavbarFragment
import com.example.bondoman.ui.utils.NetworkConnectivityLiveData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()

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