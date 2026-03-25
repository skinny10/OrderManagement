package com.skinny.ordermanagement.features.admin.data.datasource.model

import com.google.gson.annotations.SerializedName

data class AdminDashboardResponse(
    @SerializedName("totalOrders")     val totalOrders: Int,
    @SerializedName("totalClients")    val totalClients: Int,
    @SerializedName("totalSellers")    val totalSellers: Int,
    @SerializedName("totalDelivery")   val totalDelivery: Int,
    @SerializedName("pendingOrders")   val pendingOrders: Int,
    @SerializedName("preparingOrders") val preparingOrders: Int,
    @SerializedName("onWayOrders")     val onWayOrders: Int,
    @SerializedName("deliveredOrders") val deliveredOrders: Int,
    @SerializedName("totalRevenue")    val totalRevenue: Double,
    @SerializedName("recentOrders")    val recentOrders: List<AdminOrderResponse>
)