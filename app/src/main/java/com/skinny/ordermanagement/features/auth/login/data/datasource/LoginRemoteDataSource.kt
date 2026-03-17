package com.skinny.ordermanagement.features.auth.login.data.datasource

import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginRequest
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse
import javax.inject.Inject

interface LoginRemoteDataSource {
    suspend fun login(email: String, password: String): LoginResponse
}

class LoginRemoteDataSourceImpl @Inject constructor(
    private val apiService: AuthLoginApiService
) : LoginRemoteDataSource {

    override suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(
            LoginRequest(email = email, password = password)
        )
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        } else {
            throw Exception("Error del servidor: ${response.code()}")
        }
    }
}