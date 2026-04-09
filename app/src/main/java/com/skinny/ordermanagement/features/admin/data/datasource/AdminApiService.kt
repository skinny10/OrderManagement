package com.skinny.ordermanagement.features.admin.data.datasource

import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminClientResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminDashboardResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminOrderResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminUserResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.CreateClientRequest
import com.skinny.ordermanagement.features.admin.data.datasource.model.CreateClientResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.CreateUserRequest
import com.skinny.ordermanagement.features.admin.data.datasource.model.CreateUserResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.DeleteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AdminApiService {

    // 👇 Dashboard
    @GET("admin/dashboard")
    suspend fun getDashboard(): Response<AdminDashboardResponse>

    // 👇 Usuarios
    @GET("admin/users")
    suspend fun getUsers(): Response<List<AdminUserResponse>>

    @POST("admin/users")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<DeleteResponse>

    // 👇 Pedidos
    @GET("orders")
    suspend fun getOrders(): Response<List<AdminOrderResponse>>

    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") orderId: String): Response<DeleteResponse>

    // 👇 Clientes
    @GET("vendor/clients")
    suspend fun getClients(): Response<List<AdminClientResponse>>

    @POST("vendor/clients")
    suspend fun createClient(@Body request: CreateClientRequest): Response<CreateClientResponse>

    @DELETE("vendor/clients/{id}")
    suspend fun deleteClient(@Path("id") clientId: String): Response<DeleteResponse>
}