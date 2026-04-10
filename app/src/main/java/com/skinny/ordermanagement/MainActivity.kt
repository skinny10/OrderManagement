package com.skinny.ordermanagement

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.skinny.ordermanagement.core.security.TokenManager
import com.skinny.ordermanagement.features.auth.login.presentation.screens.LoginScreen
import com.skinny.ordermanagement.features.auth.register.presentation.screens.RegisterScreen
import com.skinny.ordermanagement.navigation.AdminNavGraph
import com.skinny.ordermanagement.navigation.DeliveryNavGraph
import com.skinny.ordermanagement.navigation.SellerNavGraph
import com.skinny.ordermanagement.ui.theme.OrderManagementTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            android.util.Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            android.util.Log.d("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        solicitarPermisoNotificaciones()
        tokenManager.clearSession()
        setContent {
            OrderManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(tokenManager.isLoggedIn()) }
                    if (isLoggedIn) {
                        val role = tokenManager.getRole()
                        when (role) {
                            "admin" -> AdminNavGraph(onLogout = {
                                tokenManager.clearSession()
                                isLoggedIn = false
                            })
                            "vendor" -> SellerNavGraph(onLogout = {
                                tokenManager.clearSession()
                                isLoggedIn = false
                            })
                            "delivery" -> DeliveryNavGraph(onLogout = {
                                tokenManager.clearSession()
                                isLoggedIn = false
                            })
                            else -> AdminNavGraph(onLogout = {
                                tokenManager.clearSession()
                                isLoggedIn = false
                            })
                        }
                    } else {
                        var currentScreen by remember { mutableStateOf("login") }
                        when (currentScreen) {
                            "login" -> LoginScreen(
                                onLoginSuccess = { role ->
                                    isLoggedIn = true
                                },
                                onNavigateToRegister = {
                                    currentScreen = "register"
                                }
                            )
                            "register" -> RegisterScreen(
                                onRegisterSuccess = {
                                    currentScreen = "login"
                                },
                                onNavigateToLogin = {
                                    currentScreen = "login"
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    android.util.Log.d("FCM", "Permiso ya concedido")
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

