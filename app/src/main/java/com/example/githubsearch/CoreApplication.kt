package com.example.githubsearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CoreApplication @Inject constructor(): Application() {

    companion object {
        lateinit var instance: CoreApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}