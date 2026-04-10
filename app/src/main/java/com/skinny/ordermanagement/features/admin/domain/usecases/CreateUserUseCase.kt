package com.skinny.ordermanagement.features.admin.domain.usecases

import com.skinny.ordermanagement.features.admin.domain.entities.AdminUser
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(
        name: String,
        lastName: String,
        role: String,
        email: String,
        password: String
    ): Result<AdminUser> {
        if (name.isBlank())     return Result.failure(Exception("El nombre es obligatorio"))
        if (lastName.isBlank()) return Result.failure(Exception("El apellido es obligatorio"))
        if (email.isBlank())    return Result.failure(Exception("El correo es obligatorio"))
        if (password.length < 8) return Result.failure(Exception("La contraseña debe tener mínimo 8 caracteres"))
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("El correo no es válido"))
        }
        return repository.createUser(name, lastName, role, email, password)
    }
}

