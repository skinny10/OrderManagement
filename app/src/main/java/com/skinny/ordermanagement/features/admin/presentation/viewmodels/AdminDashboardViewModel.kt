package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
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
    val sheetCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                adminRepository.getDashboard()
            }
            result.onSuccess { dashboard ->
                val recentOrders = dashboard.recentOrders.map { order ->
                    AdminOrderUi(
                        id = order.id,
                        clientName = order.clientName ?: "Cliente desconocido",
                        sellerName = order.sellerName,
                        deliveryName = order.deliveryName,
                        total = order.total,
                        status = when (order.status) {
                            "pending" -> "Pendiente"
                            "preparing" -> "Preparando"
                            "onWay" -> "En camino"
                            "delivered" -> "Entregado"
                            else -> order.status
                        },
                        date = order.date
                    )
                }
                _uiState.value = AdminDashboardUiState(
                    totalOrders = dashboard.totalOrders,
                    totalClients = dashboard.totalClients,
                    totalSellers = dashboard.totalSellers,
                    totalDelivery = dashboard.totalDelivery,
                    pendingOrders = dashboard.pendingOrders,
                    preparingOrders = dashboard.preparingOrders,
                    onWayOrders = dashboard.onWayOrders,
                    deliveredOrders = dashboard.deliveredOrders,
                    totalRevenue = dashboard.totalRevenue,
                    recentOrders = recentOrders,
                    isLoading = false,
                    error = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun onCategoryClick(key: String) {
        val orders = _uiState.value.recentOrders
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