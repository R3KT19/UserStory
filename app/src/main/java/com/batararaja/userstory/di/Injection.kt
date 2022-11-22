package com.batararaja.userstory.di

import android.content.Context
import com.batararaja.userstory.api.ApiConfig
import com.batararaja.userstory.database.StoryDatabase
import com.dicoding.myunlimitedquotes.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}