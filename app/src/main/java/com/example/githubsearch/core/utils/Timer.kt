package com.example.githubsearch.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Timer
 * @property dispatcher: The dispatcher that will be used to run the timer
 */
class Timer(private val dispatcher: CoroutineDispatcher = Dispatchers.Main) {

    private var timerJob: Job? = null

    /**
     * Schedule a timer
     * @param interval: timer interval
     * @param repeat: timer should repeat or execute one time
     * @param task: completition callback when timer completes
     */
    fun schedule(
        interval: Long,
        repeat: Boolean = false,
        task: (() -> Unit),
        onError: (() -> Unit)? = null
    ) {
        invalidate()
        timerJob = CoroutineScope(dispatcher).launch {
            while (repeat) {
                delay(interval)
                try {
                    task()
                } catch (e: Exception) {
                    onError?.invoke()
                }
            }
        }
        timerJob?.start()
    }

    /**
     * Stop the timer
     */
    fun invalidate() {
        timerJob?.cancel()
        timerJob = null
    }
}
