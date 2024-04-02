package com.example.bondoman.utils

import android.content.Context
import android.content.SharedPreferences
class TokenManager (context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Auth", Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    public fun saveToken(token: String){
        editor.apply {
            putString("TOKEN", token)
            apply()
        }
    }

    public fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", "")
    }

    public fun removeToken(){
        editor.apply {
            editor.remove("TOKEN")
            apply()
        }
    }
}