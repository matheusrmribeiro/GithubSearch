package com.example.githubsearch.features.users.domain.repository

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.models.UserRepositoryResponse
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity

interface IUsersRepository {
    suspend fun getUsers(query: String?) : ResponseWrapper<List<UserBasicEntity>>

    suspend fun getUserByUserName(userName: String) : ResponseWrapper<UserCompleteEntity>

    suspend fun getUserRepositories(userName: String) : ResponseWrapper<List<UserRepositoryEntity>>

}