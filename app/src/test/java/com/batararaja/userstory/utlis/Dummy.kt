package com.batararaja.userstory.utlis

import androidx.lifecycle.LiveData
import com.batararaja.userstory.*
import com.batararaja.userstory.api.entity.StoryEntity

object Dummy {
    fun generateDummyStoryResponse(): StoryResponse {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val storyEntity = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1669461304770_0IgFpio8.jpg",
                "2022-11-26T11:15:04.770Z",
                "Testing",
                "Hanya testing",
                110.4518633,
                "story-$i",
                -7.7394217
            )
            storyList.add(storyEntity)
        }
        return StoryResponse(
            storyList,
            false,
            "Stories fetched successfully"
        )
    }

    fun generateDummyLoginResponse() : LoginResponse {
        val loginResult = LoginResult(
            userId = "user-h0bKXnyqDhDCAzzW",
            name = "batara",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWgwYktYbnlxRGhEQ0F6elciLCJpYXQiOjE2Njk0NzY0NjF9.d7AGJdAp3oUWj5-9-lmOOaBlb8ZbsshAUki-RhVBxjg"
        )
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = loginResult
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateDummyStory(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                id = "story-$i",
                name = "test-$i",
                desc = "Testing",
                url = "https://story-api.dicoding.dev/images/stories/photos-1669461304770_0IgFpio8.jpg",
                date = "2022-11-26T11:15:04.770Z",
                lat = 0.0,
                lon = 0.0
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyUpload() : FileUploadResponse {
        return FileUploadResponse(
            error = false,
            message = "Story created Successfully"
        )
    }
}