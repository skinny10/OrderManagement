package com.skinny.ordermanagement.features.auth.register.domain.repositories

import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterResponse

interface AuthRepository {
    suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): Result<RegisterResponse>
}