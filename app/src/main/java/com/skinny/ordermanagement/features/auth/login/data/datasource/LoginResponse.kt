package com.skinny.ordermanagement.features.auth.login.data.datasource.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token")   val token: String? = null,
    @SerializedName("role")    val role: String? = null
)

