package com.skinny.ordermanagement.features.delivery.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.delivery.presentation.viewmodels.DeliveryOrdersViewModel

val DeliveryPrimaryBlue = Color(0xFF5C6BC0)

fun deliveryStatusColor(status: String): Color = when (status) {
    "Pendiente"  -> Color(0xFFF59E0B)
    "Preparando" -> Color(0xFF3B82F6)
    "En camino"  -> Color(0xFF8B5CF6)
    "Entregado"  -> Color(0xFF10B981)
    else         -> Color.Gray
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryDashboardScreen(
    onNavigateToOrders: () -> Unit,
    onOpenDrawer: () -> Unit,
    viewModel: DeliveryOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Recargar cada vez que se entra al dashboard
    LaunchedEffect(Unit) { viewModel.loadOrders() }

    val pending   = uiState.orders.count { it.status == "Pendiente" }
    val onWay     = uiState.orders.count { it.status == "En camino" }
    val delivered = uiState.orders.count { it.status == "Entregado" }

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
                    containerColor = DeliveryPrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Bienvenido, Luis Repartidor", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Rol: Repartidor", fontSize = 14.sp, color = Color.Gray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DeliveryStatusCard(Modifier.weight(1f), "Pendientes", pending,   Color(0xFFF59E0B))
                DeliveryStatusCard(Modifier.weight(1f), "En Camino",  onWay,     Color(0xFF8B5CF6))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DeliveryStatusCard(Modifier.weight(1f), "Entregados Hoy", delivered, Color(0xFF10B981))
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = onNavigateToOrders,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DeliveryPrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Ver Mis Pedidos", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun DeliveryStatusCard(modifier: Modifier, label: String, count: Int, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 13.sp, color = color, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(6.dp))
            Text(count.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}