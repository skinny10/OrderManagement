package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.entities.AdminOrder
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<List<AdminOrder>> {
        return repository.getOrders()
    }
}