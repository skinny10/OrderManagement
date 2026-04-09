package com.skinny.ordermanagement.features.seller.data.datasource

import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerClientResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerOrderResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerStatsResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.CreateSellerClientRequest
import com.skinny.ordermanagement.features.seller.data.datasource.model.CreateOrderRequest
import javax.inject.Inject

interface SellerRemoteDataSource {
    suspend fun getStats(): SellerStatsResponse
    suspend fun getClients(): List<SellerClientResponse>
    suspend fun createClient(
        name: String,
        phone: String,
        address: String
    ): SellerClientResponse
    suspend fun deleteClient(clientId: String)
    suspend fun getOrders(): List<SellerOrderResponse>
    suspend fun createOrder(clientId: String, total: Double)
}

class SellerRemoteDataSourceImpl @Inject constructor(
    private val apiService: SellerApiService
) : SellerRemoteDataSource {

    override suspend fun getStats(): SellerStatsResponse {
        val response = apiService.getStats()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        }
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun getClients(): List<SellerClientResponse> {
        val response = apiService.getClients()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun createClient(
        name: String,
        phone: String,
        address: String
    ): SellerClientResponse {
        val response = apiService.createClient(
            CreateSellerClientRequest(
                name    = name,
                phone   = phone,
                address = address
            )
        )
        if (response.isSuccessful) {
            return response.body()?.client ?: throw Exception("Error al crear cliente")
        }
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun deleteClient(clientId: String) {
        val response = apiService.deleteClient(clientId)
        if (!response.isSuccessful) throw Exception("Error al eliminar cliente: ${response.code()}")
    }

    override suspend fun getOrders(): List<SellerOrderResponse> {
        val response = apiService.getOrders()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun createOrder(clientId: String, total: Double) {
        val response = apiService.createOrder(CreateOrderRequest(clientId, total))
        if (!response.isSuccessful) throw Exception("Error al crear orden: ${response.code()}")
    }
}
