package com.example.githubsearch.features.users.domain.entities

import com.example.githubsearch.features.users.data.models.UserResponse

data class UserEntity(
    val name: String?,
    val userName: String,
    val picture: String,
    val bio: String?
) {
    companion object {
        fun mapper(response: UserResponse) = UserEntity(
            name = response.name,
            userName = response.login,
            picture = response.avatar_url,
            bio = response.bio,
        )

        fun mapper(response: List<UserResponse>) = response.map { mapper(it) }

    }
}