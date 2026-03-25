package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminOrdersUiState(
    val orders: List<AdminOrderUi> = emptyList(),
    val filteredOrders: List<AdminOrderUi> = emptyList(),
    val selectedFilter: String = "Todos",
    val isLoading: Boolean = false
)

@HiltViewModel
class AdminOrdersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdminOrdersUiState())
    val uiState: StateFlow<AdminOrdersUiState> = _uiState.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            val orders = adminOrdersRepo.toList().reversed()
            _uiState.value = AdminOrdersUiState(orders = orders, filteredOrders = orders)
        }
    }

    fun filterByStatus(status: String) {
        val all = adminOrdersRepo.toList().reversed()
        val filtered = if (status == "Todos") all else all.filter { it.status == status }
        _uiState.value = _uiState.value.copy(
            filteredOrders = filtered,
            selectedFilter = status
        )
    }

    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            adminOrdersRepo.removeAll { it.id == orderId }
            loadOrders()
        }
    }
}