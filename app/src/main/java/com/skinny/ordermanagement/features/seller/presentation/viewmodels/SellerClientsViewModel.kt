package com.skinny.ordermanagement.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

data class SellerClientsUiState(
    val clients: List<ClientUi> = emptyList(),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SellerClientsViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerClientsUiState())
    val uiState: StateFlow<SellerClientsUiState> = _uiState.asStateFlow()

    init { loadClients() }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            adminRepository.getClients().onSuccess { clients ->
                val clientUis = clients.map { client ->
                    ClientUi(
                        id = client.id,
                        name = client.name,
                        phone = client.phone,
                        address = client.address
                    )
                }
                _uiState.value = SellerClientsUiState(
                    clients = clientUis,
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

    fun addClient(name: String, phone: String, address: String) {
        viewModelScope.launch {
            adminRepository.createClient(name, phone, address).onSuccess { client ->
                _uiState.value = _uiState.value.copy(saveSuccess = true)
                loadClients()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }

    fun deleteClient(clientId: String) {
        viewModelScope.launch {
            adminRepository.deleteClient(clientId).onSuccess {
                loadClients()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}