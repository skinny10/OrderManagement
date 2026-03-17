package com.skinny.ordermanagement.features.auth.login.data.datasource.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)