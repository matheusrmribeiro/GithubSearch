package com.example.githubsearch.features.users.data.datasource.remote

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.models.UserBasicResponse
import com.example.githubsearch.features.users.data.models.UserCompleteResponse

interface IUsersRemoteDataSource {

    suspend fun getUsers(query: String?) : ResponseWrapper<List<UserBasicResponse>>

    suspend fun getUserByUserName(userName: String) : ResponseWrapper<UserCompleteResponse>

}