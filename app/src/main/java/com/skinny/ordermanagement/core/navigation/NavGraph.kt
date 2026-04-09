package com.skinny.ordermanagement.core.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminClientsScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminDashboardScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminDrawer
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminOrdersScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminUsersScreen
import com.skinny.ordermanagement.features.auth.login.presentation.screens.LoginScreen
import com.skinny.ordermanagement.features.auth.register.presentation.screens.RegisterScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph(navController: NavHostController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    NavHost(
        navController  = navController,
        startDestination = AppRoutes.Auth.Login.route
    ) {

        // 👇 Login
        composable(route = AppRoutes.Auth.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        "admin"    -> AppRoutes.Admin.Dashboard.route
                        "seller"   -> AppRoutes.Seller.Home.route
                        "delivery" -> AppRoutes.Delivery.Home.route
                        else       -> null
                    }
                    destination?.let {
                        navController.navigate(it) {
                            popUpTo(AppRoutes.Auth.Login.route) { inclusive = true }
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
                        popUpTo(AppRoutes.Auth.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // 👇 Admin Dashboard — con Drawer
        composable(route = AppRoutes.Admin.Dashboard.route) {
            ModalNavigationDrawer(
                drawerState   = drawerState,
                drawerContent = {
                    AdminDrawer(
                        onNavigateToDashboard = {
                            navController.navigate(AppRoutes.Admin.Dashboard.route) {
                                popUpTo(AppRoutes.Admin.Dashboard.route) { inclusive = true }
                            }
                        },
                        onNavigateToOrders  = { navController.navigate(AppRoutes.Admin.Orders.route) },
                        onNavigateToClients = { navController.navigate(AppRoutes.Admin.Clients.route) },
                        onNavigateToUsers   = { navController.navigate(AppRoutes.Admin.Users.route) },
                        onLogout = {
                            navController.navigate(AppRoutes.Auth.Login.route) {
                                popUpTo(AppRoutes.Admin.Dashboard.route) { inclusive = true }
                            }
                        },
                        onCloseDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            ) {
                AdminDashboardScreen(
                    onNavigateToOrders  = { navController.navigate(AppRoutes.Admin.Orders.route) },
                    onNavigateToClients = { navController.navigate(AppRoutes.Admin.Clients.route) },
                    onNavigateToUsers   = { navController.navigate(AppRoutes.Admin.Users.route) },
                    onOpenDrawer        = { scope.launch { drawerState.open() } }
                )
            }
        }

        // 👇 Admin Orders
        composable(route = AppRoutes.Admin.Orders.route) {
            AdminOrdersScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 👇 Admin Users
        composable(route = AppRoutes.Admin.Users.route) {
            AdminUsersScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 👇 Admin Clients
        composable(route = AppRoutes.Admin.Clients.route) {
            AdminClientsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 👇 Seller y Delivery — pendientes
        composable(route = AppRoutes.Seller.Home.route) {
            // TODO: SellerHomeScreen()
        }

        composable(route = AppRoutes.Delivery.Home.route) {
            // TODO: DeliveryHomeScreen()
        }
    }
}