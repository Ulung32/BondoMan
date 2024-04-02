package com.example.bondoman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.model.LoginRequest
import com.example.bondoman.service.BondoManApi
import com.example.bondoman.service.RetrofitClient
import com.example.bondoman.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                val api = BondoManApi.getInstance()

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
    }
}