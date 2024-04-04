package com.example.bondoman.service
import android.content.Context
import android.util.Log
import com.example.bondoman.model.LoginResponse
import com.example.bondoman.model.LoginRequest
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import com.example.bondoman.model.Receipt
import com.example.bondoman.model.TokenResponse
import com.example.bondoman.utils.TokenManager
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Multipart

interface BondoManApi {
    @Multipart
    @POST("api/bill/upload")
    suspend fun postReceipt(@Part file: MultipartBody.Part): Response<Receipt>

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("api/auth/token")
    suspend fun checkToken(): Response<TokenResponse>

    companion object {
//        private var ins = RetrofitClient.getInstance().create(BondoManApi::class.java)
//        fun setToken(token: String){
//            ins = RetrofitClient.getInstance(token).create(BondoManApi::class.java)
//        }
        fun getInstance(token: String): BondoManApi {
            val ins = RetrofitClient.getInstance(token).create(BondoManApi::class.java)
            return ins
        }
    }
}
