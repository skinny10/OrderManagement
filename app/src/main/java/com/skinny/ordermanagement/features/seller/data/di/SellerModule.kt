package com.skinny.ordermanagement.features.seller.data.di

import com.skinny.ordermanagement.features.seller.data.datasource.SellerApiService
import com.skinny.ordermanagement.features.seller.data.datasource.SellerRemoteDataSource
import com.skinny.ordermanagement.features.seller.data.datasource.SellerRemoteDataSourceImpl
import com.skinny.ordermanagement.features.seller.data.repository.SellerRepositoryImpl
import com.skinny.ordermanagement.features.seller.domain.repositories.SellerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SellerModule {

    @Provides
    @Singleton
    fun provideSellerApiService(retrofit: Retrofit): SellerApiService {
        return retrofit.create(SellerApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSellerRemoteDataSource(
        apiService: SellerApiService
    ): SellerRemoteDataSource {
        return SellerRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideSellerRepository(
        remoteDataSource: SellerRemoteDataSource
    ): SellerRepository {
        return SellerRepositoryImpl(remoteDataSource)
    }
}



