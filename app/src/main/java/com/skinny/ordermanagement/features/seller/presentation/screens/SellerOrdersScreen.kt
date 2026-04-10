package com.skinny.ordermanagement.features.seller.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.seller.presentation.components.OrderCard
import com.skinny.ordermanagement.features.seller.presentation.viewmodels.SellerOrdersViewModel


private val SELLER_PRIMARY_COLOR = androidx.compose.ui.graphics.Color(0xFF5C6BC0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    onCreateOrder: () -> Unit,
    viewModel: SellerOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadOrders() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SELLER_PRIMARY_COLOR,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateOrder, containerColor = SELLER_PRIMARY_COLOR) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    ) { padding ->
        if (uiState.orders.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay pedidos aún.\nToca + para crear uno.", color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.orders) { order ->
                    OrderCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }
        }
    }
}

