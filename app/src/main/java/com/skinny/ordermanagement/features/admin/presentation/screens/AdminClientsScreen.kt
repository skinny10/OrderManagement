package com.skinny.ordermanagement.features.admin.presentation.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminClientUi
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminClientsViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClientsScreen(
    onBack: () -> Unit,
    viewModel: AdminClientsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val snackbar   = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbar.showSnackbar("✅ Cliente guardado correctamente")
            viewModel.clearSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AdminBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }, containerColor = AdminBlue) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.error != null) {
                Text("Error: ${uiState.error}", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            if (uiState.clients.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay clientes registrados", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.clients, key = { it.id }) { client ->
                        AdminClientCard(client = client, onDelete = { viewModel.deleteClient(client.id) })
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddClientDialog(
            onDismiss = { showDialog = false },
            onSave = { name, phone, address ->
                viewModel.addClient(name, phone, address)
                showDialog = false
            }
        )
    }
}

@Composable
fun AdminClientCard(client: AdminClientUi, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

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
                    Text(client.name.first().toString(),
                        fontWeight = FontWeight.Bold, color = AdminBlue, fontSize = 18.sp)
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
                Surface(
                    color = AdminBlue.copy(0.1f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        "${client.totalOrders} pedidos",
                        Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp, color = AdminBlue
                    )
                }
            }
            IconButton(onClick = { showConfirm = true }) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFE53935))
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Eliminar Cliente") },
            text  = { Text("¿Eliminar a ${client.name}? Esta acción no se puede deshacer.") },
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

@Composable
fun AddClientDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var validName by remember { mutableStateOf(true) }
    var validPhone by remember { mutableStateOf(true) }
    var validAddress by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Cliente") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; validName = name.isNotBlank() },
                    label = { Text("Nombre") },
                    isError = !validName
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; validPhone = phone.isNotBlank() },
                    label = { Text("Teléfono") },
                    isError = !validPhone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it; validAddress = address.isNotBlank() },
                    label = { Text("Dirección") },
                    isError = !validAddress
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validName && validPhone && validAddress) {
                        onSave(name, phone, address)
                    }
                },
                enabled = validName && validPhone && validAddress
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
