package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class DeleteClientUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(clientId: String): Result<Unit> {
        return repository.deleteClient(clientId)
    }
}