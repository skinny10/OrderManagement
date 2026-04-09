package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.features.seller.domain.usecases.GetSellerOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerOrdersUiState(
    val orders: List<RecentOrderUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SellerOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetSellerOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerOrdersUiState())
    val uiState: StateFlow<SellerOrdersUiState> = _uiState.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getOrdersUseCase().onSuccess { orders ->
                val ordersUi = orders.map { order ->
                    RecentOrderUi(
                        id        = order.id,
                        clientName = order.clientName,
                        total     = order.total,
                        status    = order.status,
                        date      = order.date
                    )
                }
                _uiState.value = _uiState.value.copy(
                    orders = ordersUi,
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
}