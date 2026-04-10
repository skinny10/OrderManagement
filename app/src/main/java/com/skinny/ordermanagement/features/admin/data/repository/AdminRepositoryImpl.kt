package com.skinny.ordermanagement.features.admin.data.repository

import com.skinny.ordermanagement.features.admin.data.datasource.AdminRemoteDataSource
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminClientResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminOrderResponse
import com.skinny.ordermanagement.features.admin.data.datasource.model.AdminUserResponse
import com.skinny.ordermanagement.features.admin.domain.entities.AdminClient
import com.skinny.ordermanagement.features.admin.domain.entities.AdminDashboard
import com.skinny.ordermanagement.features.admin.domain.entities.AdminOrder
import com.skinny.ordermanagement.features.admin.domain.entities.AdminUser
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class AdminRepositoryImpl @Inject constructor(
    private val remoteDataSource: AdminRemoteDataSource
) : AdminRepository {

    override suspend fun getDashboard(): Result<AdminDashboard> = safeCall {
        val r = remoteDataSource.getDashboard()
        AdminDashboard(
            totalOrders     = r.totalOrders,
            totalClients    = r.totalClients,
            totalSellers    = r.totalSellers,
            totalDelivery   = r.totalDelivery,
            pendingOrders   = r.pendingOrders,
            preparingOrders = r.preparingOrders,
            onWayOrders     = r.onWayOrders,
            deliveredOrders = r.deliveredOrders,
            totalRevenue    = r.totalRevenue,
            recentOrders    = r.recentOrders.map { it.toEntity() }
        )
    }

    override suspend fun getUsers(): Result<List<AdminUser>> = safeCall {
        remoteDataSource.getUsers().map { it.toEntity() }
    }

    override suspend fun createUser(
        name: String,
        lastName: String,
        role: String,
        email: String,
        password: String
    ): Result<AdminUser> = safeCall {
        remoteDataSource.createUser(name, lastName, role, email, password).toEntity()
    }

    override suspend fun deleteUser(userId: String): Result<Unit> = safeCall {
        remoteDataSource.deleteUser(userId)
    }

    override suspend fun getOrders(): Result<List<AdminOrder>> = safeCall {
        remoteDataSource.getOrders().map { it.toEntity() }
    }

    override suspend fun deleteOrder(orderId: String): Result<Unit> = safeCall {
        remoteDataSource.deleteOrder(orderId)
    }

    override suspend fun getClients(): Result<List<AdminClient>> = safeCall {
        remoteDataSource.getClients().map { it.toEntity() }
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

private fun AdminUserResponse.toEntity() = AdminUser(
    id           = id,
    name         = name,
    role         = role,
    email        = email,
    activeOrders = activeOrders
)

private fun AdminOrderResponse.toEntity() = AdminOrder(
    id           = id,
    clientName   = clientName,
    sellerName   = sellerName,
    deliveryName = deliveryName,
    total        = total,
    status       = status,
    date         = date
)

private fun AdminClientResponse.toEntity() = AdminClient(
    id          = id ?: "",
    name        = name,
    phone       = phone,
    address     = address,
    totalOrders = totalOrders
)


