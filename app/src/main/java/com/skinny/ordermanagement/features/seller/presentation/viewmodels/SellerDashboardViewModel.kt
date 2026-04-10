package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.features.seller.domain.usecases.GetSellerStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerDashboardUiState(
    val userName          : String = "",
    val totalClients      : Int = 0,
    val todayOrders       : Int = 0,
    val pendingOrders     : Int = 0,
    val preparingOrders   : Int = 0,
    val onWayOrders       : Int = 0,
    val deliveredOrders   : Int = 0,
    val recentOrders      : List<RecentOrderUi> = emptyList(),
    val isLoading         : Boolean = false,
    val error             : String? = null,
    val showCategorySheet : Boolean = false,
    val categoryTitle     : String = "",
    val categoryOrders    : List<RecentOrderUi> = emptyList(),
    val categoryCount     : Int = 0,
    val categoryIsClients : Boolean = false,
    val clients           : List<ClientUi> = emptyList()
)

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val getStatsUseCase: GetSellerStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerDashboardUiState())
    val uiState: StateFlow<SellerDashboardUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getStatsUseCase().onSuccess { stats ->
                Log.d("SellerDashboard", "Stats received: recentOrders size ${stats.recentOrders.size}")
                val clientsUi = stats.clients.map { client ->
                    ClientUi(
                        id          = client.id,
                        name        = client.name,
                        phone       = client.phone,
                        address     = client.address,
                        totalOrders = client.totalOrders
                    )
                }
                
                val recentOrdersUi = stats.recentOrders.map { order ->
                    RecentOrderUi(
                        id        = order.id,
                        clientName = order.clientName,
                        total     = order.total,
                        status    = order.status,
                        date      = order.date
                    )
                }

                _uiState.value = _uiState.value.copy(
                    totalClients    = stats.totalClients,
                    todayOrders     = stats.todayOrders,
                    pendingOrders   = stats.pendingOrders,
                    preparingOrders = stats.preparingOrders,
                    onWayOrders     = stats.onWayOrders,
                    deliveredOrders = stats.deliveredOrders,
                    recentOrders    = recentOrdersUi,
                    clients         = clientsUi,
                    isLoading       = false
                )
            }.onFailure { error ->
                Log.e("SellerDashboard", "Error loading stats: ${error.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun onCategoryClick(key: String) {
        val orders = _uiState.value.recentOrders
        val isClients = key == "clientes"
        val (title, filtered) = when (key) {
            "clientes"   -> "Clientes Registrados"   to emptyList()
            "hoy"        -> "Pedidos de Hoy"          to orders
            "Pendiente"  -> "Pedidos Pendientes"      to orders.filter { it.status == "Pendiente" }
            "Preparando" -> "Pedidos en Preparación"  to orders.filter { it.status == "Preparando" }
            "En camino"  -> "Pedidos en Camino"       to orders.filter { it.status == "En camino" }
            "Entregado"  -> "Pedidos Entregados"      to orders.filter { it.status == "Entregado" }
            else         -> ""                         to emptyList()
        }
        _uiState.value = _uiState.value.copy(
            showCategorySheet = true,
            categoryTitle     = title,
            categoryOrders    = filtered,
            categoryCount     = if (isClients) _uiState.value.clients.size else filtered.size,
            categoryIsClients = isClients
        )
    }

    fun closeCategorySheet() {
        _uiState.value = _uiState.value.copy(showCategorySheet = false)
    }
}

