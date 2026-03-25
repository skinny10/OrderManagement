package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

data class AdminClientsUiState(
    val clients: List<AdminClientUi> = emptyList(),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminClientsViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminClientsUiState())
    val uiState: StateFlow<AdminClientsUiState> = _uiState.asStateFlow()

    init { loadClients() }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            adminRepository.getClients().onSuccess { clients ->
                val clientUis = clients.map { client ->
                    AdminClientUi(
                        id = client.id,
                        name = client.name,
                        phone = client.phone,
                        address = client.address,
                        totalOrders = client.totalOrders
                    )
                }
                _uiState.value = AdminClientsUiState(
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

    fun deleteClient(clientId: String) {
        viewModelScope.launch {
            adminRepository.deleteClient(clientId).onSuccess {
                loadClients()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }
}