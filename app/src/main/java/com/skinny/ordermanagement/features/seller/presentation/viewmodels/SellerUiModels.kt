package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import java.util.UUID

data class RecentOrderUi(
    val id: String,
    val clientName: String,
    val total: Double,
    val status: String,
    val date: String,
    val products: List<OrderProductUi> = emptyList()
)

data class OrderProductUi(
    val name: String,
    val quantity: Int,
    val price: Double
)

data class ClientUi(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val totalOrders: Int = 0
)

data class ProductUi(
    val id: Int,
    val name: String,
    val price: Double
)

val clientsRepository = mutableListOf<ClientUi>()
var nextClientId = 1

val ordersRepository = mutableListOf(
    RecentOrderUi(UUID.randomUUID().toString(), "Juan Pérez", 185.0, "Pendiente", "12 mar, 10:30 a.m.", listOf(OrderProductUi("Hamburguesa", 1, 80.0), OrderProductUi("Refresco", 1, 25.0))),
    RecentOrderUi(UUID.randomUUID().toString(), "María López", 240.0, "En camino", "12 mar, 11:00 a.m.", listOf(OrderProductUi("Pizza", 2, 120.0))),
    RecentOrderUi(UUID.randomUUID().toString(), "Pedro Gomez", 155.0, "Entregado", "11 mar, 02:20 p.m.", listOf(OrderProductUi("Pizza", 1, 120.0), OrderProductUi("Refresco", 1, 25.0)))
)

val catalogProducts = listOf(
    ProductUi(1, "Hamburguesa", 80.0),
    ProductUi(2, "Pizza", 120.0),
    ProductUi(3, "Refresco", 25.0),
    ProductUi(4, "Ensalada", 65.0),
    ProductUi(5, "Agua", 15.0)
)



