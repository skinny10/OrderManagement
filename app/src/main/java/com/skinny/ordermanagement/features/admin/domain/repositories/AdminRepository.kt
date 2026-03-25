package com.skinny.ordermanagement.features.admin.domain.repositories

import com.skinny.ordermanagement.features.admin.domain.entities.AdminClient
import com.skinny.ordermanagement.features.admin.domain.entities.AdminDashboard
import com.skinny.ordermanagement.features.admin.domain.entities.AdminOrder
import com.skinny.ordermanagement.features.admin.domain.entities.AdminUser

interface AdminRepository {

    suspend fun getDashboard(): Result<AdminDashboard>

    suspend fun getUsers(): Result<List<AdminUser>>
    suspend fun createUser(
        name: String,
        lastName: String,
        role: String,
        email: String,
        password: String
    ): Result<AdminUser>
    suspend fun deleteUser(userId: String): Result<Unit>

    suspend fun getOrders(): Result<List<AdminOrder>>
    suspend fun deleteOrder(orderId: String): Result<Unit>

    suspend fun getClients(): Result<List<AdminClient>>
    suspend fun deleteClient(clientId: String): Result<Unit>
}