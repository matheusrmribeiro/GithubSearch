package com.example.githubsearch.features.users.domain.entities

import com.example.githubsearch.features.users.data.models.UserCompleteResponse

data class UserCompleteEntity(
    val name: String?,
    val userName: String,
    val picture: String,
    val bio: String?,
    val followers: Int,
    val following: Int,
) {
    companion object {
        fun mapper(response: UserCompleteResponse) = UserCompleteEntity(
            name = response.name ?: "",
            userName = response.login,
            picture = response.avatar_url,
            bio = response.bio,
            followers = response.followers,
            following = response.following,
        )

        fun mapper(response: List<UserCompleteResponse>) = response.map { mapper(it) }

    }
}