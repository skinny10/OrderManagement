package com.skinny.ordermanagement.features.admin.data.datasource.model

import com.google.gson.annotations.SerializedName

data class AdminClientResponse(
    @SerializedName("id")          val id: String?,
    @SerializedName("name")        val name: String,
    @SerializedName("phone")       val phone: String,
    @SerializedName("address")     val address: String,
    @SerializedName("totalOrders") val totalOrders: Int = 0
)

