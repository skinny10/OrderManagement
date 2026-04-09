package com.skinny.ordermanagement.features.seller.domain.repositories

import com.skinny.ordermanagement.features.seller.domain.entities.SellerClient
import com.skinny.ordermanagement.features.seller.domain.entities.SellerOrder
import com.skinny.ordermanagement.features.seller.domain.entities.SellerStats

interface SellerRepository {
    suspend fun getStats(): Result<SellerStats>
    suspend fun getClients(): Result<List<SellerClient>>
    suspend fun createClient(
        name: String,
        phone: String,
        address: String
    ): Result<SellerClient>
    suspend fun deleteClient(clientId: String): Result<Unit>
    suspend fun getOrders(): Result<List<SellerOrder>>
    suspend fun createOrder(clientId: String, total: Double): Result<Unit>
}
