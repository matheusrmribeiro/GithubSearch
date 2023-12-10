package com.example.githubsearch.features.users.di

import com.example.githubsearch.features.users.data.datasource.IUsersApiService
import com.example.githubsearch.features.users.data.datasource.remote.IUsersRemoteDataSource
import com.example.githubsearch.features.users.data.datasource.remote.UsersRemoteDataSourceImpl
import com.example.githubsearch.features.users.data.repository.UsersRepositoryImpl
import com.example.githubsearch.features.users.domain.repository.IUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsersModule {

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): IUsersApiService =
        retrofit.create(IUsersApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        service: IUsersApiService
    ): IUsersRemoteDataSource = UsersRemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: IUsersRemoteDataSource,
    ): IUsersRepository = UsersRepositoryImpl(remoteDataSource)

}