package com.skinny.ordermanagement.features.admin.data.datasource.model

import com.google.gson.annotations.SerializedName

data class AdminOrderResponse(
    @SerializedName("id")          val id: String,
    @SerializedName("clientName")   val clientName: String?,
    @SerializedName("sellerName")   val sellerName: String,
    @SerializedName("deliveryName") val deliveryName: String,
    @SerializedName("total")        val total: Double,
    @SerializedName("status")       val status: String,
    @SerializedName("date")         val date: String
)

