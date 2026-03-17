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

        // 👇 Login
        composable(route = AppRoutes.Auth.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        "admin"    -> AppRoutes.Admin.Home.route
                        "seller"   -> AppRoutes.Seller.Home.route
                        "delivery" -> AppRoutes.Delivery.Home.route
                        else       -> null
                    }
                    destination?.let {
                        navController.navigate(it) {
                            // 👇 Limpia backstack — no puede volver al Login
                            popUpTo(AppRoutes.Auth.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.Auth.Register.route)
                }
            )
        }

        // 👇 Register
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

        // 👇 Vistas por rol — pantallas temporales hasta tener las reales
        composable(route = AppRoutes.Admin.Home.route) {
            // TODO: Reemplazar con AdminHomeScreen()
        }

        composable(route = AppRoutes.Seller.Home.route) {
            // TODO: Reemplazar con SellerHomeScreen()
        }

        composable(route = AppRoutes.Delivery.Home.route) {
            // TODO: Reemplazar con DeliveryHomeScreen()
        }
    }
}