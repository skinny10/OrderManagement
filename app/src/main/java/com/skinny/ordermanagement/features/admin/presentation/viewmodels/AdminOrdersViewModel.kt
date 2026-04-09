package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.skinny.ordermanagement.features.admin.domain.usecases.GetAdminOrdersUseCase
import javax.inject.Inject

data class AdminOrdersUiState(
    val orders: List<AdminOrderUi> = emptyList(),
    val filteredOrders: List<AdminOrderUi> = emptyList(),
    val selectedFilter: String = "Todos",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetAdminOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminOrdersUiState())
    val uiState: StateFlow<AdminOrdersUiState> = _uiState.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getOrdersUseCase().onSuccess { orders ->
                val orderUis = orders.map { order ->
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
                _uiState.value = AdminOrdersUiState(
                    orders = orderUis,
                    filteredOrders = orderUis,
                    isLoading = false
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun filterOrders(status: String) {
        val filtered = if (status == "Todos") {
            _uiState.value.orders
        } else {
            _uiState.value.orders.filter { it.status == status }
        }
        _uiState.value = _uiState.value.copy(
            selectedFilter = status,
            filteredOrders = filtered
        )
    }
}