package com.example.githubsearch.features.users.data.datasource.remote

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.models.UserResponse

interface IUsersRemoteDataSource {

    suspend fun getUsers(query: String?) : ResponseWrapper<List<UserResponse>>

    suspend fun getUserByUserName(userName: String) : ResponseWrapper<UserResponse>

}