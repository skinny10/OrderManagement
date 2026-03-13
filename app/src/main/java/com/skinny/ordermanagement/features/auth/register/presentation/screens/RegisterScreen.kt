package com.skinny.ordermanagement.features.auth.register.presentation.screens

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
import androidx.compose.material.icons.filled.Person
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
import com.skinny.ordermanagement.features.auth.register.presentation.components.RegisterTextField
import com.skinny.ordermanagement.features.auth.register.presentation.viewmodels.RegisterUiState
import com.skinny.ordermanagement.features.auth.register.presentation.viewmodels.RegisterViewModel

// 🎨 Paleta de colores azul vivo
val BluePrimary    = Color(0xFF1565C0)
val BlueLight      = Color(0xFF42A5F5)
val BlueAccent     = Color(0xFF0288D1)
val BlueBackground = Color(0xFFF0F6FF)
val WhiteCard      = Color(0xFFFFFFFF)
val GrayText       = Color(0xFF607D8B)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState  by viewModel.uiState.collectAsState()
    val name     by viewModel.name.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email    by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                snackbarHostState.showSnackbar("¡Registro exitoso!")
                onRegisterSuccess()
                viewModel.resetState()
            }
            is RegisterUiState.Error -> {
                snackbarHostState.showSnackbar(
                    (uiState as RegisterUiState.Error).message
                )
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BluePrimary, BlueLight)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "Register",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BluePrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Gestión de Pedidos",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = BlueAccent,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(WhiteCard)
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    RegisterTextField(
                        value = name,
                        onValueChange = viewModel::onNameChange,
                        label = "Nombre",
                        accentColor = BluePrimary
                    )

                    RegisterTextField(
                        value = lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = "Apellido",
                        accentColor = BluePrimary
                    )

                    RegisterTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Correo electrónico",
                        keyboardType = KeyboardType.Email,
                        accentColor = BluePrimary
                    )

                    RegisterTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        accentColor = BluePrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (uiState is RegisterUiState.Loading)
                                    Brush.linearGradient(listOf(GrayText, GrayText))
                                else
                                    Brush.linearGradient(
                                        colors = listOf(BluePrimary, BlueLight)
                                    )
                            )
                            .clickable(enabled = uiState !is RegisterUiState.Loading) {
                                viewModel.onRegister()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState is RegisterUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(26.dp),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                text = "Crear cuenta",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

// dirigir al login
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = GrayText,
                    fontSize = 14.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = BluePrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = BluePrimary,
                contentColor = Color.White
            )
        }
    }
}