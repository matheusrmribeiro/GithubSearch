package com.example.githubsearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoreApplication: Application() {

    companion object {
        lateinit var instance: CoreApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}