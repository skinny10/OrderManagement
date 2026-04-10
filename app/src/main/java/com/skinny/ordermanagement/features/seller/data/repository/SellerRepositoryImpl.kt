package com.skinny.ordermanagement.features.seller.data.repository

import com.skinny.ordermanagement.features.seller.data.datasource.SellerRemoteDataSource
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerClientResponse
import com.skinny.ordermanagement.features.seller.data.datasource.model.SellerOrderResponse
import com.skinny.ordermanagement.features.seller.domain.entities.SellerClient
import com.skinny.ordermanagement.features.seller.domain.entities.SellerOrder
import com.skinny.ordermanagement.features.seller.domain.entities.SellerStats
import com.skinny.ordermanagement.features.seller.domain.repositories.SellerRepository
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val remoteDataSource: SellerRemoteDataSource
) : SellerRepository {

    override suspend fun getStats(): Result<SellerStats> = safeCall {
        val r = remoteDataSource.getStats()
        SellerStats(
            totalClients    = r.totalClients,
            todayOrders     = r.todayOrders,
            pendingOrders   = r.pendingOrders,
            preparingOrders = r.preparingOrders,
            onWayOrders     = r.onWayOrders,
            deliveredOrders = r.deliveredOrders,
            recentOrders    = r.recentOrders.map { it.toEntity() },
            clients         = r.clients.map { it.toEntity() }
        )
    }

    override suspend fun getClients(): Result<List<SellerClient>> = safeCall {
        remoteDataSource.getClients().map { it.toEntity() }
    }

    override suspend fun createClient(
        name: String,
        phone: String,
        address: String
    ): Result<SellerClient> = safeCall {
        remoteDataSource.createClient(name, phone, address).toEntity()
    }

    override suspend fun deleteClient(clientId: String): Result<Unit> = safeCall {
        remoteDataSource.deleteClient(clientId)
    }

    override suspend fun getOrders(): Result<List<SellerOrder>> = safeCall {
        remoteDataSource.getOrders().map { it.toEntity() }
    }

    override suspend fun createOrder(clientId: String, total: Double, items: List<Any>): Result<Unit> = safeCall {
        remoteDataSource.createOrder(clientId, total, items)
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

private fun SellerClientResponse.toEntity() = SellerClient(
    id          = id ?: UUID.randomUUID().toString(),
    name        = name,
    phone       = phone,
    address     = address,
    totalOrders = totalOrders
)

private fun SellerOrderResponse.toEntity() = SellerOrder(
    id         = id,
    clientName = clientName,
    address    = address,
    total      = total,
    status     = status,
    date       = date
)


