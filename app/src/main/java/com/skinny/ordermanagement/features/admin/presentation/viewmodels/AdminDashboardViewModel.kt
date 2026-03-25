package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardUiState(
    val totalOrders: Int = 0,
    val totalClients: Int = 0,
    val totalSellers: Int = 0,
    val totalDelivery: Int = 0,
    val pendingOrders: Int = 0,
    val preparingOrders: Int = 0,
    val onWayOrders: Int = 0,
    val deliveredOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val recentOrders: List<AdminOrderUi> = emptyList(),
    // Category sheet
    val showSheet: Boolean = false,
    val sheetTitle: String = "",
    val sheetOrders: List<AdminOrderUi> = emptyList(),
    val sheetCount: Int = 0
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            val orders  = adminOrdersRepo.toList()
            val revenue = orders.filter { it.status == "Entregado" }.sumOf { it.total }
            _uiState.value = AdminDashboardUiState(
                totalOrders     = orders.size,
                totalClients    = adminClientsRepo.size,
                totalSellers    = adminUsersRepo.count { it.role == "Vendedor" },
                totalDelivery   = adminUsersRepo.count { it.role == "Repartidor" },
                pendingOrders   = orders.count { it.status == "Pendiente" },
                preparingOrders = orders.count { it.status == "Preparando" },
                onWayOrders     = orders.count { it.status == "En camino" },
                deliveredOrders = orders.count { it.status == "Entregado" },
                totalRevenue    = revenue,
                recentOrders    = orders.takeLast(3).reversed()
            )
        }
    }

    fun onCategoryClick(key: String) {
        val orders = adminOrdersRepo.toList()
        val (title, filtered) = when (key) {
            "total"      -> "Todos los Pedidos"     to orders
            "Pendiente"  -> "Pedidos Pendientes"     to orders.filter { it.status == "Pendiente" }
            "Preparando" -> "Pedidos en Preparación" to orders.filter { it.status == "Preparando" }
            "En camino"  -> "Pedidos en Camino"      to orders.filter { it.status == "En camino" }
            "Entregado"  -> "Pedidos Entregados"     to orders.filter { it.status == "Entregado" }
            else         -> ""                        to emptyList()
        }
        _uiState.value = _uiState.value.copy(
            showSheet   = true,
            sheetTitle  = title,
            sheetOrders = filtered,
            sheetCount  = filtered.size
        )
    }

    fun closeSheet() { _uiState.value = _uiState.value.copy(showSheet = false) }
}