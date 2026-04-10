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
import com.google.firebase.messaging.FirebaseMessaging
import com.skinny.ordermanagement.features.auth.login.domain.repositories.LoginRepository
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import com.skinny.ordermanagement.core.work.SyncWorker
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    private val loginRepository: LoginRepository,
    @ApplicationContext private val context: Context

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

                    
                    FirebaseMessaging.getInstance().token.addOnSuccessListener { fcmToken ->
                        tokenManager.saveFcmToken(fcmToken)

                        
                        viewModelScope.launch {
                            response.token?.let { authToken ->
                                loginRepository.updateFcmToken(
                                    token = authToken,
                                    fcmToken = fcmToken
                                )
                            }
                        }
                    }

                    _uiState.value = LoginUiState.Success(
                        message = response.message,
                        role = response.role
                    )

                    // Programar sincronización en segundo plano
                    scheduleSyncWork()
                },
                onFailure = { exception ->
                    _uiState.value = LoginUiState.Error(
                        exception.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    private fun scheduleSyncWork() {
        val workManager = WorkManager.getInstance(context)
        val syncWorkRequest = SyncWorker.createWorkRequest()
        workManager.enqueueUniquePeriodicWork(
            "sync_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
