package com.skinny.ordermanagement.features.auth.login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.core.security.TokenManager
import com.skinny.ordermanagement.features.auth.login.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    fun onLogin() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = loginUseCase(
                email = _email.value,
                password = _password.value
            )

            result.fold(
                onSuccess = { response ->
                    response.token?.let { tokenManager.saveToken(it) }
                    response.role?.let { tokenManager.saveRole(it) }
                    _uiState.value = LoginUiState.Success(
                        message = response.message,
                        role = response.role
                    )
                },
                onFailure = { exception ->
                    _uiState.value = LoginUiState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}