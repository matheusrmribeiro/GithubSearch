package com.example.githubsearch.features.users.data.repository

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity
import com.example.githubsearch.features.users.domain.repository.IUsersRepository

class UsersRepositoryImplMock() : IUsersRepository {
    var isSuccess: Boolean = true
    override suspend fun getUsers(query: String?): ResponseWrapper<List<UserBasicEntity>> {
        return if (isSuccess)
            ResponseWrapper.Success(
                listOf(
                    UserBasicEntity(
                        name = "Matheus Miranda",
                        userName = "matheusrmribeiro",
                        picture = "https://url",
                        bio = "Android developer"
                    )
                )
            )
        else
            ResponseWrapper.Error(0)
    }

    override suspend fun getUserByUserName(userName: String): ResponseWrapper<UserCompleteEntity> {
        return if (isSuccess)
            ResponseWrapper.Success(
                UserCompleteEntity(
                    name = "Matheus Miranda",
                    userName = "matheusrmribeiro",
                    picture = "https://url",
                    bio = "Android developer",
                    followers = 1,
                    following = 2
                )
            )
        else
            ResponseWrapper.Error(0)
    }

    override suspend fun getUserRepositories(userName: String): ResponseWrapper<List<UserRepositoryEntity>> {
        return if (isSuccess)
            ResponseWrapper.Success(
                listOf(
                    UserRepositoryEntity(
                        name = "Github Search",
                        description = "An application to list github users and their repositories",
                        url = "https://url"
                    )
                )
            )
        else
            ResponseWrapper.Error(0)
    }
}