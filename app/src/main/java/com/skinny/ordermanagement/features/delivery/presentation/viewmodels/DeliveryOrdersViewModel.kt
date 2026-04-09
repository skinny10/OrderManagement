package com.skinny.ordermanagement.features.delivery.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.skinny.ordermanagement.features.delivery.domain.usecases.GetDeliveryOrdersUseCase
import com.skinny.ordermanagement.features.delivery.domain.usecases.UpdateDeliveryOrderStatusUseCase
import javax.inject.Inject

data class DeliveryOrderUi(
    val id: String,
    val clientName: String,
    val address: String,
    val total: Double,
    val status: String,
    val date: String
)

data class DeliveryOrdersUiState(
    val orders: List<DeliveryOrderUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DeliveryOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetDeliveryOrdersUseCase,
    private val updateStatusUseCase: UpdateDeliveryOrderStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryOrdersUiState())
    val uiState: StateFlow<DeliveryOrdersUiState> = _uiState.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getOrdersUseCase().onSuccess { orders ->
                val orderUis = orders.map { order ->
                    DeliveryOrderUi(
                        id         = order.id,
                        clientName = order.clientName,
                        address    = order.address,
                        total      = order.total,
                        status     = order.status,
                        date       = order.date
                    )
                }
                _uiState.value = DeliveryOrdersUiState(
                    orders    = orderUis,
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

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            updateStatusUseCase(orderId, newStatus).onSuccess {
                loadOrders()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }
}