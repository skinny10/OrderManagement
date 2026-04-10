package com.skinny.ordermanagement.features.admin.data.datasource

import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminClientResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminDashboardResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminOrderResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminUserResponse
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

    
    @GET("admin/dashboard")
    suspend fun getDashboard(): Response<AdminDashboardResponse>

    
    @GET("admin/users")
    suspend fun getUsers(): Response<List<AdminUserResponse>>

    @POST("admin/users")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<DeleteResponse>

    
    @GET("admin/orders")
    suspend fun getOrders(): Response<List<AdminOrderResponse>>

    @DELETE("admin/orders/{id}")
    suspend fun deleteOrder(@Path("id") orderId: String): Response<DeleteResponse>

    
    @GET("admin/clients")
    suspend fun getClients(): Response<List<AdminClientResponse>>
}

