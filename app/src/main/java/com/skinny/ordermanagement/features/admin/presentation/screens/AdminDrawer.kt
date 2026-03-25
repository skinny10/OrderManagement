package com.skinny.ordermanagement.features.admin.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDrawer(
    onNavigateToDashboard : () -> Unit,
    onNavigateToOrders    : () -> Unit,
    onNavigateToClients   : () -> Unit,
    onNavigateToUsers     : () -> Unit,
    onLogout              : () -> Unit,
    onCloseDrawer         : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
    ) {
        // 👇 Header con gradiente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(AdminBlue, Color(0xFF7986CB))
                    )
                )
                .padding(24.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(50),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(40.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Administrador",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Panel de Control",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // 👇 Items de navegación
        AdminDrawerItem(
            icon  = Icons.Default.Dashboard,
            label = "Dashboard",
            onClick = { onNavigateToDashboard(); onCloseDrawer() }
        )
        AdminDrawerItem(
            icon  = Icons.Default.ShoppingCart,
            label = "Pedidos",
            onClick = { onNavigateToOrders(); onCloseDrawer() }
        )
        AdminDrawerItem(
            icon  = Icons.Default.Person,
            label = "Clientes",
            onClick = { onNavigateToClients(); onCloseDrawer() }
        )
        AdminDrawerItem(
            icon  = Icons.Default.Group,
            label = "Usuarios",
            onClick = { onNavigateToUsers(); onCloseDrawer() }
        )

        Spacer(Modifier.weight(1f))
        Divider()

        // 👇 Cerrar sesión
        AdminDrawerItem(
            icon    = Icons.Default.ExitToApp,
            label   = "Cerrar sesión",
            tint    = Color(0xFFE53935),
            onClick = { onLogout(); onCloseDrawer() }
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AdminDrawerItem(
    icon    : ImageVector,
    label   : String,
    tint    : Color = AdminBlue,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = if (tint == AdminBlue) Color(0xFF2D3748) else tint,
            fontWeight = FontWeight.Medium
        )
    }
}