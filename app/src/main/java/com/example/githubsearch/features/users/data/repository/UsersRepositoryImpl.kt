package com.example.githubsearch.features.users.data.repository

import com.example.githubsearch.R
import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.datasource.remote.IUsersRemoteDataSource
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity
import com.example.githubsearch.features.users.domain.repository.IUsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: IUsersRemoteDataSource
) : IUsersRepository {
    override suspend fun getUsers(query: String?): ResponseWrapper<List<UserBasicEntity>> {
        return when (val response = remoteDataSource.getUsers(query)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(
                message = R.string.data_fetching_error
            )

            is ResponseWrapper.Success -> {
                val users = UserBasicEntity.mapper((response.result ?: listOf()))
                ResponseWrapper.Success(users)
            }
        }
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserCompleteEntity> {
        return when (val response = remoteDataSource.getUserByUserName(userName)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(
                message = R.string.data_fetching_error
            )

            is ResponseWrapper.Success -> {
                response.result?.let {
                    val user = UserCompleteEntity.mapper(response.result)
                    ResponseWrapper.Success(user)
                } ?: run {
                    ResponseWrapper.Error(
                        message = R.string.search_not_found
                    )
                }
            }
        }
    }

    override suspend fun getUserRepositories(userName: String): ResponseWrapper<List<UserRepositoryEntity>> {
        return when (val response = remoteDataSource.getUserRepositories(userName)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(
                message = R.string.data_fetching_error
            )

            is ResponseWrapper.Success -> {
                response.result?.let {
                    val user = UserRepositoryEntity.mapper(response.result)
                    ResponseWrapper.Success(user)
                } ?: run {
                    ResponseWrapper.Error(
                        message = R.string.users_detail_repositories_not_found
                    )
                }
            }
        }
    }
}