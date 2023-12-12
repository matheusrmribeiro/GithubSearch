package com.example.githubsearch.features.users.presentation.listing

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.githubsearch.MainActivity
import com.example.githubsearch.R
import com.example.githubsearch.utils.RecyclerViewItemCountAssertion
import com.example.githubsearch.utils.searchViewAction
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UsersListFragmentTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_VerificandoCargaInicial() {
        onView(allOf(withId(R.id.rcv_users), isDisplayed())).check(RecyclerViewItemCountAssertion(moreThan = 1))
    }

    @Test
    fun test_VerificandoClickNaCelula() {
        onView(withId(R.id.rcv_users))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                    1,
                    click()
                )
            )
        onView(withId(R.id.ctl_page)).check(matches(isDisplayed()))
    }

    @Test
    fun test_VerificandoBusca() {
        onView(withId(R.id.src_search)).perform(searchViewAction("matheusrmribeiro"))
        Thread.sleep(2000)
        onView(allOf(withId(R.id.rcv_users), isDisplayed())).check(RecyclerViewItemCountAssertion(expectedCount = 1))
    }

}