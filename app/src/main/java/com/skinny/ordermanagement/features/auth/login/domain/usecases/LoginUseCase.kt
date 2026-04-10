package com.skinny.ordermanagement.features.auth.login.domain.usecases

import com.skinny.ordermanagement.features.auth.login.data.datasource.model.LoginResponse
import com.skinny.ordermanagement.features.auth.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<LoginResponse> {

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("El correo no es válido"))
        }

        if (password.length < 8) {
            return Result.failure(Exception("La contraseña debe tener al menos 8 caracteres"))
        }

        return loginRepository.login(email, password)
    }
}

