package com.example.bondoman

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.model.LoginRequest
import com.example.bondoman.service.BondoManApi
import com.example.bondoman.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var networkConnectivityObserver : LiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                val api = BondoManApi.getInstance("")

                CoroutineScope(Dispatchers.IO).launch {
                    val response = api.login(LoginRequest(email, password))
                    if(!response.isSuccessful){
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext, "failed login", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Log.e("Login", response.body()?.token.toString())
                        val tokenManager = TokenManager(applicationContext)
                        tokenManager.saveToken(response.body()?.token.toString())
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext, "login success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        if(!isOnline(applicationContext)){
            val intent = Intent(this, ErrorPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear back stack
            startActivity(intent)
        }
    }

    private fun isOnline(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}