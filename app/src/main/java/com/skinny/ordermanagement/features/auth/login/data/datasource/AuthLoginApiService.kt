package com.skinny.ordermanagement.features.auth.login.data.datasource

import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginRequest
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthLoginApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}