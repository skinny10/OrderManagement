package com.skinny.ordermanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrderManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("login") }
                    when (currentScreen) {
                        "login" -> LoginScreen(
                            onLoginSuccess = { role ->
                                currentScreen = "logged_in"
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
                        "logged_in" -> {
                            val role = tokenManager.getRole()
                            when (role) {
                                "admin" -> AdminNavGraph(onLogout = {
                                    tokenManager.clearSession()
                                    currentScreen = "login"
                                })
                                "vendor" -> SellerNavGraph(onLogout = {
                                    tokenManager.clearSession()
                                    currentScreen = "login"
                                })
                                "delivery" -> DeliveryNavGraph(onLogout = {
                                    tokenManager.clearSession()
                                    currentScreen = "login"
                                })
                                else -> AdminNavGraph(onLogout = {
                                    tokenManager.clearSession()
                                    currentScreen = "login"
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
