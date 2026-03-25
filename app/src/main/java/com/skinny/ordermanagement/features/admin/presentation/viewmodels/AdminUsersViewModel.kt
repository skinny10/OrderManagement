package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUsersUiState(
    val users: List<AdminUserUi> = emptyList(),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class AdminUsersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUsersUiState())
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    init { loadUsers() }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = AdminUsersUiState(users = adminUsersRepo.toList())
        }
    }

    fun addUser(name: String, role: String, email: String) {
        viewModelScope.launch {
            val nuevo = AdminUserUi(nextAdminUserId++, name.trim(), role, email.trim(), 0)
            adminUsersRepo.add(nuevo)
            _uiState.value = _uiState.value.copy(
                users = adminUsersRepo.toList(),
                saveSuccess = true
            )
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            adminUsersRepo.removeAll { it.id == userId }
            _uiState.value = _uiState.value.copy(users = adminUsersRepo.toList())
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}