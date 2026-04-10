package com.skinny.ordermanagement.features.auth.register.data.datasource

import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterRequest
import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterResponse
import javax.inject.Inject

interface AuthRemoteDataSource {
    suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): RegisterResponse
}

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): RegisterResponse {
        val response = authApiService.register(
            RegisterRequest(
                name = name,
                lastName = lastName,
                email = email,
                password = password
            )
        )
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        } else {
            throw Exception("Error del servidor: ${response.code()}")
        }
    }
}

