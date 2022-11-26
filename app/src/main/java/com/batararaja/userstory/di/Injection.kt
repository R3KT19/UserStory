package com.batararaja.userstory.di

import android.content.Context
import com.batararaja.userstory.api.ApiConfig
import com.batararaja.userstory.data.StoryRepository
import com.batararaja.userstory.database.StoryDatabase
import com.batararaja.userstory.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(database, apiService, appExecutors)
    }
}