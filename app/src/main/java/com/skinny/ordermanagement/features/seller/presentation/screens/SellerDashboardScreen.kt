package com.skinny.ordermanagement.features.seller.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.seller.presentation.viewmodels.*

val PrimaryBlue = Color(0xFF5C6BC0)

fun statusColor(status: String): Color = when (status) {
    "Pendiente"  -> Color(0xFFF59E0B)
    "Preparando" -> Color(0xFF3B82F6)
    "En camino"  -> Color(0xFF8B5CF6)
    "Entregado"  -> Color(0xFF10B981)
    else         -> Color.Gray
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToClients: () -> Unit,
    onCreateOrder: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: SellerDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Refrescar al entrar a la pantalla
    LaunchedEffect(Unit) { viewModel.loadDashboard() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sistema de Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Bienvenido, ${uiState.userName}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Rol: Vendedor", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(Modifier.weight(1f), "Clientes", uiState.totalClients.toString(),
                        "Registrados", Icons.Default.Person) { viewModel.onCategoryClick("clientes") }
                    StatCard(Modifier.weight(1f), "Hoy", uiState.todayOrders.toString(),
                        "pedidos", Icons.Default.ShoppingCart) { viewModel.onCategoryClick("hoy") }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusCard(Modifier.weight(1f), "Pendientes",  uiState.pendingOrders,
                        Color(0xFFF59E0B)) { viewModel.onCategoryClick("Pendiente") }
                    StatusCard(Modifier.weight(1f), "Preparando",  uiState.preparingOrders,
                        Color(0xFF3B82F6)) { viewModel.onCategoryClick("Preparando") }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusCard(Modifier.weight(1f), "En Camino",  uiState.onWayOrders,
                        Color(0xFF8B5CF6)) { viewModel.onCategoryClick("En camino") }
                    StatusCard(Modifier.weight(1f), "Entregados", uiState.deliveredOrders,
                        Color(0xFF10B981)) { viewModel.onCategoryClick("Entregado") }
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pedidos Recientes", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    TextButton(onClick = onNavigateToOrders) { Text("Ver Todos", color = PrimaryBlue) }
                }
            }

            items(uiState.recentOrders) { order ->
                OrderCard(order = order, onClick = {})
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text("Acciones Rápidas", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onCreateOrder,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Crear Pedido", modifier = Modifier.padding(vertical = 4.dp)) }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onNavigateToClients,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Gestionar Clientes") }
            }
        }
    }

    if (uiState.showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeCategorySheet() },
            sheetState = sheetState
        ) {
            CategorySheet(uiState = uiState, onDismiss = { viewModel.closeCategorySheet() })
        }
    }
}

@Composable
fun CategorySheet(
    uiState: com.skinny.ordermanagement.features.seller.presentation.viewmodels.SellerDashboardUiState,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(uiState.categoryTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Surface(color = PrimaryBlue.copy(0.1f), shape = RoundedCornerShape(20.dp)) {
                Text(
                    "${uiState.categoryCount} total",
                    Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = PrimaryBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        if (uiState.categoryIsClients) {
            uiState.clients.forEach { client ->
                Card(
                    Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            Modifier.size(38.dp), shape = RoundedCornerShape(50),
                            color = PrimaryBlue.copy(0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(client.name.first().toString(), color = PrimaryBlue,
                                    fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(client.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text(client.phone, fontSize = 12.sp, color = Color.Gray)
                            Text(client.address, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        } else if (uiState.categoryOrders.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                Text("No hay pedidos en esta categoría", color = Color.Gray)
            }
        } else {
            uiState.categoryOrders.forEach { order ->
                OrderCard(order = order, onClick = {})
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 13.sp, color = Color.Gray)
                Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    label: String,
    count: Int,
    color: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, fontSize = 13.sp, color = color, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(6.dp))
            Text(count.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun OrderCard(order: RecentOrderUi, onClick: () -> Unit) {
    val color = statusColor(order.status)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Pedido #${order.id}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(order.clientName, fontSize = 13.sp, color = Color.Gray)
                Text(order.date, fontSize = 12.sp, color = Color.LightGray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("\$${order.total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(4.dp))
                Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)) {
                    Text(
                        order.status,
                        Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}