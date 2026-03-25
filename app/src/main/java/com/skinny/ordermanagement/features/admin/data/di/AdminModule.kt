package com.skinny.ordermanagement.features.admin.data.di

import com.skinny.ordermanagement.features.admin.data.datasource.AdminApiService
import com.skinny.ordermanagement.features.admin.data.datasource.AdminRemoteDataSource
import com.skinny.ordermanagement.features.admin.data.datasource.AdminRemoteDataSourceImpl
import com.skinny.ordermanagement.features.admin.data.repository.AdminRepositoryImpl
import com.skinny.ordermanagement.features.admin.domain.repositories.AdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdminModule {

    @Provides
    @Singleton
    fun provideAdminApiService(retrofit: Retrofit): AdminApiService {
        return retrofit.create(AdminApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminRemoteDataSource(
        apiService: AdminApiService
    ): AdminRemoteDataSource {
        return AdminRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        remoteDataSource: AdminRemoteDataSource
    ): AdminRepository {
        return AdminRepositoryImpl(remoteDataSource)
    }
}