package com.example.githubsearch.features.users.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.features.users.domain.repository.IUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val usersRepository: IUsersRepository
) : ViewModel() {

    private var searchQuery: String? = null

    fun setSearchQuery(query: String?) {
        searchQuery = query
    }

    fun fetchUsers() = flow {
        when (val response = usersRepository.getUsers(searchQuery)) {
            is ResponseWrapper.Success -> emit(ViewState.Success(response.result!!))
            is ResponseWrapper.Error -> emit(ViewState.Error("", response.message))
        }
    }
        .onStart { emit(ViewState.Loading) }
        .asLiveData()

}