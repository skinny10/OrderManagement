package com.skinny.ordermanagement.features.admin.presentation.viewmodels



data class AdminOrderUi(
    val id: String,
    val clientName: String,
    val sellerName: String,
    val deliveryName: String,
    val total: Double,
    val status: String,
    val date: String
)

data class AdminClientUi(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val totalOrders: Int
)

data class AdminUserUi(
    val id: String,
    val name: String,
    val role: String,   
    val email: String,
    val activeOrders: Int,
    val password: String = ""
)



val adminOrdersRepo = mutableListOf(
    AdminOrderUi("1", "Juan Pérez",   "Ana Vendedora",  "Luis Repartidor", 185.0, "Pendiente", "12 mar, 10:30 a.m."),
    AdminOrderUi("2", "María López",  "Ana Vendedora",  "Luis Repartidor", 240.0, "En camino",  "12 mar, 11:00 a.m."),
    AdminOrderUi("3", "Pedro Gomez",  "Ana Vendedora",  "Luis Repartidor", 155.0, "Entregado",  "11 mar, 02:20 p.m.")
)

val adminClientsRepo = mutableListOf(
    AdminClientUi("1", "Juan Pérez",  "9611234567", "Centro", 2),
    AdminClientUi("2", "María López", "9615552233", "Terán",  3),
    AdminClientUi("3", "Pedro Gomez", "9619874321", "Norte",  1)
)

val adminUsersRepo = mutableListOf(
    AdminUserUi("1", "Ana Vendedora",   "Vendedor",    "ana@mail.com",   3, "password123"),
    AdminUserUi("2", "Luis Repartidor", "Repartidor",  "luis@mail.com",  2, "password123")
)
var nextAdminUserId = 3

