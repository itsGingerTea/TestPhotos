package com.example.unsplashapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashapp.data.models.Photo
import com.example.unsplashapp.data.network.ApiPhoto
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import javax.inject.Named

class PhotosPagingSource @AssistedInject constructor(
    @Named("base_api") private val api: ApiPhoto,
    @Assisted("query") private val query: String
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition((anchorPosition)) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = api.getPhotos(query, pageNumber, pageSize)

            return if (response.isSuccessful) {
                val photos = checkNotNull(response.body())
                val nextPageNumber = if (photos.isEmpty()) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber else null
                LoadResult.Page(photos, prevPageNumber, nextPageNumber)
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
        fun create(@Assisted("query") query: String): PhotosPagingSource
    }
}