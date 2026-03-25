package com.skinny.ordermanagement.features.auth.login.data.repository

import com.skinny.ordermanagement.features.auth.login.data.datasource.LoginRemoteDataSource
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse
import com.skinny.ordermanagement.features.auth.login.domain.repositories.LoginRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = remoteDataSource.login(email, password)
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