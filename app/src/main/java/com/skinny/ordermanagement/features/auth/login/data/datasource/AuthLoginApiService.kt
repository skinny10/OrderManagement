package com.skinny.ordermanagement.features.auth.login.data.datasource

import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginRequest
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.FcmTokenRequest
import com.skinny.ordermanagement.features.auth.login.data.datasource.model.FcmTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PUT

interface AuthLoginApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @PUT("auth/fcm-token")                    
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Body request: FcmTokenRequest
    ): Response<FcmTokenResponse>
}
