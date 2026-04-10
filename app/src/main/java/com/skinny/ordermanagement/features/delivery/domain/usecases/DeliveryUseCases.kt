package com.skinny.ordermanagement.features.delivery.domain.usecases

import com.skinny.ordermanagement.features.delivery.domain.entities.DeliveryOrder
import com.skinny.ordermanagement.features.delivery.domain.repositories.DeliveryRepository
import javax.inject.Inject

class GetDeliveryStatsUseCase @Inject constructor(
    private val repository: DeliveryRepository
) {
    suspend operator fun invoke() = repository.getStats()
}

class GetDeliveryOrdersUseCase @Inject constructor(
    private val repository: DeliveryRepository
) {
    suspend operator fun invoke(): Result<List<DeliveryOrder>> {
        return repository.getOrders()
    }
}

class UpdateDeliveryOrderStatusUseCase @Inject constructor(
    private val repository: DeliveryRepository
) {
    suspend operator fun invoke(orderId: String, status: String): Result<Unit> {
        if (orderId.isBlank()) return Result.failure(Exception("ID de pedido inválido"))
        if (status.isBlank()) return Result.failure(Exception("Estado inválido"))
        return repository.updateOrderStatus(orderId, status)
    }
}



