package com.skinny.ordermanagement.features.auth.register.data.repository

import com.skinny.ordermanagement.features.auth.register.data.datasource.AuthRemoteDataSource
import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterResponse
import com.skinny.ordermanagement.features.auth.register.domain.repositories.AuthRepository
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun register(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = remoteDataSource.register(name, lastName, email, password)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception("Error del servidor: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }
}

