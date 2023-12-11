package com.example.githubsearch.features.users.data.models

class UserCompleteResponse(
    val name: String,
    val login: String,
    val avatar_url: String,
    val bio: String?,
    val followers: Int,
    val following: Int,
)