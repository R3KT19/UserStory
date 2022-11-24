package com.batararaja.userstory.api

import com.batararaja.userstory.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(
        @Body registerInfo: RegisterInfo
    ) : Call<RegisterResponse>

    @POST("login")
    fun login(
        @Body loginInfo: LoginInfo
    ) : Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : StoryResponse

    @GET("stories")
    fun getStoriesMap(
        @Query("location") location : Int
    ) : Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id : String
    ) : Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}