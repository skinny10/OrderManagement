package com.skinny.ordermanagement.features.seller.data.datasource.model

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("clientId") val clientId: String,
    @SerializedName("total") val total: Double,
    @SerializedName("items") val items: List<Any> = emptyList()
)

data class OrderItemRequest(
    @SerializedName("productId") val productId: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double
)


