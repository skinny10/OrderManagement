package com.skinny.ordermanagement.features.delivery.domain.entities

data class DeliveryOrder(
    val id: String,
    val clientName: String,
    val address: String,
    val total: Double,
    val status: String,
    val date: String
)

data class DeliveryStats(
    val totalOrders: Int,
    val pendingOrders: Int,
    val onWayOrders: Int,
    val deliveredOrders: Int,
    val recentOrders: List<DeliveryOrder>
)

