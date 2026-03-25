package com.skinny.ordermanagement.features.admin.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminDashboardViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminDashboardUiState
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminOrderUi

val AdminBlue = Color(0xFF5C6BC0)

fun adminStatusColor(status: String): Color = when (status) {
    "Pendiente"  -> Color(0xFFF59E0B)
    "Preparando" -> Color(0xFF3B82F6)
    "En camino"  -> Color(0xFF8B5CF6)
    "Entregado"  -> Color(0xFF10B981)
    else         -> Color.Gray
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) { viewModel.loadDashboard() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminBlue,
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
                Text("Bienvenido, Administrador", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Vista general del sistema", fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
            }


            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(
                        modifier  = Modifier.weight(1f),
                        title     = "Pedidos",
                        value     = uiState.totalOrders.toString(),
                        subtitle  = "Total",
                        icon      = Icons.Default.ShoppingCart,
                        onClick   = { viewModel.onCategoryClick("total") }
                    )
                    AdminStatCard(
                        modifier  = Modifier.weight(1f),
                        title     = "Clientes",
                        value     = uiState.totalClients.toString(),
                        subtitle  = "Registrados",
                        icon      = Icons.Default.Person,
                        onClick   = onNavigateToClients
                    )
                }
            }


            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(
                        modifier  = Modifier.weight(1f),
                        title     = "Vendedores",
                        value     = uiState.totalSellers.toString(),
                        subtitle  = "Activos",
                        icon      = Icons.Default.Store,
                        onClick   = onNavigateToUsers
                    )
                    AdminStatCard(
                        modifier  = Modifier.weight(1f),
                        title     = "Repartidores",
                        value     = uiState.totalDelivery.toString(),
                        subtitle  = "Activos",
                        icon      = Icons.Default.DeliveryDining,
                        onClick   = onNavigateToUsers
                    )
                }
            }


            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Ingresos Totales", fontSize = 13.sp, color = Color.Gray)
                            Text(
                                "\$${uiState.totalRevenue.toInt()}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                            Text("De pedidos entregados", fontSize = 12.sp, color = Color.Gray)
                        }
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }


            item {
                Text("Estado de Pedidos", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatusCard(Modifier.weight(1f), "Pendientes",  uiState.pendingOrders,
                        Color(0xFFF59E0B)) { viewModel.onCategoryClick("Pendiente") }
                    AdminStatusCard(Modifier.weight(1f), "Preparando",  uiState.preparingOrders,
                        Color(0xFF3B82F6)) { viewModel.onCategoryClick("Preparando") }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatusCard(Modifier.weight(1f), "En Camino",  uiState.onWayOrders,
                        Color(0xFF8B5CF6)) { viewModel.onCategoryClick("En camino") }
                    AdminStatusCard(Modifier.weight(1f), "Entregados", uiState.deliveredOrders,
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
                    TextButton(onClick = onNavigateToOrders) {
                        Text("Ver Todos", color = AdminBlue)
                    }
                }
            }

            items(uiState.recentOrders) { order ->
                AdminOrderCard(order = order, onClick = {})
            }

            // Acciones Rápidas
            item {
                Spacer(Modifier.height(4.dp))
                Text("Acciones Rápidas", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onNavigateToOrders,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AdminBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Pedidos") }
                    Button(
                        onClick = onNavigateToUsers,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AdminBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Usuarios") }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onNavigateToClients,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Gestionar Clientes") }
            }
        }
    }

    if (uiState.showSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeSheet() },
            sheetState = sheetState
        ) {
            AdminCategorySheet(uiState = uiState, onDismiss = { viewModel.closeSheet() })
        }
    }
}

@Composable
fun AdminCategorySheet(uiState: AdminDashboardUiState, onDismiss: () -> Unit) {
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
            Text(uiState.sheetTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Surface(color = AdminBlue.copy(0.1f), shape = RoundedCornerShape(20.dp)) {
                Text(
                    "${uiState.sheetCount} total",
                    Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = AdminBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (uiState.sheetOrders.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                Text("No hay pedidos en esta categoría", color = Color.Gray)
            }
        } else {
            uiState.sheetOrders.forEach { order ->
                AdminOrderCard(order = order, onClick = {})
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun AdminStatCard(
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
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 13.sp, color = Color.Gray)
                Icon(icon, null, tint = AdminBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AdminBlue)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AdminStatusCard(
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
fun AdminOrderCard(order: AdminOrderUi, onClick: () -> Unit) {
    val color = adminStatusColor(order.status)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pedido #${order.id}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)) {
                    Text(
                        order.status,
                        Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(order.clientName, fontSize = 13.sp, color = Color.Gray)
                    Text("Vendedor: ${order.sellerName}", fontSize = 12.sp, color = Color.LightGray)
                    Text("Repartidor: ${order.deliveryName}", fontSize = 12.sp, color = Color.LightGray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("\$${order.total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(order.date, fontSize = 11.sp, color = Color.LightGray)
                }
            }
        }
    }
}