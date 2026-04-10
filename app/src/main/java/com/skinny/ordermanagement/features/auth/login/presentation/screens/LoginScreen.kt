package com.skinny.ordermanagement.features.auth.login.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.auth.login.presentation.components.LoginTextField
import com.skinny.ordermanagement.features.auth.login.presentation.viewmodels.LoginUiState
import com.skinny.ordermanagement.features.auth.login.presentation.viewmodels.LoginViewModel

val BluePrimaryLogin    = Color(0xFF1565C0)
val BlueLightLogin      = Color(0xFF42A5F5)
val BlueAccentLogin     = Color(0xFF0288D1)
val BlueBackgroundLogin = Color(0xFFF0F6FF)
val WhiteCardLogin      = Color(0xFFFFFFFF)
val GrayTextLogin       = Color(0xFF607D8B)

@Composable
fun LoginScreen(
    onLoginSuccess: (role: String?) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState  by viewModel.uiState.collectAsState()
    val email    by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                val role = (uiState as LoginUiState.Success).role
                snackbarHostState.showSnackbar("¡Bienvenido!")
                onLoginSuccess(role)
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(
                    (uiState as LoginUiState.Error).message
                )
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackgroundLogin)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BluePrimaryLogin, BlueLightLogin)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Login",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BluePrimaryLogin,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Gestión de Pedidos",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = BlueAccentLogin,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(WhiteCardLogin)
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    LoginTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Correo electrónico",
                        keyboardType = KeyboardType.Email,
                        accentColor = BluePrimaryLogin
                    )

                    LoginTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        accentColor = BluePrimaryLogin
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (uiState is LoginUiState.Loading)
                                    Brush.linearGradient(listOf(GrayTextLogin, GrayTextLogin))
                                else
                                    Brush.linearGradient(
                                        colors = listOf(BluePrimaryLogin, BlueLightLogin)
                                    )
                            )
                            .clickable(enabled = uiState !is LoginUiState.Loading) {
                                viewModel.onLogin()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState is LoginUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(26.dp),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                text = "Iniciar sesión",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    color = GrayTextLogin,
                    fontSize = 14.sp
                )
                Text(
                    text = "Regístrate",
                    color = BluePrimaryLogin,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = BluePrimaryLogin,
                contentColor = Color.White
            )
        }
    }
}

