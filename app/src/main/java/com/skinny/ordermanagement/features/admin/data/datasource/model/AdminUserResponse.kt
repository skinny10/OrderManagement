package com.skinny.ordermanagement.features.admin.data.datasource.model

import com.google.gson.annotations.SerializedName

data class AdminUserResponse(
    @SerializedName("_id")          val id: String,
    @SerializedName("name")         val name: String,
    @SerializedName("role")         val role: String,
    @SerializedName("email")        val email: String,
    @SerializedName("activeOrders") val activeOrders: Int = 0
)

data class CreateUserRequest(
    @SerializedName("name")      val name: String,
    @SerializedName("lastName")  val lastName: String,  // 👈 agregado
    @SerializedName("role")      val role: String,
    @SerializedName("email")     val email: String,
    @SerializedName("password")  val password: String   // 👈 agregado
)

data class CreateUserResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("user")    val user: AdminUserResponse? = null
)

data class DeleteResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)