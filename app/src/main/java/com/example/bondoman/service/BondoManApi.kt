package com.example.bondoman.service
import android.util.Log
import com.example.bondoman.model.LoginResponse
import com.example.bondoman.model.LoginRequest
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import com.example.bondoman.model.Receipt
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart

interface BondoManApi {
    @Multipart
    @POST("api/bill/upload")
    suspend fun postReceipt(@Part file: MultipartBody.Part): Receipt

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    companion object {
        private val ins = RetrofitClient.getInstance().create(BondoManApi::class.java)
        fun getInstance(): BondoManApi { return ins }
    }
}
