package com.example.bondoman.service
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import com.example.bondoman.model.Receipt
import retrofit2.http.Multipart

interface BondoManApi {
    @Multipart
    @POST("api/bill/upload")
    suspend fun postReceipt(@Part file: MultipartBody.Part): Receipt

    companion object {
        private val ins = RetrofitClient.getInstance().create(BondoManApi::class.java)
        fun getInstance(): BondoManApi { return ins }
    }
}
