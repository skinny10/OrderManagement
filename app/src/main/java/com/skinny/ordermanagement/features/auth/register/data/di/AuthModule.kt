package com.skinny.ordermanagement.features.auth.register.data.di

import com.skinny.ordermanagement.features.auth.register.data.datasource.AuthApiService
import com.skinny.ordermanagement.features.auth.register.data.datasource.AuthRemoteDataSource
import com.skinny.ordermanagement.features.auth.register.data.datasource.AuthRemoteDataSourceImpl
import com.skinny.ordermanagement.features.auth.register.data.repository.AuthRepositoryImpl
import com.skinny.ordermanagement.features.auth.register.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        apiService: AuthApiService
    ): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        remoteDataSource: AuthRemoteDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(remoteDataSource)
    }
}