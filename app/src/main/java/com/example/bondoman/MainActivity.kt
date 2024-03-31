package com.example.bondoman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.NavController
import com.example.bondoman.ui.NavbarFragment
import com.example.bondoman.ui.utils.NetworkConnectivityLiveData

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()

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