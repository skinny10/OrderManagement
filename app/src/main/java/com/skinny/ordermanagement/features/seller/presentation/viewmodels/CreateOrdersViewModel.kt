package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class CartItem(
    val product: ProductUi,
    val quantity: Int
)

data class CreateOrderUiState(
    val clients: List<ClientUi> = emptyList(),
    val products: List<ProductUi> = emptyList(),
    val selectedClient: ClientUi? = null,
    val cartItems: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val error: String? = null,
    val orderCreated: Boolean = false
)

@HiltViewModel
class CreateOrderViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = CreateOrderUiState(
            clients  = clientsRepository.toList(),
            products = catalogProducts
        )
    }

    fun selectClient(client: ClientUi) {
        _uiState.value = _uiState.value.copy(selectedClient = client, error = null)
    }

    fun addToCart(product: ProductUi) {
        val cart    = _uiState.value.cartItems.toMutableList()
        val index   = cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            cart[index] = cart[index].copy(quantity = cart[index].quantity + 1)
        } else {
            cart.add(CartItem(product, 1))
        }
        _uiState.value = _uiState.value.copy(cartItems = cart, total = calcTotal(cart))
    }

    fun removeFromCart(product: ProductUi) {
        val cart  = _uiState.value.cartItems.toMutableList()
        val index = cart.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            if (cart[index].quantity > 1) {
                cart[index] = cart[index].copy(quantity = cart[index].quantity - 1)
            } else {
                cart.removeAt(index)
            }
        }
        _uiState.value = _uiState.value.copy(cartItems = cart, total = calcTotal(cart))
    }

    fun createOrder() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedClient == null) {
                _uiState.value = state.copy(error = "Selecciona un cliente para continuar")
                return@launch
            }
            if (state.cartItems.isEmpty()) {
                _uiState.value = state.copy(error = "Agrega al menos un producto")
                return@launch
            }
            val sdf  = SimpleDateFormat("dd MMM, hh:mm a", Locale("es", "MX"))
            val date = sdf.format(Date())
            val newOrder = RecentOrderUi(
                id         = nextOrderId++,
                clientName = state.selectedClient.name,
                total      = state.total,
                status     = "Pendiente",
                date       = date,
                products   = state.cartItems.map {
                    OrderProductUi(it.product.name, it.quantity, it.product.price)
                }
            )
            ordersRepository.add(newOrder)
            _uiState.value = state.copy(orderCreated = true, error = null)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun calcTotal(cart: List<CartItem>) = cart.sumOf { it.product.price * it.quantity }
}