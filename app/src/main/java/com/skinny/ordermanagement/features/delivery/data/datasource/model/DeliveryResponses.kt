package com.skinny.ordermanagement.features.delivery.data.datasource.model

import com.google.gson.annotations.SerializedName

data class DeliveryOrderResponse(
    @SerializedName("id")         val id: String,
    @SerializedName("clientName") val clientName: String,
    @SerializedName("address")    val address: String,
    @SerializedName("total")      val total: Double,
    @SerializedName("status")     val status: String,
    @SerializedName("date")       val date: String
)

data class DeliveryStatsResponse(
    @SerializedName("totalOrders")     val totalOrders: Int,
    @SerializedName("pendingOrders")   val pendingOrders: Int,
    @SerializedName("onWayOrders")     val onWayOrders: Int,
    @SerializedName("deliveredOrders") val deliveredOrders: Int,
    @SerializedName("recentOrders")    val recentOrders: List<DeliveryOrderResponse>
)

data class UpdateOrderStatusRequest(
    @SerializedName("status") val status: String
)

data class UpdateOrderStatusResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

data class DeleteResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)



