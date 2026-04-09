package com.skinny.ordermanagement.features.delivery.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.skinny.ordermanagement.features.delivery.presentation.viewmodels.DeliveryOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryOrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: DeliveryOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val order = uiState.orders.find { it.id == orderId }

    var selectedStatus by remember(order?.status) { mutableStateOf(order?.status ?: "Pendiente") }
    var savedStatus    by remember(order?.status) { mutableStateOf(order?.status ?: "Pendiente") }
    var expanded       by remember { mutableStateOf(false) }

    val statusChanged = selectedStatus != savedStatus
    val statusOptions = listOf("Pendiente", "Preparando", "En camino", "Entregado")

    LaunchedEffect(Unit) { viewModel.loadOrders() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedido #$orderId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    Surface(
                        color = deliveryStatusColor(savedStatus).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            savedStatus,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = deliveryStatusColor(savedStatus),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
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
        if (order == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DeliveryPrimaryBlue)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DeliverySectionCard("Información del Cliente") {
                    DeliveryInfoRow(Icons.Default.Person,     order.clientName)
                    DeliveryInfoRow(Icons.Default.Phone,      "9615552233")
                    DeliveryInfoRow(Icons.Default.LocationOn, order.address)
                }
            }

            item {
                DeliverySectionCard("Información del Pedido") {
                    DeliveryInfoRow(Icons.Default.DateRange, order.date)
                    Spacer(Modifier.height(4.dp))
                    Text("  💲  \$${order.total.toInt()}", fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("  👤  Vendedor: Ana Vendedora", fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("  🚴  Repartidor: Luis Repartidor", fontSize = 14.sp)
                }
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Productos", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Pizza", fontWeight = FontWeight.Medium)
                                Text("2 × \$120", fontSize = 12.sp, color = Color.Gray)
                            }
                            Text("\$${order.total.toInt()}", fontWeight = FontWeight.Medium)
                        }
                        HorizontalDivider(Modifier.padding(vertical = 12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "\$${order.total.toInt()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DeliveryPrimaryBlue
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Cambiar Estado", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Nuevo estado") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier.fillMaxWidth().menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(
                                        text = {
                                            Surface(
                                                color = deliveryStatusColor(status).copy(0.15f),
                                                shape = RoundedCornerShape(20.dp)
                                            ) {
                                                Text(
                                                    status,
                                                    Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                                    color = deliveryStatusColor(status),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            selectedStatus = status
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        if (statusChanged) {
                            Spacer(Modifier.height(10.dp))
                            Surface(
                                color = Color(0xFFFFF3CD),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, null,
                                        tint = Color(0xFF856404), modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "$savedStatus → $selectedStatus",
                                        color = Color(0xFF856404), fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = {
                                // Actualiza el repositorio compartido — se refleja en Dashboard y lista
                                viewModel.updateOrderStatus(orderId, selectedStatus)
                                savedStatus = selectedStatus
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = statusChanged,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = deliveryStatusColor(selectedStatus),
                                disabledContainerColor = Color(0xFFBDBDBD)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Actualizar",
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeliverySectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DeliveryInfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(text, fontSize = 14.sp)
    }
}