package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerClientsUiState(
    val clients: List<ClientUi> = emptyList(),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class SellerClientsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SellerClientsUiState())
    val uiState: StateFlow<SellerClientsUiState> = _uiState.asStateFlow()

    init { loadClients() }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = SellerClientsUiState(clients = clientsRepository.toList())
        }
    }

    fun addClient(name: String, phone: String, address: String) {
        viewModelScope.launch {
            val nuevo = ClientUi(nextClientId++, name.trim(), phone.trim(), address.trim())
            clientsRepository.add(nuevo)
            _uiState.value = _uiState.value.copy(
                clients     = clientsRepository.toList(),
                saveSuccess = true
            )
        }
    }

    fun deleteClient(clientId: Int) {
        viewModelScope.launch {
            clientsRepository.removeAll { it.id == clientId }
            _uiState.value = _uiState.value.copy(clients = clientsRepository.toList())
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}