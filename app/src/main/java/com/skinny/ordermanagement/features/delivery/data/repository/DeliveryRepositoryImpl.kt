package com.skinny.ordermanagement.features.delivery.data.repository

import com.skinny.ordermanagement.features.delivery.data.datasource.DeliveryRemoteDataSource
import com.skinny.ordermanagement.features.delivery.data.datasource.model.DeliveryOrderResponse
import com.skinny.ordermanagement.features.delivery.domain.entities.DeliveryOrder
import com.skinny.ordermanagement.features.delivery.domain.entities.DeliveryStats
import com.skinny.ordermanagement.features.delivery.domain.repositories.DeliveryRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeliveryRepositoryImpl @Inject constructor(
    private val remoteDataSource: DeliveryRemoteDataSource
) : DeliveryRepository {

    override suspend fun getStats(): Result<DeliveryStats> = safeCall {
        val r = remoteDataSource.getStats()
        DeliveryStats(
            totalOrders     = r.totalOrders,
            pendingOrders   = r.pendingOrders,
            onWayOrders     = r.onWayOrders,
            deliveredOrders = r.deliveredOrders,
            recentOrders    = r.recentOrders.map { it.toEntity() }
        )
    }

    override suspend fun getOrders(): Result<List<DeliveryOrder>> = safeCall {
        remoteDataSource.getOrders().map { it.toEntity() }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = safeCall {
        remoteDataSource.updateOrderStatus(orderId, status)
    }

    private suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: HttpException) {
            Result.failure(Exception("Error del servidor: ${e.code()}"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a internet"))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error inesperado"))
        }
    }
}

private fun DeliveryOrderResponse.toEntity() = DeliveryOrder(
    id         = id,
    clientName = clientName,
    address    = address,
    total      = total,
    status     = status,
    date       = date
)



