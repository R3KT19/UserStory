package com.batararaja.userstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.batararaja.userstory.*
import com.batararaja.userstory.api.ApiConfig
import com.batararaja.userstory.api.ApiService
import com.batararaja.userstory.api.entity.StoryEntity
import com.batararaja.userstory.database.StoryDatabase
import com.batararaja.userstory.utils.AppExecutors
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository(private val storyDatabase: StoryDatabase,
                      private val apiService: ApiService,
                      private val appExecutors: AppExecutors ) {

    private val registerData = MediatorLiveData<Result<RegisterResponse>>()
    private val loginData = MediatorLiveData<Result<LoginResponse>>()
    private val storyData = MediatorLiveData<Result<PagingData<StoryEntity>>>()
    private val storyDataMap = MediatorLiveData<Result<StoryResponse>>()
    private val uploadData = MediatorLiveData<Result<FileUploadResponse>>()

    fun getStory(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        registerData.value = Result.Loading
        val registerInfo = RegisterInfo(name, email, password)
        val registerResponse = MutableLiveData<RegisterResponse>()
        val client = apiService.register(registerInfo)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    registerResponse.value = response.body()
                }else{
                    registerData.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerData.value = Result.Error(t.message.toString())
            }
        })
        registerData.addSource(registerResponse){data : RegisterResponse->
            registerData.value = Result.Success(data)
        }
        return registerData
    }

    fun login(email: String, password: String) :  LiveData<Result<LoginResponse>>{
        loginData.value = Result.Loading
        val registerInfo = LoginInfo(email, password)
        val loginResponse = MutableLiveData<LoginResponse>()
        val client = apiService.login(registerInfo)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful){
                    loginResponse.value = response.body()
                }else{
                    loginData.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginData.value = Result.Error(t.message.toString())
            }
        })
        loginData.addSource(loginResponse){data : LoginResponse->
            loginData.value = Result.Success(data)
        }
        return loginData
    }

    fun getStoryMap() :  LiveData<Result<StoryResponse>> {
        storyDataMap.value = Result.Loading
        val storyResponse = MutableLiveData<StoryResponse>()
        val client = apiService.getStoriesMap(1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful){
                    storyResponse.value = response.body()
                }else{
                    storyDataMap.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                storyDataMap.value = Result.Error(t.message.toString())
            }
        })
        storyDataMap.addSource(storyResponse){data : StoryResponse->
            storyDataMap.value = Result.Success(data)
        }
        return storyDataMap
    }

    fun uploadImage(photo : File?, desc : String, lat : Double?, lon : Double?) : LiveData<Result<FileUploadResponse>> {
        uploadData.value = Result.Loading
        val file = reduceFileImage(photo as File)
        val uploadResponse = MutableLiveData<FileUploadResponse>()
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        if (lat !=null && lon !=null) {
            val latitude = lat.toString().toRequestBody("text/plain".toMediaType())
            val longitude = lon.toString().toRequestBody("text/plain".toMediaType())
            val client = ApiConfig.getApiService().uploadImage(imageMultipart, description, latitude, longitude)
            client.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            uploadResponse.value = response.body()
                        }
                    } else {
                        uploadData.value = Result.Error(response.message().toString())
                    }
                }
                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    uploadData.value = Result.Error(t.message.toString())
                }
            })
        } else {
            val client = ApiConfig.getApiService().uploadImage(imageMultipart, description, null, null)

            client.enqueue(object : Callback<FileUploadResponse> {
                override fun onResponse(
                    call: Call<FileUploadResponse>,
                    response: Response<FileUploadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            uploadResponse.value = response.body()
                        }
                    } else {
                        uploadData.value = Result.Error(response.message().toString())
                    }
                }
                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    uploadData.value = Result.Error(t.message.toString())
                }
            })
        }

        uploadData.addSource(uploadResponse){data : FileUploadResponse->
            uploadData.value = Result.Success(data)
        }
        return uploadData
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            appExecutors: AppExecutors
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService, appExecutors)
            }.also { instance = it }

        private const val TAG = "Repository"
    }
}