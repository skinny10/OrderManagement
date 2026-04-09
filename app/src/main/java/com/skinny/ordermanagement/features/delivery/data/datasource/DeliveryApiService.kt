package com.skinny.ordermanagement.features.delivery.data.datasource

import com.skinny.ordermanagement.features.delivery.data.datasource.model.DeliveryOrderResponse
import com.skinny.ordermanagement.features.delivery.data.datasource.model.DeliveryStatsResponse
import com.skinny.ordermanagement.features.delivery.data.datasource.model.UpdateOrderStatusRequest
import com.skinny.ordermanagement.features.delivery.data.datasource.model.UpdateOrderStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeliveryApiService {

    @GET("delivery/stats")
    suspend fun getStats(): Response<DeliveryStatsResponse>

    @GET("delivery/orders")
    suspend fun getOrders(): Response<List<DeliveryOrderResponse>>

    @PUT("delivery/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") orderId: String,
        @Body request: UpdateOrderStatusRequest
    ): Response<UpdateOrderStatusResponse>
}

