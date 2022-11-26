package com.batararaja.userstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.batararaja.userstory.adapter.StoryListAdapter
import com.batararaja.userstory.api.entity.StoryEntity
import com.batararaja.userstory.data.StoryRepository
import com.batararaja.userstory.utlis.Dummy
import com.batararaja.userstory.utlis.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStoryMap = Dummy.generateDummyStoryResponse()
    private val dummyLogin = Dummy.generateDummyLoginResponse()
    private val dummyRegister = Dummy.generateDummyRegisterResponse()
    private val dummyStory = Dummy.generateDummyStory()
    private val dummyUpload = Dummy.generateDummyUpload()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        `when`(storyRepository.getStory()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualQuote: PagingData<StoryEntity> = mainViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `when Get Story with Location should not null and Return Success`() {
        val expectedStory = MutableLiveData<Result<StoryResponse>>()
        expectedStory.value = Result.Success(dummyStoryMap)
        `when`(storyRepository.getStoryMap()).thenReturn(expectedStory)

        val actualStory = mainViewModel.getStoryMap().getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryMap()
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
        Assert.assertEquals(dummyStoryMap.listStory.size, (actualStory as Result.Success).data.listStory.size)
    }

    @Test
    fun `when Get Story with Location Network Error Should Return Error` () {
        val storyWithLocation = MutableLiveData<Result<StoryResponse>>()
        storyWithLocation.value = Result.Error("Error")
        `when`(storyRepository.getStoryMap()).thenReturn(storyWithLocation)

        val actualStory = mainViewModel.getStoryMap().getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryMap()
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }

    @Test
    fun `when Login should not null and Return Success` () {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLogin)
        `when`(storyRepository.login("batararaja19@gmail.com", "batara")).thenReturn(expectedLogin)

        val actualLogin = mainViewModel.login("batararaja19@gmail.com", "batara").getOrAwaitValue()
        Mockito.verify(storyRepository).login("batararaja19@gmail.com", "batara")
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
        Assert.assertEquals(dummyLogin.loginResult, (actualLogin as Result.Success).data.loginResult)
    }

    @Test
    fun `when Login with Network Error Should Return Error`() {
        val login = MutableLiveData<Result<LoginResponse>>()
        login.value = Result.Error("Error")
        `when`(storyRepository.login("batararaja19@gmail.com", "batara")).thenReturn(login)

        val actualLogin = mainViewModel.login("batararaja19@gmail.com", "batara").getOrAwaitValue()
        Mockito.verify(storyRepository).login("batararaja19@gmail.com", "batara")
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
    }

    @Test
    fun `when Register should not null and Return Success`() {
        val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
        expectedRegister.value = Result.Success(dummyRegister)
        `when`(storyRepository.register("test", "test123456789@gmail.com", "test123456789")).thenReturn(expectedRegister)

        val actualRegister = mainViewModel.register("test", "test123456789@gmail.com", "test123456789").getOrAwaitValue()
        Mockito.verify(storyRepository).register("test", "test123456789@gmail.com", "test123456789")
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Success)
        Assert.assertEquals(dummyRegister.error, (actualRegister as Result.Success).data.error)
    }

    @Test
    fun `when Register with network Error and Should Return Error` () {
        val register = MutableLiveData<Result<RegisterResponse>>()
        register.value = Result.Error("Error")
        `when`(storyRepository.register("test", "test123456789@gmail.com", "test123456789")).thenReturn(register)

        val actualRegister = mainViewModel.register("test", "test123456789@gmail.com", "test123456789").getOrAwaitValue()
        Mockito.verify(storyRepository).register("test", "test123456789@gmail.com", "test123456789")
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Error)
    }

    @Test
    fun `when Upload Story should not null and Return Success` () {
        val expectedUpload = MutableLiveData<Result<FileUploadResponse>>()
        expectedUpload.value = Result.Success(dummyUpload)
        `when`(storyRepository.uploadImage(File("Path/jpg"), "test", 0.0, 0.0)).thenReturn(expectedUpload)

        val actualUpload = mainViewModel.uploadImage(File("Path/jpg"), "test", 0.0, 0.0).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadImage(File("Path/jpg"), "test", 0.0, 0.0)
        Assert.assertNotNull(actualUpload)
        Assert.assertTrue(actualUpload is Result.Success)
        Assert.assertEquals(dummyUpload.error, (actualUpload as Result.Success).data.error)
    }

    @Test
    fun `when Upload Story with Network Error and Return Error` () {
        val upload = MutableLiveData<Result<FileUploadResponse>>()
        upload.value = Result.Error("Error")
        `when`(storyRepository.uploadImage(File("Path/jpg"), "test", 0.0, 0.0)).thenReturn(upload)

        val actualUpload = mainViewModel.uploadImage(File("Path/jpg"), "test", 0.0, 0.0).getOrAwaitValue()
        Mockito.verify(storyRepository).uploadImage(File("Path/jpg"), "test", 0.0, 0.0)
        Assert.assertNotNull(actualUpload)
        Assert.assertTrue(actualUpload is Result.Error)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}