package com.skinny.ordermanagement.features.seller.domain.entities

data class SellerClient(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val totalOrders: Int = 0
)

data class SellerOrder(
    val id: String,
    val clientName: String,
    val address: String,
    val total: Double,
    val status: String,
    val date: String
)

data class SellerStats(
    val totalClients: Int,
    val todayOrders: Int,
    val pendingOrders: Int,
    val preparingOrders: Int,
    val onWayOrders: Int,
    val deliveredOrders: Int,
    val recentOrders: List<SellerOrder>,
    val clients: List<SellerClient>
)



