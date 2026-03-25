package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.entities.AdminClient
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<List<AdminClient>> {
        return repository.getClients()
    }
}