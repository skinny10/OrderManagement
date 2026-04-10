package com.skinny.ordermanagement.features.seller.data.datasource.model

import com.google.gson.annotations.SerializedName

data class SellerClientResponse(
    @SerializedName("id")          val id: String?,
    @SerializedName("name")        val name: String,
    @SerializedName("phone")       val phone: String,
    @SerializedName("address")     val address: String,
    @SerializedName("totalOrders") val totalOrders: Int = 0
)

data class SellerOrderResponse(
    @SerializedName("id")          val id: String,
    @SerializedName("clientName")  val clientName: String,
    @SerializedName("address")     val address: String,
    @SerializedName("total")       val total: Double,
    @SerializedName("status")      val status: String,
    @SerializedName("date")        val date: String
)

data class SellerStatsResponse(
    @SerializedName("totalClients")    val totalClients: Int,
    @SerializedName("todayOrders")     val todayOrders: Int,
    @SerializedName("pendingOrders")   val pendingOrders: Int,
    @SerializedName("preparingOrders") val preparingOrders: Int,
    @SerializedName("onWayOrders")     val onWayOrders: Int,
    @SerializedName("deliveredOrders") val deliveredOrders: Int,
    @SerializedName("recentOrders")    val recentOrders: List<SellerOrderResponse>,
    @SerializedName("clients")         val clients: List<SellerClientResponse>
)

data class CreateSellerClientRequest(
    @SerializedName("name")    val name: String,
    @SerializedName("phone")   val phone: String,
    @SerializedName("address") val address: String
)

data class CreateSellerClientResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("client")  val client: SellerClientResponse? = null
)

data class DeleteResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)



