package com.example.githubsearch.features.users.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubsearch.core.utils.ViewState
import com.example.githubsearch.features.users.data.repository.UsersRepositoryImplMock
import com.example.githubsearch.features.users.domain.entities.UserCompleteEntity
import com.example.githubsearch.getOrAwaitValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UsersDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mockUsersRepository = UsersRepositoryImplMock()
    private val mockViewModel: UsersDetailViewModel = UsersDetailViewModel(mockUsersRepository)
    private val userName = "matheusrmribeiro"


    @Test
    fun `Test - Verificando se esta carregando`() = runTest {
        setIsSuccessResult(true)
        mockViewModel.userName = userName
        val response = mockViewModel.fetchUserByUserName().getOrAwaitValue()
        assert(response is ViewState.Loading)
    }

    @Test
    fun `Test - Verificando o retorno positivo do fetchUserByUserName`() = runTest {
        setIsSuccessResult(true)
        mockViewModel.userName = userName
        val response = mockViewModel.fetchUserByUserName().getOrAwaitValue(positionOfValueToBeCatch = 2)
        assert(response is ViewState.Success<UserCompleteEntity?>)
        val firstUser = (response as ViewState.Success<UserCompleteEntity?>).result
        Assert.assertEquals(firstUser?.name, "Matheus Miranda")
        Assert.assertEquals(firstUser?.userName, "matheusrmribeiro")
    }

    @Test
    fun `Test - Verificando o retorno negativo do fetchUserByUserName`() = runTest {
        setIsSuccessResult(false)
        mockViewModel.userName = userName
        val response = mockViewModel.fetchUserByUserName().getOrAwaitValue(positionOfValueToBeCatch = 2)
        assert(response is ViewState.Error)
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
