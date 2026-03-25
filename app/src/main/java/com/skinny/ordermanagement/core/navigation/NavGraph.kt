package com.skinny.ordermanagement.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skinny.ordermanagement.features.auth.login.presentation.screens.LoginScreen
import com.skinny.ordermanagement.features.auth.register.presentation.screens.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Auth.Login.route
    ) {

        composable(route = AppRoutes.Auth.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.Auth.Register.route)
                }
            )
        }

        composable(route = AppRoutes.Auth.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.Auth.Login.route) {
                        popUpTo(AppRoutes.Auth.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}