package com.skinny.ordermanagement.features.auth.login.domain.repositories

import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun updateFcmToken(token: String, fcmToken: String): Result<Unit>

}

