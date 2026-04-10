package com.skinny.ordermanagement.ui.theme

import androidx.compose.ui.graphics.Color

fun getStatusColor(status: String): Color = when (status.lowercase()) {
    "pendiente" -> Color(0xFFF59E0B)
    "preparando" -> Color(0xFF3B82F6)
    "en camino" -> Color(0xFF8B5CF6)
    "entregado" -> Color(0xFF10B981)
    "en proceso" -> Color(0xFF3B82F6)
    "completado" -> Color(0xFF10B981)
    "cancelado" -> Color(0xFFEF4444)
    else -> Color.Gray
}



