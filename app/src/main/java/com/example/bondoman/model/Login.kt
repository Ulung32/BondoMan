package com.example.bondoman.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    @SerializedName("nim") val nim: String,
    @SerializedName("iat") val iat: Long,
    @SerializedName("exp") val exp: Long
)