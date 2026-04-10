package com.skinny.ordermanagement.features.auth.login.presentation.viewmodels

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val message: String, val role: String?) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

