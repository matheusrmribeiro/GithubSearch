package com.example.githubsearch.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.`is`


class RecyclerViewItemCountAssertion(private val expectedCount: Int? = null, private val moreThan: Int? = null) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        if (expectedCount != null)
            assertThat(adapter!!.itemCount, `is`(expectedCount))

        if (moreThan != null)
            assert(adapter!!.itemCount > moreThan) {
                "Expected more than: $moreThan; Found: ${adapter!!.itemCount}"
            }
    }
}