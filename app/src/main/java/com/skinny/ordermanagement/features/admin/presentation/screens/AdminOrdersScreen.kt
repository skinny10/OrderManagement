package com.skinny.ordermanagement.features.admin.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminOrderUi
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminOrdersViewModel
import com.skinny.ordermanagement.ui.theme.PrimaryBlue
import com.skinny.ordermanagement.ui.theme.getStatusColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(
    onBack: () -> Unit,
    viewModel: AdminOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filters = listOf("Todos", "Pendiente", "Preparando", "En camino", "Entregado")

    LaunchedEffect(Unit) { viewModel.loadOrders() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todos los Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
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
        Column(Modifier.fillMaxSize().padding(padding)) {

            
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val selected = uiState.selectedFilter == filter
                    FilterChip(
                        selected = selected,
                        onClick  = { viewModel.filterOrders(filter) },
                        label    = { Text(filter, fontSize = 13.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }

            if (uiState.filteredOrders.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay pedidos en esta categoría", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.filteredOrders, key = { it.id }) { order ->
                        AdminOrderCardWithDelete(
                            order    = order,
                            onDelete = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrderCardWithDelete(order: AdminOrderUi, onDelete: () -> Unit) {
    val color = getStatusColor(order.status)
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            order.status,
                            Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            fontSize = 12.sp, color = color, fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { showConfirm = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Delete, null,
                            tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(order.clientName, fontSize = 13.sp, color = Color.Gray)
                    Text("Vendedor: ${order.sellerName}", fontSize = 12.sp, color = Color.LightGray)
                    Text("Repartidor: ${order.deliveryName}", fontSize = 12.sp, color = Color.LightGray)
                    Text(order.date, fontSize = 11.sp, color = Color.LightGray)
                }
                Text("\$${order.total.toInt()}", fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, color = PrimaryBlue)
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Eliminar Pedido") },
            text  = { Text("¿Estás seguro de eliminar este pedido? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { showConfirm = false; onDelete() },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) { Text("Eliminar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}

