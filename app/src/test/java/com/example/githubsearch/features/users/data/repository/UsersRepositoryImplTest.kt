package com.example.githubsearch.features.users.data.repository

import com.example.githubsearch.core.network.ResponseWrapper
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.features.users.domain.entities.UserRepositoryEntity
import com.example.githubsearch.features.users.domain.repository.IUsersRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UsersRemoteDataSourceImplTest {

    private val mockUsersRepository: IUsersRepository = UsersRepositoryImplMock()
    private val userName = "matheusrmribeiro"

    @Test
    fun `Test - Verificando o retorno positivo do getUsers`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRepository.getUsers(null)
        val firstUser = response.result?.getOrNull(0)
        assert(response is ResponseWrapper.Success<List<UserBasicEntity>>)
        assertEquals(response.result?.size, 1)
        assertEquals(firstUser?.name, "Matheus Miranda")
        assertEquals(firstUser?.userName, "matheusrmribeiro")
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUsers`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRepository.getUsers(null)
        assert(response is ResponseWrapper.Error<List<UserBasicEntity>>)
    }

    @Test
    fun `Test - Verificando o retorno positivo do getUserByName`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRepository.getUserByUserName(userName)
        val user = response.result
        assert(response is ResponseWrapper.Success<UserCompleteEntity>)
        assertEquals(user?.name, "Matheus Miranda")
        assertEquals(user?.userName, userName)
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUserByName`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRepository.getUserByUserName(userName)
        assert(response is ResponseWrapper.Error<UserCompleteEntity>)
    }

    @Test
    fun `Test - Verificando o retorno positivo do getUserRepositories`() = runTest {
        setIsSuccessResult(true)
        val response = mockUsersRepository.getUserRepositories(userName)
        val firstUser = response.result?.getOrNull(0)
        assert(response is ResponseWrapper.Success<List<UserRepositoryEntity>>)
        assertEquals(response.result?.size, 1)
        assertEquals(firstUser?.name, "Github Search")
        assertEquals(
            firstUser?.description,
            "An application to list github users and their repositories"
        )
    }

    @Test
    fun `Test - Verificando o retorno negativo do getUserRepositories`() = runTest {
        setIsSuccessResult(false)
        val response = mockUsersRepository.getUserRepositories(userName)
        assert(response is ResponseWrapper.Error<List<UserRepositoryEntity>>)
    }

    /**
     * Auxiliary functions
     */
    private fun setIsSuccessResult(value: Boolean) {
        (mockUsersRepository as UsersRepositoryImplMock).isSuccess = value
    }
}
