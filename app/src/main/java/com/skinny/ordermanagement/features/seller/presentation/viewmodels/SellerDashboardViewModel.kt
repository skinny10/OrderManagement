package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerDashboardUiState(
    val userName: String = "Ana Vendedora",
    val totalClients: Int = 0,
    val todayOrders: Int = 0,
    val pendingOrders: Int = 0,
    val preparingOrders: Int = 0,
    val onWayOrders: Int = 0,
    val deliveredOrders: Int = 0,
    val recentOrders: List<RecentOrderUi> = emptyList(),
    val isLoading: Boolean = false,
    // Bottom sheet
    val showCategorySheet: Boolean = false,
    val categoryTitle: String = "",
    val categoryOrders: List<RecentOrderUi> = emptyList(),
    val categoryCount: Int = 0,
    val categoryIsClients: Boolean = false
)

@HiltViewModel
class SellerDashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SellerDashboardUiState())
    val uiState: StateFlow<SellerDashboardUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            val orders = ordersRepository.toList()
            _uiState.value = _uiState.value.copy(
                userName        = "Ana Vendedora",
                totalClients    = clientsRepository.size,
                todayOrders     = orders.size,
                pendingOrders   = orders.count { it.status == "Pendiente" },
                preparingOrders = orders.count { it.status == "Preparando" },
                onWayOrders     = orders.count { it.status == "En camino" },
                deliveredOrders = orders.count { it.status == "Entregado" },
                recentOrders    = orders.takeLast(3).reversed()
            )
        }
    }

    fun onCategoryClick(key: String) {
        val orders = ordersRepository.toList()
        val isClients = key == "clientes"
        val (title, filtered) = when (key) {
            "clientes"   -> "Clientes Registrados"  to emptyList()
            "hoy"        -> "Pedidos de Hoy"         to orders
            "Pendiente"  -> "Pedidos Pendientes"     to orders.filter { it.status == "Pendiente" }
            "Preparando" -> "Pedidos en Preparación" to orders.filter { it.status == "Preparando" }
            "En camino"  -> "Pedidos en Camino"      to orders.filter { it.status == "En camino" }
            "Entregado"  -> "Pedidos Entregados"     to orders.filter { it.status == "Entregado" }
            else         -> ""                        to emptyList()
        }
        _uiState.value = _uiState.value.copy(
            showCategorySheet = true,
            categoryTitle     = title,
            categoryOrders    = filtered,
            categoryCount     = if (isClients) clientsRepository.size else filtered.size,
            categoryIsClients = isClients
        )
    }

    fun closeCategorySheet() {
        _uiState.value = _uiState.value.copy(showCategorySheet = false)
    }
}