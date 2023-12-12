package com.example.githubsearch.features.users.presentation.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.features.users.data.repository.UsersRepositoryImplMock
import com.example.githubsearch.features.users.domain.entities.UserBasicEntity
import com.example.githubsearch.getOrAwaitValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UsersListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mockUsersRepository = UsersRepositoryImplMock()
    private val mockViewModel: UsersListViewModel = UsersListViewModel(mockUsersRepository)
    private val userName = "Nadir Ribeiro"

    @Test
    fun `Test - Verificando se esta carregando`() = runTest {
        setIsSuccessResult(true)
        val response = mockViewModel.fetchUsers().getOrAwaitValue()
        assert(response is ViewState.Loading)
    }

    @Test
    fun `Test - Verificando o retorno positivo do fetchUsers`() = runTest {
        setIsSuccessResult(true)
        val response = mockViewModel.fetchUsers().getOrAwaitValue(positionOfValueToBeCatch = 2)
        assert(response is ViewState.Success<List<UserBasicEntity>>)
        val firstUser = (response as ViewState.Success<List<UserBasicEntity>>).result[0]
        assertEquals(firstUser.name, "Matheus Miranda")
        assertEquals(firstUser.userName, "matheusrmribeiro")
    }

    @Test
    fun `Test - Verificando o retorno negativo do fetchUsers`() = runTest {
        setIsSuccessResult(false)
        val response = mockViewModel.fetchUsers().getOrAwaitValue(positionOfValueToBeCatch = 2)
        assert(response is ViewState.Error)
    }

    @Test
    fun `Test - Verificando o retorno negativo do setSearchQuery`() = runTest {
        setIsSuccessResult(true)
        mockViewModel.setSearchQuery(userName)
        val response = mockViewModel.fetchUsers().getOrAwaitValue(positionOfValueToBeCatch = 2)
        assert(response is ViewState.Success<List<UserBasicEntity>>)
        val firstUser = (response as ViewState.Success<List<UserBasicEntity>>).result[0]
        assertEquals(firstUser.name, userName)
        assertEquals(firstUser.userName, "nadribeiro")
    }

    /**
     * Auxiliary functions
     */
    private fun setIsSuccessResult(value: Boolean) {
        mockUsersRepository.isSuccess = value
    }

    companion object {
        private val testDispatcher = UnconfinedTestDispatcher()

        @JvmStatic
        @AfterClass
        fun close(): Unit {
            Dispatchers.shutdown()
        }

        @JvmStatic
        @BeforeClass
        fun setup() {
            Dispatchers.setMain(testDispatcher)
        }

    }
}
