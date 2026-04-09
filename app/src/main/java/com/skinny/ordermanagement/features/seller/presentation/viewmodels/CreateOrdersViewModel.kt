package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import com.skinny.ordermanagement.features.seller.domain.usecases.GetSellerClientsUseCase
import com.skinny.ordermanagement.features.seller.domain.usecases.CreateOrderUseCase

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
class CreateOrderViewModel @Inject constructor(
    private val getClientsUseCase: GetSellerClientsUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    init {
        loadClients()
        _uiState.value = _uiState.value.copy(products = catalogProducts)
    }

    fun loadClients() {
        viewModelScope.launch {
            getClientsUseCase().onSuccess { clients ->
                val clientUis = clients.map { client ->
                    ClientUi(
                        id = client.id,
                        name = client.name,
                        phone = client.phone,
                        address = client.address
                    )
                }
                _uiState.value = _uiState.value.copy(clients = clientUis)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
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
                _uiState.value = state.copy(error = "Selecciona un cliente")
                return@launch
            }
            if (state.cartItems.isEmpty()) {
                _uiState.value = state.copy(error = "Agrega al menos un producto")
                return@launch
            }

            // ✅ Llama al API con el id del cliente y el total
            createOrderUseCase(
                clientId = state.selectedClient.id,
                total    = state.total
            ).onSuccess {
                _uiState.value = state.copy(orderCreated = true, error = null)
            }.onFailure { error ->
                _uiState.value = state.copy(error = error.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun calcTotal(cart: List<CartItem>) = cart.sumOf { it.product.price * it.quantity }
}