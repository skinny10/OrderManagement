package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(orderId: String): Result<Unit> {
        return repository.deleteOrder(orderId)
    }
}