package com.example.githubsearch.features.users.domain.repository

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity

interface IUsersRepository {

    /**
     * Get the users based on query search or all the users if it is null
     * @param [query] The query containing the user's name or user name.
     *
     * @return A list containing the basic information from all the returned users
     */
    suspend fun getUsers(query: String?) : ResponseWrapper<List<UserBasicEntity>>

    /**
     * Get the user according the given User Name.
     * @param [userName] The user name.
     *
     * @return All the information necessary about the user.
     */
    suspend fun getUserByUserName(userName: String) : ResponseWrapper<UserCompleteEntity>

    /**
     * Get the user's repositories according the given User Name.
     * @param [userName] The user name.
     *
     * @return A list containing the information from all user's repositories.
     */
    suspend fun getUserRepositories(userName: String) : ResponseWrapper<List<UserRepositoryEntity>>

}