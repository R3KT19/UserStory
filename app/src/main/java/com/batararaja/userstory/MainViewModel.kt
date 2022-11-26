package com.batararaja.userstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.batararaja.userstory.api.entity.StoryEntity
import com.batararaja.userstory.data.StoryRepository
import java.io.File

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

//    val story: LiveData<PagingData<StoryEntity>> =
//        storyRepository.getStory().cachedIn(viewModelScope)

    fun getStory() = storyRepository.getStory().cachedIn(viewModelScope)

    fun register(name: String, email: String, password: String) =
        storyRepository.register(name, email, password)

    fun login(email: String, password: String) =
        storyRepository.login(email, password)

    fun getStoryMap() = storyRepository.getStoryMap()

    fun uploadImage(photo : File?, desc : String, lat : Double?, lon : Double?) = storyRepository.uploadImage(photo, desc, lat, lon)

    companion object {
        private const val TAG = "MainViewModel"
    }
}