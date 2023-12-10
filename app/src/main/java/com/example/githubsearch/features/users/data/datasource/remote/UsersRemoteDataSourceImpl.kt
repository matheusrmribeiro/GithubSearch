package com.example.githubsearch.features.users.data.datasource.remote

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.core.network.doRequest
import com.example.githubsearch.features.users.data.datasource.IUsersApiService
import com.example.githubsearch.features.users.data.models.UserResponse
import javax.inject.Inject

class UsersRemoteDataSourceImpl @Inject constructor(
    private val usersService: IUsersApiService
) : IUsersRemoteDataSource {
    override suspend fun getUsers(query: String?): ResponseWrapper<List<UserResponse>> {
        return if (query == null) {
            doRequest {
                usersService.getAllUsers()
            }
        } else {
            when (val response = doRequest { usersService.getSearchUsers(query) }) {
                is ResponseWrapper.Error -> ResponseWrapper.Error(0)

                is ResponseWrapper.Success -> {
                    ResponseWrapper.Success(response.result?.items ?: listOf())
                }
            }
        }
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserResponse> {
        return doRequest {
            usersService.getUserByUserName(userName)
        }
    }
}