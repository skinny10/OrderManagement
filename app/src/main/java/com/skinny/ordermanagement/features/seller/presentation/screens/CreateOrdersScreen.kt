package com.skinny.ordermanagement.features.seller.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.seller.presentation.viewmodels.*
import com.skinny.ordermanagement.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    onBack: () -> Unit,
    onOrderCreated: () -> Unit,
    viewModel: CreateOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(uiState.orderCreated) {
        if (uiState.orderCreated) onOrderCreated()
    }
    LaunchedEffect(uiState.error) {
        uiState.error?.let { snackbar.showSnackbar(it); viewModel.clearError() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Pedido") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    if (uiState.cartItems.isNotEmpty()) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "\$${uiState.total.toInt()}",
                                fontWeight = FontWeight.Bold, fontSize = 22.sp, color = PrimaryBlue
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    Button(
                        onClick = { viewModel.createOrder() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.selectedClient != null && uiState.cartItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            if (uiState.selectedClient == null) "Selecciona un cliente"
                            else if (uiState.cartItems.isEmpty()) "Agrega productos"
                            else "Confirmar Pedido",
                            Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            item {
                OrderSectionCard(title = "1. Selecciona un Cliente") {
                    uiState.clients.forEach { client ->
                        val selected = uiState.selectedClient?.id == client.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.5.dp,
                                    if (selected) PrimaryBlue else Color.LightGray,
                                    RoundedCornerShape(8.dp)
                                )
                                .background(if (selected) PrimaryBlue.copy(0.08f) else Color.Transparent)
                                .clickable { viewModel.selectClient(client) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                Modifier.size(36.dp), shape = RoundedCornerShape(50),
                                color = PrimaryBlue.copy(0.15f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(client.name.first().toString(),
                                        color = PrimaryBlue, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(client.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(client.address, fontSize = 12.sp, color = Color.Gray)
                            }
                            if (selected) {
                                Surface(color = PrimaryBlue, shape = RoundedCornerShape(20.dp)) {
                                    Text("✓", Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                        color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            
            item {
                OrderSectionCard(title = "2. Agrega Productos") {
                    uiState.products.forEach { product ->
                        val cartItem = uiState.cartItems.find { it.product.id == product.id }
                        val qty = cartItem?.quantity ?: 0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text("\$${product.price.toInt()}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (qty > 0) {
                                    IconButton(
                                        onClick = { viewModel.removeFromCart(product) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, null,
                                            tint = Color.Red, modifier = Modifier.size(18.dp))
                                    }
                                    Text(
                                        qty.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.addToCart(product) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, null,
                                        tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            
            if (uiState.cartItems.isNotEmpty()) {
                item {
                    OrderSectionCard(title = "Resumen del Pedido") {
                        uiState.cartItems.forEach { item ->
                            Row(
                                Modifier.fillMaxWidth().padding(bottom = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.product.name} × ${item.quantity}",
                                    fontSize = 14.sp)
                                Text("\$${(item.product.price * item.quantity).toInt()}",
                                    fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("\$${uiState.total.toInt()}",
                                fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
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

