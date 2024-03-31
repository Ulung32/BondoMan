package com.example.bondoman.service

import okhttp3.Response
import okhttp3.Interceptor
import okhttp3.OkHttpClient


class RetrofitClient {
    companion object {
        private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuaW0iOiIxMzUyMTE2MCIsImlhdCI6MTcxMTg3NzQ2NiwiZXhwIjoxNzExODc3NzY2fQ.gXcWW2rWjE0jNF1StQGWCR8_OvyVS7DioSti9ojrkgI"
        private val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(TOKEN))
            .build()

        private val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://pbd-backend-2024.vercel.app/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(client)
            .build()

        fun getInstance(): retrofit2.Retrofit {
            return retrofit
        }
    }
}

class AuthInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()
        return chain.proceed(request)
    }
}
