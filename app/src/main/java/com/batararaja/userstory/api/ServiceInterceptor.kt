package com.batararaja.userstory.api

import androidx.viewbinding.BuildConfig
import com.batararaja.userstory.Preferences
import com.batararaja.userstory.ui.activity.ListStoryActivity
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class ServiceInterceptor : Interceptor {

    private val token = Preferences(ListStoryActivity.context).getToken()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if(request.header("No-Authentication") == null){

            if (request.url.toString().contains("/register") == false) {
                request = token
                    .takeUnless { it.isNullOrEmpty() }
                    ?.let {
                        request.newBuilder()
                            .addHeader("Authorization", "Bearer $it")
                            .build()
                    }
                    ?: request
            }
        }
        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return chain.proceed(request)
    }
}