package com.skinny.ordermanagement.features.seller.data.datasource

import com.skinny.ordermanagement.features.seller.data.datasource.model.CreateOrderRequest
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerClientResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerOrderResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerStatsResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.CreateSellerClientRequest
import com.skinny.ordermanagement.features.seller.data.datasource.model.CreateSellerClientResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.DeleteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerApiService {

    @GET("vendor/stats")
    suspend fun getStats(): Response<SellerStatsResponse>

    @GET("vendor/clients")
    suspend fun getClients(): Response<List<SellerClientResponse>>

    @POST("vendor/clients")
    suspend fun createClient(@Body request: CreateSellerClientRequest): Response<CreateSellerClientResponse>

    @DELETE("vendor/clients/{id}")
    suspend fun deleteClient(@Path("id") clientId: String): Response<DeleteResponse>

    @GET("vendor/orders")
    suspend fun getOrders(): Response<List<SellerOrderResponse>>

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<Unit>
}
