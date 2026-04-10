package com.skinny.ordermanagement.features.delivery.domain.repositories

import com.skinny.ordermanagement.features.delivery.domain.entities.DeliveryOrder
import com.skinny.ordermanagement.features.delivery.domain.entities.DeliveryStats

interface DeliveryRepository {
    suspend fun getStats(): Result<DeliveryStats>
    suspend fun getOrders(): Result<List<DeliveryOrder>>
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit>
}



