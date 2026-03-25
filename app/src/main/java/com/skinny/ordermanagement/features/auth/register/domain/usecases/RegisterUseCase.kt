package com.skinny.ordermanagement.features.auth.register.domain.usecases

import com.skinny.ordermanagement.features.auth.register.data.datasource.model.RegisterResponse
import com.skinny.ordermanagement.features.auth.register.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        lastName: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {

        if (name.isBlank() || lastName.isBlank()) {
            return Result.failure(Exception("El nombre y apellido son obligatorios"))
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("El correo no es válido"))
        }

        if (password.length < 8) {
            return Result.failure(Exception("La contraseña debe tener al menos 8 caracteres"))
        }

        return authRepository.register(name, lastName, email, password)
    }
}