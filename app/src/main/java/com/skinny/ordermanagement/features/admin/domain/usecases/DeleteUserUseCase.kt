package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return repository.deleteUser(userId)
    }
}