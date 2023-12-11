package com.example.githubsearch.features.users.domain.entities

import com.example.githubsearch.features.users.data.models.UserRepositoryResponse

class UserRepositoryEntity(
    val name: String,
    val description: String?,
    val url: String,
) {
    companion object {
        fun mapper(response: UserRepositoryResponse) = UserRepositoryEntity(
            name = response.name,
            description = response.description,
            url = response.html_url,
        )
        fun mapper(response: List<UserRepositoryResponse>) = response.map { mapper(it) }

    }
}