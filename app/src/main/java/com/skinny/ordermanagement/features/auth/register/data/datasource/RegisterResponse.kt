package com.skinny.ordermanagement.features.auth.register.data.datasource.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token")   val token: String? = null
)

