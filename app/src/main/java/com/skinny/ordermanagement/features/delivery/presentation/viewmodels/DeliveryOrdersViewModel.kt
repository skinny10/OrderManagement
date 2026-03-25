package com.skinny.ordermanagement.features.delivery.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeliveryOrderUi(
    val id: Int,
    val clientName: String,
    val address: String,
    val total: Double,
    val status: String,
    val date: String
)

data class DeliveryOrdersUiState(
    val orders: List<DeliveryOrderUi> = emptyList(),
    val isLoading: Boolean = false
)


private val deliveryOrdersRepo = mutableListOf(
    DeliveryOrderUi(1, "Juan Pérez",  "Centro", 185.0, "Pendiente", "12 mar, 10:30 a.m."),
    DeliveryOrderUi(2, "María López", "Terán",  240.0, "En camino",  "12 mar, 11:00 a.m."),
    DeliveryOrderUi(3, "Pedro Gomez", "Norte",  155.0, "Entregado",  "11 mar, 02:20 p.m.")
)

@HiltViewModel
class DeliveryOrdersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryOrdersUiState())
    val uiState: StateFlow<DeliveryOrdersUiState> = _uiState.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = DeliveryOrdersUiState(orders = deliveryOrdersRepo.toList())
        }
    }

    fun updateOrderStatus(orderId: Int, newStatus: String) {
        viewModelScope.launch {
            val index = deliveryOrdersRepo.indexOfFirst { it.id == orderId }
            if (index >= 0) {
                deliveryOrdersRepo[index] = deliveryOrdersRepo[index].copy(status = newStatus)
            }
            _uiState.value = DeliveryOrdersUiState(orders = deliveryOrdersRepo.toList())
        }
    }
}