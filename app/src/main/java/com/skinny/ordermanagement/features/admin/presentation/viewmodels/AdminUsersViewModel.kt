package com.skinny.ordermanagement.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.features.admin.domain.usecases.CreateUserUseCase
import com.skinny.ordermanagement.features.admin.domain.usecases.DeleteUserUseCase
import com.skinny.ordermanagement.features.admin.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUsersUiState(
    val users       : List<AdminUserUi> = emptyList(),
    val isLoading   : Boolean = false,
    val saveSuccess : Boolean = false,
    val error       : String? = null
)

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
    private val getUsersUseCase  : GetUsersUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUsersUiState())
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    init { loadUsers() }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getUsersUseCase().fold(
                onSuccess = { users ->
                    _uiState.value = _uiState.value.copy(
                        users     = users.map { it.toUi() },
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error     = e.message
                    )
                }
            )
        }
    }

    fun addUser(name: String, lastName: String, role: String, email: String, password: String) {
        viewModelScope.launch {
            createUserUseCase(name, lastName, role, email, password).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(saveSuccess = true)
                    loadUsers()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            deleteUserUseCase(userId).fold(
                onSuccess = { loadUsers() },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
            )
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}

private fun com.skinny.ordermanagement.features.admin.domain.entities.AdminUser.toUi() = AdminUserUi(
    id           = id,
    name         = name,
    role         = when (role) {
        "vendor"   -> "Vendedor"
        "delivery" -> "Repartidor"
        else       -> role
    },
    email        = email,
    activeOrders = activeOrders
)