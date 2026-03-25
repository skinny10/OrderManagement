package com.skinny.ordermanagement.features.admin.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminUserUi
import com.skinny.ordermanagement.features.admin.presentation.viewmodels.AdminUsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    onBack: () -> Unit,
    viewModel: AdminUsersViewModel = hiltViewModel()
) {
    val uiState  by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val snackbar   = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbar.showSnackbar("✅ Usuario agregado correctamente")
            viewModel.clearSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios del Sistema") },
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
                Icon(Icons.Default.Add, contentDescription = "Agregar usuario", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Sección Vendedores
            val sellers    = uiState.users.filter { it.role == "Vendedor" }
            val deliveries = uiState.users.filter { it.role == "Repartidor" }

            if (sellers.isNotEmpty()) {
                item {
                    Text("Vendedores", fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp, color = AdminBlue)
                }
                items(sellers, key = { it.id }) { user ->
                    AdminUserCard(user = user, onDelete = { viewModel.deleteUser(user.id) })
                }
            }

            if (deliveries.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Text("Repartidores", fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp, color = Color(0xFF8B5CF6))
                }
                items(deliveries, key = { it.id }) { user ->
                    AdminUserCard(user = user, onDelete = { viewModel.deleteUser(user.id) })
                }
            }
        }
    }

    if (showDialog) {
        AddUserDialog(
            onDismiss = { showDialog = false },
            onSave    = { name, role, email ->
                viewModel.addUser(name, role, email)
                showDialog = false
            }
        )
    }
}

@Composable
fun AdminUserCard(user: AdminUserUi, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }
    val roleColor = if (user.role == "Vendedor") AdminBlue else Color(0xFF8B5CF6)
    val roleIcon  = if (user.role == "Vendedor") Icons.Default.Store else Icons.Default.DeliveryDining

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
                color = roleColor.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(roleIcon, null, tint = roleColor, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(user.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(user.email, fontSize = 13.sp, color = Color.Gray)
                }
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(color = roleColor.copy(0.1f), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            user.role,
                            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp, color = roleColor, fontWeight = FontWeight.Medium
                        )
                    }
                    Surface(color = Color(0xFFF3F4F6), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            "${user.activeOrders} pedidos activos",
                            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp, color = Color.Gray
                        )
                    }
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
            title = { Text("Eliminar Usuario") },
            text  = { Text("¿Eliminar a ${user.name}? Esta acción no se puede deshacer.") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name     by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var role     by remember { mutableStateOf("Vendedor") }
    var expanded by remember { mutableStateOf(false) }
    var error    by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(24.dp)) {
                Text("Nuevo Usuario", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = name, onValueChange = { name = it; error = "" },
                    label = { Text("Nombre completo *") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email, onValueChange = { email = it; error = "" },
                    label = { Text("Correo electrónico *") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = role, onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol *") },
                        leadingIcon = { Icon(Icons.Default.Work, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Vendedor", "Repartidor").forEach { option ->
                            DropdownMenuItem(
                                text    = { Text(option) },
                                onClick = { role = option; expanded = false }
                            )
                        }
                    }
                }

                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(error, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onDismiss, Modifier.weight(1f)) { Text("Cancelar") }
                    Button(
                        onClick = {
                            when {
                                name.isBlank()  -> error = "El nombre es obligatorio"
                                email.isBlank() -> error = "El correo es obligatorio"
                                else            -> onSave(name, role, email)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors   = ButtonDefaults.buttonColors(containerColor = AdminBlue)
                    ) { Text("Guardar") }
                }
            }
        }
    }
}