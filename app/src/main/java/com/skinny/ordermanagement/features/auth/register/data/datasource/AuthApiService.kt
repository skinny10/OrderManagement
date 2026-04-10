package com.skinny.ordermanagement.features.auth.register.data.datasource

import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterRequest
import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}
