package com.skinny.ordermanagement.features.delivery.data.datasource

import com.skinny.ordermanagement.features.delivery.data.datasource.model.DeliveryOrderResponse
import com.skinny.ordermanagement.features.delivery.data.datasource.model.DeliveryStatsResponse
import com.skinny.ordermanagement.features.delivery.data.datasource.model.UpdateOrderStatusRequest
import javax.inject.Inject

interface DeliveryRemoteDataSource {
    suspend fun getStats(): DeliveryStatsResponse
    suspend fun getOrders(): List<DeliveryOrderResponse>
    suspend fun updateOrderStatus(orderId: String, status: String)
}

class DeliveryRemoteDataSourceImpl @Inject constructor(
    private val apiService: DeliveryApiService
) : DeliveryRemoteDataSource {

    override suspend fun getStats(): DeliveryStatsResponse {
        val response = apiService.getStats()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        }
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun getOrders(): List<DeliveryOrderResponse> {
        val response = apiService.getOrders()
        if (response.isSuccessful) return response.body() ?: emptyList()
        throw Exception("Error del servidor: ${response.code()}")
    }

    override suspend fun updateOrderStatus(orderId: String, status: String) {
        val response = apiService.updateOrderStatus(orderId, UpdateOrderStatusRequest(status))
        if (!response.isSuccessful) throw Exception("Error al actualizar pedido: ${response.code()}")
    }
}



