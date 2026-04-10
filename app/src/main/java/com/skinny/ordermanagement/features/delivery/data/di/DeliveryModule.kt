package com.skinny.ordermanagement.features.delivery.data.di

import com.skinny.ordermanagement.features.delivery.data.datasource.DeliveryApiService
import com.skinny.ordermanagement.features.delivery.data.datasource.DeliveryRemoteDataSource
import com.skinny.ordermanagement.features.delivery.data.datasource.DeliveryRemoteDataSourceImpl
import com.skinny.ordermanagement.features.delivery.data.repository.DeliveryRepositoryImpl
import com.skinny.ordermanagement.features.delivery.domain.repositories.DeliveryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeliveryModule {

    @Provides
    @Singleton
    fun provideDeliveryApiService(retrofit: Retrofit): DeliveryApiService {
        return retrofit.create(DeliveryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeliveryRemoteDataSource(
        apiService: DeliveryApiService
    ): DeliveryRemoteDataSource {
        return DeliveryRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideDeliveryRepository(
        remoteDataSource: DeliveryRemoteDataSource
    ): DeliveryRepository {
        return DeliveryRepositoryImpl(remoteDataSource)
    }
}



