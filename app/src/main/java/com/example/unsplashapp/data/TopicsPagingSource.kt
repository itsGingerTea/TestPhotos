package com.example.unsplashapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashapp.data.models.Topic
import com.example.unsplashapp.data.network.ApiPhoto
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import javax.inject.Named

class TopicsPagingSource @AssistedInject constructor(
    @Named("base_api") private val api: ApiPhoto
) : PagingSource<Int, Topic>() {
    override fun getRefreshKey(state: PagingState<Int, Topic>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Topic> {
        try {
            val pageNumber: Int = params.key ?: 1
            val pageSize: Int = params.loadSize
            val response = api.getTopics(pageNumber, pageSize)

            return if (response.isSuccessful) {
                val topics = checkNotNull(response.body())
                val nextPageNumber = if (topics.isEmpty()) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                LoadResult.Page(topics, prevPageNumber, nextPageNumber)
            } else
                LoadResult.Error(HttpException(response))
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): TopicsPagingSource
    }
}