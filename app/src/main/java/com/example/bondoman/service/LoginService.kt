package com.example.bondoman.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.bondoman.LoginActivity
import com.example.bondoman.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginService : Service() {
    private lateinit var tokenManager : TokenManager
    private lateinit var handler: Handler
    private lateinit var tokenChecker: Runnable
    private lateinit var bondoManApi: BondoManApi
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.v("Login service", "HALOOOOOO")
        handler = Handler()
        tokenManager = TokenManager(this)
        tokenChecker = Runnable {
            if(isOnline(applicationContext)){
                CoroutineScope(Dispatchers.IO).launch {
                    bondoManApi = BondoManApi.getInstance(tokenManager.getToken()!!)
                    val response = bondoManApi.checkToken()
                    Log.v("dimas", response.body()?.exp.toString())
                    if(!response.isSuccessful){
                        tokenManager.removeToken()
                        withContext(Dispatchers.Main){
                            val intent = Intent(this@LoginService, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear back stack
                            startActivity(intent)
                            Toast.makeText(this@LoginService, "Session has already experid", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            handler.postDelayed(tokenChecker, 3 * 60 * 1000)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(tokenChecker)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(tokenChecker)
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