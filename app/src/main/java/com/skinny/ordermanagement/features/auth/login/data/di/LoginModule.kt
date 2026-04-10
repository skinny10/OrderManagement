package com.skinny.ordermanagement.features.auth.login.data.di

import com.skinny.ordermanagement.features.auth.login.data.datasource.AuthLoginApiService
import com.skinny.ordermanagement.features.auth.login.data.datasource.LoginRemoteDataSource
import com.skinny.ordermanagement.features.auth.login.data.datasource.LoginRemoteDataSourceImpl
import com.skinny.ordermanagement.features.auth.login.data.repository.LoginRepositoryImpl
import com.skinny.ordermanagement.features.auth.login.domain.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideAuthLoginApiService(retrofit: Retrofit): AuthLoginApiService {
        return retrofit.create(AuthLoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRemoteDataSource(
        apiService: AuthLoginApiService
    ): LoginRemoteDataSource {
        return LoginRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        remoteDataSource: LoginRemoteDataSource
    ): LoginRepository {
        return LoginRepositoryImpl(remoteDataSource)
    }
}

