package com.example.githubsearch.features.users.data.repository

import com.example.githubsearch.R
import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.datasource.remote.IUsersRemoteDataSource
import com.example.githubsearch.features.users.domain.entities.UserEntity
import com.example.githubsearch.features.users.domain.repository.IUsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: IUsersRemoteDataSource
) : IUsersRepository {
    override suspend fun getUsers(query: String?): ResponseWrapper<List<UserEntity>> {
        return when (val response = remoteDataSource.getUsers(query)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(
                message = R.string.data_fetching_error
            )

            is ResponseWrapper.Success -> {
                val users = UserEntity.mapper((response.result ?: listOf()))
                ResponseWrapper.Success(users)
            }
        }
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserEntity> {
        return when (val response = remoteDataSource.getUserByUserName(userName)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(
                message = R.string.data_fetching_error
            )

            is ResponseWrapper.Success -> {
                response.result?.let {
                    val user = UserEntity.mapper(response.result)
                    ResponseWrapper.Success(user)
                } ?: run {
                    ResponseWrapper.Error(
                        message = R.string.users_search_not_found
                    )
                }
            }
        }
    }
}