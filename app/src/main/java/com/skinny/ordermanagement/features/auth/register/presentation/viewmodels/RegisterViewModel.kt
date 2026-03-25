package com.skinny.ordermanagement.features.auth.register.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.features.auth.register.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    fun onRegister() {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val result = registerUseCase(
                name = _name.value,
                lastName = _lastName.value,
                email = _email.value,
                password = _password.value
            )

            result.fold(
                onSuccess = { response ->
                    _uiState.value = RegisterUiState.Success(response.message)
                },
                onFailure = { exception ->
                    _uiState.value = RegisterUiState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}