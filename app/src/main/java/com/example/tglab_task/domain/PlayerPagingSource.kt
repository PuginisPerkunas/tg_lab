package com.example.tglab_task.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tglab_task.di.TGLabApi
import com.example.tglab_task.models.PlayerData
import timber.log.Timber

private const val STARTING_PAGE_INDEX = 1

class PlayerPagingSource(
    private val api: TGLabApi,
    private val query: String
) : PagingSource<Int, PlayerData>() {

    override fun getRefreshKey(state: PagingState<Int, PlayerData>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayerData> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = api.getPlayerInfo(
                query = query,
                page = page
            )
            val endOfPaginationReached = response.playerData.isEmpty()
            if (endOfPaginationReached) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = response.playerData,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (page == response.meta.total_pages) null else page + 1
                )
            }
        } catch (exception: Exception) {
            Timber.e(exception.message)
            LoadResult.Error(exception)
        }
    }
}