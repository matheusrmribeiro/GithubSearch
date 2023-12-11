package com.example.githubsearch.features.users.domain.entities

import com.example.githubsearch.features.users.data.models.UserBasicResponse

data class UserBasicEntity(
    val name: String?,
    val userName: String,
    val picture: String,
    val bio: String?
) {
    companion object {
        fun mapper(response: UserBasicResponse) = UserBasicEntity(
            name = response.name,
            userName = response.login,
            picture = response.avatar_url,
            bio = response.bio,
        )

        fun mapper(response: List<UserBasicResponse>) = response.map { mapper(it) }

    }
}