package com.nasa.app.ui.fragments.fragment_media_preview.found

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.found_media_previews.FoundPreviewsMediaDataSource
import com.nasa.app.data.repository.found_media_previews.FoundPreviewsMediaDataSourceFactory
import com.nasa.app.ui.fragments.di.FragmentScope
import com.nasa.app.utils.POST_PER_PAGE
import javax.inject.Inject

@FragmentScope
class FoundPreviewsPagedListRepository @Inject constructor(
    private val foundPreviewsDataSourceFactory: FoundPreviewsMediaDataSourceFactory
) {
    lateinit var foundPreviewsPagedList: LiveData<PagedList<MediaPreview>>

    fun fetchLiveMediaPreviewPagedList(): LiveData<PagedList<MediaPreview>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        foundPreviewsPagedList = LivePagedListBuilder(foundPreviewsDataSourceFactory, config).build()

        return foundPreviewsPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<FoundPreviewsMediaDataSource, NetworkState>(
            foundPreviewsDataSourceFactory.previewsLiveDataSource,
            FoundPreviewsMediaDataSource::networkState
        )
    }
}