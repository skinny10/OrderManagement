package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.skinny.ordermanagement.features.admin.domain.usecases.GetAdminClientsUseCase
import javax.inject.Inject

data class AdminClientsUiState(
    val clients: List<AdminClientUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AdminClientsViewModel @Inject constructor(
    private val getClientsUseCase: GetAdminClientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminClientsUiState())
    val uiState: StateFlow<AdminClientsUiState> = _uiState.asStateFlow()

    init { loadClients() }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getClientsUseCase().onSuccess { clients ->
                val clientUis = clients.map { client ->
                    AdminClientUi(
                        id = client.id ?: "",
                        name = client.name.ifBlank { "Sin nombre" },
                        phone = client.phone.ifBlank { "" },
                        address = client.address.ifBlank { "" },
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
}

