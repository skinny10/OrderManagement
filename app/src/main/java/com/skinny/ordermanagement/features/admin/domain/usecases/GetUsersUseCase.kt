package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.entities.AdminUser
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<List<AdminUser>> {
        return repository.getUsers()
    }
}