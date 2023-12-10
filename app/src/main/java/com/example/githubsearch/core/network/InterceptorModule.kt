package com.example.githubsearch.core.network


import android.os.Build
import com.example.githubsearch.BuildConfig
import com.example.githubsearch.CoreApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InterceptorModule {

    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    @Named("HeadersInterceptor")
    fun provideHeadersInterceptor(): Interceptor {

        return Interceptor { chain ->
            val request = chain.request()
            val userAgent = StringBuilder("Android ").append(Build.VERSION.SDK_INT).toString()
            val appName =
                CoreApplication.instance.applicationInfo.loadLabel(CoreApplication.instance.packageManager)
                    .toString()

            val newRequest = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Api-Key", "")
                .addHeader("X-Amz-User-Agent", userAgent)
                .addHeader("X-App-Version", BuildConfig.VERSION_NAME)
                .addHeader("App-Name", appName)
                .build()
            return@Interceptor chain.proceed(newRequest)
        }
    }
}
