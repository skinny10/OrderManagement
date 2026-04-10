package com.skinny.ordermanagement.features.admin.data.datasource

import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminClientResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminDashboardResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminOrderResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminUserResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.CreateUserRequest
import javax.inject.Inject


interface AdminRemoteDataSource {
    suspend fun getDashboard(): AdminDashboardResponse
    suspend fun getUsers(): List<AdminUserResponse>
    suspend fun createUser(
        name: String,
        lastName: String,
        role: String,
        email: String,
        password: String
    ): AdminUserResponse
    suspend fun deleteUser(userId: String)
    suspend fun getOrders(): List<AdminOrderResponse>
    suspend fun deleteOrder(orderId: String)
    suspend fun getClients(): List<AdminClientResponse>
}

class AdminRemoteDataSourceImpl @Inject constructor(
    private val apiService: AdminApiService
) : AdminRemoteDataSource {

    override suspend fun getDashboard(): AdminDashboardResponse {
        val response = apiService.getDashboard()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        }
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun getUsers(): List<AdminUserResponse> {
        val response = apiService.getUsers()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun createUser(
        name: String,
        lastName: String,
        role: String,
        email: String,
        password: String
    ): AdminUserResponse {
        val response = apiService.createUser(
            CreateUserRequest(
                name     = name,
                lastName = lastName,
                role     = role,
                email    = email,
                password = password
            )
        )
        if (response.isSuccessful) {
            return response.body()?.user ?: throw Exception("Error al crear usuario")
        }
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun deleteUser(userId: String) {
        val response = apiService.deleteUser(userId)
        if (!response.isSuccessful) throw Exception("Error al eliminar usuario: ${response.code()}")
    }

    override suspend fun getOrders(): List<AdminOrderResponse> {
        val response = apiService.getOrders()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun deleteOrder(orderId: String) {
        val response = apiService.deleteOrder(orderId)
        if (!response.isSuccessful) throw Exception("Error al eliminar pedido: ${response.code()}")
    }

    override suspend fun getClients(): List<AdminClientResponse> {
        val response = apiService.getClients()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }
}


