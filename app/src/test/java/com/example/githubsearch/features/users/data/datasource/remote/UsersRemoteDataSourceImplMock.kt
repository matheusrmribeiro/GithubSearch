package com.example.githubsearch.features.users.data.datasource.remote

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.core.network.doRequest
import com.example.githubsearch.features.users.data.datasource.IUsersApiService
import com.example.githubsearch.features.users.data.models.UserRepositoryResponse
import com.example.githubsearch.features.users.data.models.UserBasicResponse
import com.example.githubsearch.features.users.data.models.UserCompleteResponse
import javax.inject.Inject

class UsersRemoteDataSourceImplMock() : IUsersRemoteDataSource {
    var isSuccess: Boolean = true
    override suspend fun getUsers(query: String?): ResponseWrapper<List<UserBasicResponse>> {
        return if (isSuccess)
            ResponseWrapper.Success(
                listOf(
                    UserBasicResponse(
                        name = "Matheus Miranda",
                        login = "matheusrmribeiro",
                        avatar_url = "https://url",
                        bio = "Android developer"
                    )
                )
            )
        else
            ResponseWrapper.Error(0)
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserCompleteResponse> {
        return if (isSuccess)
            ResponseWrapper.Success(
                UserCompleteResponse(
                    name = "Matheus Miranda",
                    login = "matheusrmribeiro",
                    avatar_url = "https://url",
                    bio = "Android developer",
                    followers = 1,
                    following = 2
                )
            )
        else
            ResponseWrapper.Error(0)
    }

    override suspend fun getUserRepositories(userName: String): ResponseWrapper<List<UserRepositoryResponse>> {
        return if (isSuccess)
            ResponseWrapper.Success(
                listOf(
                    UserRepositoryResponse(
                        name = "Github Search",
                        description = "An application to list github users and their repositories",
                        html_url = "https://url"
                    )
                )
            )
        else
            ResponseWrapper.Error(0)
    }
}