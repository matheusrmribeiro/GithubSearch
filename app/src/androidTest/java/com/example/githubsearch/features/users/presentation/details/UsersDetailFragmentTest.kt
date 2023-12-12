package com.example.githubsearch.features.users.presentation.details

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubsearch.R
import com.example.githubsearch.utils.RecyclerViewItemCountAssertion
import com.example.githubsearch.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class UsersDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val userName = "matheusrmribeiro"

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_VerificandoUserInfos() {
        val fragmentArgs = bundleOf("userName" to userName)
        launchFragmentInHiltContainer<UsersDetailFragment>(fragmentArgs)
        Thread.sleep(2000)
        Espresso.onView(allOf(withId(R.id.txt_user_name))).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun test_VerificandoUsersRepositoryComItens() {
        val fragmentArgs = bundleOf("userName" to userName)
        launchFragmentInHiltContainer<UsersDetailFragment>(fragmentArgs)
        Thread.sleep(2000)
        Espresso.onView(
            allOf(
                withId(R.id.rcv_repositories),
                isDisplayed()
            )
        ).check(RecyclerViewItemCountAssertion(moreThan = 1))
    }

    @Test
    fun test_VerificandoUsersRepositorySemItens() {
        val fragmentArgs = bundleOf("userName" to "asdaasd")
        launchFragmentInHiltContainer<UsersDetailFragment>(fragmentArgs)
        Thread.sleep(2000)
        Espresso.onView(allOf(withId(R.id.txt_message))).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun test_VerificandoUsersRepositoryError() {
        val fragmentArgs = bundleOf("userName" to "asdaasd 123 1223")
        launchFragmentInHiltContainer<UsersDetailFragment>(fragmentArgs)
        Thread.sleep(2000)
        Espresso.onView(allOf(withId(R.id.txt_error_message))).check(ViewAssertions.matches(isDisplayed()))
    }

}