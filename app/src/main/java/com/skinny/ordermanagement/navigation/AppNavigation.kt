package com.skinny.ordermanagement.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.skinny.ordermanagement.features.delivery.presentation.screens.*
import com.skinny.ordermanagement.features.seller.presentation.screens.*
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminDashboardScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminOrdersScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminClientsScreen
import com.skinny.ordermanagement.features.admin.presentation.screens.AdminUsersScreen
import kotlinx.coroutines.launch

private val NavPrimaryBlue = Color(0xFF5C6BC0)


private const val SELLER_DASHBOARD    = "seller_dashboard"
private const val SELLER_ORDERS       = "seller_orders"
private const val SELLER_CLIENTS      = "seller_clients"
private const val SELLER_CREATE_ORDER = "seller_create_order"
private const val SELLER_ORDER_DETAIL = "seller_order_detail/{orderId}"
private fun sellerOrderDetail(id: Int) = "seller_order_detail/$id"


private const val DELIVERY_DASHBOARD    = "delivery_dashboard"
private const val DELIVERY_ORDERS       = "delivery_orders"
private const val DELIVERY_ORDER_DETAIL = "delivery_order_detail/{orderId}"
private fun deliveryOrderDetail(id: Int) = "delivery_order_detail/$id"


private const val ADMIN_DASHBOARD = "admin_dashboard"
private const val ADMIN_ORDERS    = "admin_orders"
private const val ADMIN_CLIENTS   = "admin_clients"
private const val ADMIN_USERS     = "admin_users"

data class DrawerItem(val label: String, val icon: ImageVector, val route: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerNavGraph(onLogout: () -> Unit) {
    val navController  = rememberNavController()
    val drawerState    = rememberDrawerState(DrawerValue.Closed)
    val scope          = rememberCoroutineScope()
    val currentBack    by navController.currentBackStackEntryAsState()
    val currentRoute   = currentBack?.destination?.route ?: SELLER_DASHBOARD

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                userName     = "Ana Vendedora",
                role         = "Vendedor",
                currentRoute = currentRoute,
                items = listOf(
                    DrawerItem("Dashboard", Icons.Default.Dashboard,    SELLER_DASHBOARD),
                    DrawerItem("Clientes",  Icons.Default.Person,       SELLER_CLIENTS),
                    DrawerItem("Pedidos",   Icons.Default.ShoppingCart, SELLER_ORDERS)
                ),
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(SELLER_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                onLogout = onLogout
            )
        }
    ) {
        NavHost(navController = navController, startDestination = SELLER_DASHBOARD) {
            composable(SELLER_DASHBOARD) {
                SellerDashboardScreen(
                    onNavigateToOrders  = { navController.navigate(SELLER_ORDERS) },
                    onNavigateToClients = { navController.navigate(SELLER_CLIENTS) },
                    onCreateOrder       = { navController.navigate(SELLER_CREATE_ORDER) },
                    onOpenDrawer        = { scope.launch { drawerState.open() } }
                )
            }
            composable(SELLER_ORDERS) {
                SellerOrdersScreen(
                    onBack        = { navController.popBackStack() },
                    onOrderClick  = { id -> navController.navigate(sellerOrderDetail(id)) },
                    onCreateOrder = { navController.navigate(SELLER_CREATE_ORDER) }
                )
            }
            composable(SELLER_CREATE_ORDER) {
                CreateOrderScreen(
                    onBack         = { navController.popBackStack() },
                    onOrderCreated = {
                        navController.navigate(SELLER_ORDERS) {
                            popUpTo(SELLER_DASHBOARD) { inclusive = false }
                        }
                    }
                )
            }
            composable(
                route = SELLER_ORDER_DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.IntType })
            ) { back ->
                OrderDetailScreen(
                    orderId = back.arguments?.getInt("orderId") ?: 0,
                    onBack  = { navController.popBackStack() }
                )
            }
            composable(SELLER_CLIENTS) {
                SellerClientsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryNavGraph(onLogout: () -> Unit) {
    val navController  = rememberNavController()
    val drawerState    = rememberDrawerState(DrawerValue.Closed)
    val scope          = rememberCoroutineScope()
    val currentBack    by navController.currentBackStackEntryAsState()
    val currentRoute   = currentBack?.destination?.route ?: DELIVERY_DASHBOARD

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                userName     = "Luis Repartidor",
                role         = "Repartidor",
                currentRoute = currentRoute,
                items = listOf(
                    DrawerItem("Dashboard", Icons.Default.Dashboard,    DELIVERY_DASHBOARD),
                    DrawerItem("Pedidos",   Icons.Default.ShoppingCart, DELIVERY_ORDERS)
                ),
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(DELIVERY_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                onLogout = onLogout
            )
        }
    ) {
        NavHost(navController = navController, startDestination = DELIVERY_DASHBOARD) {
            composable(DELIVERY_DASHBOARD) {
                DeliveryDashboardScreen(
                    onNavigateToOrders = { navController.navigate(DELIVERY_ORDERS) },
                    onOpenDrawer       = { scope.launch { drawerState.open() } }
                )
            }
            composable(DELIVERY_ORDERS) {
                DeliveryOrdersScreen(
                    onBack       = { navController.popBackStack() },
                    onOrderClick = { id -> navController.navigate(deliveryOrderDetail(id)) }
                )
            }
            composable(
                route = DELIVERY_ORDER_DETAIL,
                arguments = listOf(navArgument("orderId") { type = NavType.IntType })
            ) { back ->
                DeliveryOrderDetailScreen(
                    orderId = back.arguments?.getInt("orderId") ?: 0,
                    onBack  = { navController.popBackStack() }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNavGraph(onLogout: () -> Unit) {
    val navController  = rememberNavController()
    val drawerState    = rememberDrawerState(DrawerValue.Closed)
    val scope          = rememberCoroutineScope()
    val currentBack    by navController.currentBackStackEntryAsState()
    val currentRoute   = currentBack?.destination?.route ?: ADMIN_DASHBOARD

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                userName     = "Administrador",
                role         = "Administrador",
                currentRoute = currentRoute,
                items = listOf(
                    DrawerItem("Dashboard", Icons.Default.Dashboard,    ADMIN_DASHBOARD),
                    DrawerItem("Pedidos",   Icons.Default.ShoppingCart, ADMIN_ORDERS),
                    DrawerItem("Clientes",  Icons.Default.Person,       ADMIN_CLIENTS),
                    DrawerItem("Usuarios",  Icons.Default.Group,        ADMIN_USERS)
                ),
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(ADMIN_DASHBOARD) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                onLogout = onLogout
            )
        }
    ) {
        NavHost(navController = navController, startDestination = ADMIN_DASHBOARD) {
            composable(ADMIN_DASHBOARD) {
                AdminDashboardScreen(
                    onNavigateToOrders  = { navController.navigate(ADMIN_ORDERS) },
                    onNavigateToClients = { navController.navigate(ADMIN_CLIENTS) },
                    onNavigateToUsers   = { navController.navigate(ADMIN_USERS) },
                    onOpenDrawer        = { scope.launch { drawerState.open() } }
                )
            }
            composable(ADMIN_ORDERS) {
                AdminOrdersScreen(onBack = { navController.popBackStack() })
            }
            composable(ADMIN_CLIENTS) {
                AdminClientsScreen(onBack = { navController.popBackStack() })
            }
            composable(ADMIN_USERS) {
                AdminUsersScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}



@Composable
fun AppDrawerContent(
    userName: String,
    role: String,
    currentRoute: String,
    items: List<DrawerItem>,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(NavPrimaryBlue)
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {
            Column {
                Text(
                    "Sistema de Pedidos",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        userName.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(userName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(role, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        items.forEach { item ->
            val selected = currentRoute == item.route
            val tint     = if (selected) NavPrimaryBlue else Color.DarkGray
            val bg       = if (selected) NavPrimaryBlue.copy(alpha = 0.1f) else Color.Transparent
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .background(bg, RoundedCornerShape(8.dp))
                    .clickable { onNavigate(item.route) }
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(item.icon, null, tint = tint, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(16.dp))
                Text(
                    item.label,
                    color = tint,
                    fontSize = 15.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }

        Spacer(Modifier.weight(1f))
        HorizontalDivider()
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onLogout() }
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ExitToApp, null, tint = Color.Gray, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text("Cerrar Sesión", color = Color.Gray, fontSize = 15.sp)
        }
    }
}