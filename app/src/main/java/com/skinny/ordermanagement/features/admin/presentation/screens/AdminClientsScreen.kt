package com.skinny.ordermanagement.features.admin.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skinny.ordermanagement.ui.theme.AdminBlue
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminClientsViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminClientUi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClientsScreen(
    onBack: () -> Unit,
    viewModel: AdminClientsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
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
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.error != null) {
                Text("Error: ${uiState.error}", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            if (uiState.clients.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay clientes registrados.",
                        color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.clients, key = { it.id }) { client ->
                        AdminClientCard(client = client)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminClientCard(client: AdminClientUi) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                Modifier.size(44.dp), shape = RoundedCornerShape(50),
                color = AdminBlue.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        if (client.name.isNotEmpty()) client.name.first().toString() else "?",
                        fontWeight = FontWeight.Bold, color = AdminBlue, fontSize = 18.sp
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(client.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(client.phone, fontSize = 13.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(client.address, fontSize = 13.sp, color = Color.Gray)
                }
                Text("Pedidos: ${client.totalOrders}", fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }
}


