package com.skinny.ordermanagement.features.seller.domain.usecases

import com.skinny.ordermanagement.features.seller.domain.entities.SellerClient
import com.skinny.ordermanagement.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class GetSellerClientsUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(): Result<List<SellerClient>> {
        return repository.getClients()
    }
}

class CreateSellerClientUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(
        name: String,
        phone: String,
        address: String
    ): Result<SellerClient> {
        if (name.isBlank()) return Result.failure(Exception("El nombre es obligatorio"))
        if (phone.isBlank()) return Result.failure(Exception("El teléfono es obligatorio"))
        if (address.isBlank()) return Result.failure(Exception("La dirección es obligatoria"))
        return repository.createClient(name, phone, address)
    }
}

class DeleteSellerClientUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(clientId: String): Result<Unit> {
        return repository.deleteClient(clientId)
    }
}

class GetSellerStatsUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke() = repository.getStats()
}

class GetSellerOrdersUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke() = repository.getOrders()
}

class CreateOrderUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(clientId: String, total: Double) =
        repository.createOrder(clientId, total)
}
