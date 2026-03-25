package com.skinny.ordermanagement.features.auth.register.data.datasource.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")      val name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email")     val email: String,
    @SerializedName("password")  val password: String
)