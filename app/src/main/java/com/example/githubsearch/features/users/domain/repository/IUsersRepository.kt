package com.example.githubsearch.features.users.domain.repository

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.domain.entities.UserEntity

interface IUsersRepository {
    suspend fun getUsers(query: String?) : ResponseWrapper<List<UserEntity>>

    suspend fun getUserByUserName(userName: String) : ResponseWrapper<UserEntity>

}