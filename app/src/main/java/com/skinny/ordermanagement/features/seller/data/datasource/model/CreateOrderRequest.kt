package com.skinny.ordermanagement.features.seller.data.datasource.model

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("clientId") val clientId: String,
    @SerializedName("total") val total: Double
)
