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
        return doRequest {
            if (query == null) {
                usersService.getAllUsers()
            } else
                usersService.getSearchUsers(query)
        }
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserResponse> {
        return doRequest {
            usersService.getUserByUserName(userName)
        }
    }
}