package com.skinny.ordermanagement.features.seller.presentation.viewmodels

data class RecentOrderUi(
    val id: Int,
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
    val id: Int,
    val name: String,
    val phone: String,
    val address: String
)

data class ProductUi(
    val id: Int,
    val name: String,
    val price: Double
)

// ── Repositorios en memoria (persisten mientras la app esté abierta) ──────────

val clientsRepository = mutableListOf(
    ClientUi(1, "Juan Pérez",  "9611234567", "Centro"),
    ClientUi(2, "María López", "9615552233", "Terán"),
    ClientUi(3, "Pedro Gomez", "9619874321", "Norte")
)
var nextClientId = 4

val ordersRepository = mutableListOf(
    RecentOrderUi(1, "Juan Pérez",  185.0, "Pendiente", "12 mar, 10:30 a.m.",
        listOf(OrderProductUi("Hamburguesa", 1, 80.0), OrderProductUi("Refresco", 1, 25.0))),
    RecentOrderUi(2, "María López", 240.0, "En camino",  "12 mar, 11:00 a.m.",
        listOf(OrderProductUi("Pizza", 2, 120.0))),
    RecentOrderUi(3, "Pedro Gomez", 155.0, "Entregado",  "11 mar, 02:20 p.m.",
        listOf(OrderProductUi("Pizza", 1, 120.0), OrderProductUi("Refresco", 1, 25.0)))
)
var nextOrderId = 4

val catalogProducts = listOf(
    ProductUi(1, "Hamburguesa", 80.0),
    ProductUi(2, "Pizza",       120.0),
    ProductUi(3, "Refresco",    25.0),
    ProductUi(4, "Ensalada",    65.0),
    ProductUi(5, "Agua",        15.0)
)