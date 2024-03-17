package com.example.bondoman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bondoman.ui.NavbarFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = NavbarFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragmentNavbar, fragment).commit()
    }
}