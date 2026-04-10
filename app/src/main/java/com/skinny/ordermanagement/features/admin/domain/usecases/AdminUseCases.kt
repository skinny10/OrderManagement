package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetAdminClientsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke() = repository.getClients()
}

class GetAdminOrdersUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke() = repository.getOrders()
}


