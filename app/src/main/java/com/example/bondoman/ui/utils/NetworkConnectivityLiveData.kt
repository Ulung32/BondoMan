package com.example.bondoman.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import android.os.Build

class NetworkConnectivityLiveData(
    private val context: Context
): LiveData<Boolean>() {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkConnectivityCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updater()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager.registerDefaultNetworkCallback(connectionCallback())
            }else -> {
                context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (::networkConnectivityCallback.isInitialized) {
                connectivityManager.unregisterNetworkCallback(networkConnectivityCallback)
            }
        } else {
            context.unregisterReceiver(receiver)
        }
    }

    private fun connectionCallback(): ConnectivityManager.NetworkCallback {
        networkConnectivityCallback = object: ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }
        return networkConnectivityCallback
    }
    private fun updater(){
        val networkConnection = connectivityManager.activeNetworkInfo
        postValue(networkConnection?.isConnected)
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updater()
        }
    }
}