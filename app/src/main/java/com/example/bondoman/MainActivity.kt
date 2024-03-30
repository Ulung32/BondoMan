package com.example.bondoman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bondoman.ui.NavbarFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val fragment = NavbarFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragmentNavbar, fragment).commit()
    }
}