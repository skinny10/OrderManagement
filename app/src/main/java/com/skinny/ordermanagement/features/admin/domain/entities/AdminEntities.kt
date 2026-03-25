package com.skinny.ordermanagement.features.admin.domain.entities

data class AdminUser(
    val id: String,
    val name: String,
    val role: String,
    val email: String,
    val activeOrders: Int
)

data class AdminOrder(
    val id: String,
    val clientName: String,
    val sellerName: String,
    val deliveryName: String,
    val total: Double,
    val status: String,
    val date: String
)

data class AdminClient(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val totalOrders: Int
)

data class AdminDashboard(
    val totalOrders: Int,
    val totalClients: Int,
    val totalSellers: Int,
    val totalDelivery: Int,
    val pendingOrders: Int,
    val preparingOrders: Int,
    val onWayOrders: Int,
    val deliveredOrders: Int,
    val totalRevenue: Double,
    val recentOrders: List<AdminOrder>
)