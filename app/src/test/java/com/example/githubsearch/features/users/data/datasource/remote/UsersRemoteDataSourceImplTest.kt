package com.example.githubsearch.features.users.data.datasource.remote

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.data.models.UserBasicResponse
import com.example.githubsearch.features.users.data.models.UserCompleteResponse
import com.example.githubsearch.features.users.data.models.UserRepositoryResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UsersRemoteDataSourceImplTest {

    private val mockUsersRemoteDataSource: IUsersRemoteDataSource = UsersRemoteDataSourceImplMock()
    private val userName = "matheusrmribeiro"

    @Test
    fun `Test - Verificando o retorno positivo do getUsers`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRemoteDataSource.getUsers(null)
        val firstUser = response.result?.getOrNull(0)
        assert(response is ResponseWrapper.Success<List<UserBasicResponse>>)
        assertEquals(response.result?.size, 1)
        assertEquals(firstUser?.name, "Matheus Miranda")
        assertEquals(firstUser?.login, "matheusrmribeiro")
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUsers`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRemoteDataSource.getUsers(null)
        assert(response is ResponseWrapper.Error<List<UserBasicResponse>>)
    }

    @Test
    fun `Test - Verificando o retorno positivo do getUserByName`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRemoteDataSource.getUserByUserName(userName)
        val user = response.result
        assert(response is ResponseWrapper.Success<UserCompleteResponse>)
        assertEquals(user?.name, "Matheus Miranda")
        assertEquals(user?.login, userName)
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUserByName`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRemoteDataSource.getUserByUserName(userName)
        assert(response is ResponseWrapper.Error<UserCompleteResponse>)
    }

    @Test
    fun `Test - Verificando o retorno positivo do getUserRepositories`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRemoteDataSource.getUserRepositories(userName)
        val firstUser = response.result?.getOrNull(0)
        assert(response is ResponseWrapper.Success<List<UserRepositoryResponse>>)
        assertEquals(response.result?.size, 1)
        assertEquals(firstUser?.name, "Github Search")
        assertEquals(firstUser?.description, "An application to list github users and their repositories")
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUserRepositories`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRemoteDataSource.getUserRepositories(userName)
        assert(response is ResponseWrapper.Error<List<UserRepositoryResponse>>)
    }

    /**
     * Auxiliary functions
     */
    private fun setIsSuccessResult(value: Boolean) {
        (mockUsersRemoteDataSource as UsersRemoteDataSourceImplMock).isSuccess = value
    }
}