package com.batararaja.userstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.batararaja.userstory.api.ApiService
import com.batararaja.userstory.api.entity.StoryEntity
import java.time.ZonedDateTime


class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(page, params.loadSize)

            val data = responseData.listStory.map {
                StoryEntity(
                    id = it.id,
                    name = it.name,
                    desc = it.description,
                    url = it.photoUrl,
                    date = it.createdAt,
                    lat = it.lat,
                    lon = it.lon
                )
            }

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}