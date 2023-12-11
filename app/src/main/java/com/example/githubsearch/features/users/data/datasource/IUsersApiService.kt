package com.example.githubsearch.features.users.data.datasource

import com.example.githubsearch.features.users.data.models.UserBasicResponse
import com.example.githubsearch.features.users.data.models.UserCompleteResponse
import com.example.githubsearch.features.users.data.models.UserSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IUsersApiService {

    @GET("users")
    suspend fun getAllUsers(): Response<List<UserBasicResponse>>

    @GET("search/users")
    suspend fun getSearchUsers(
        @Query("q") query: String
    ): Response<UserSearchResponse>

    @GET("users/{userName}")
    suspend fun getUserByUserName(
        @Path("userName") userName: String
    ): Response<UserCompleteResponse>
}