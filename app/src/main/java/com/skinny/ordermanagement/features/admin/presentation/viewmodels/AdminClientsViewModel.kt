package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminClientsUiState(
    val clients: List<AdminClientUi> = emptyList(),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class AdminClientsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdminClientsUiState())
    val uiState: StateFlow<AdminClientsUiState> = _uiState.asStateFlow()

    init { loadClients() }

    fun loadClients() {
        viewModelScope.launch {
            _uiState.value = AdminClientsUiState(clients = adminClientsRepo.toList())
        }
    }

    fun deleteClient(clientId: Int) {
        viewModelScope.launch {
            adminClientsRepo.removeAll { it.id == clientId }
            _uiState.value = _uiState.value.copy(clients = adminClientsRepo.toList())
        }
    }
}