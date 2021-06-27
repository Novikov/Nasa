package com.nasa.app.ui.fragments.fragment_media_preview.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.initial_media_previews.InitialPreviewsMediaDataSource
import com.nasa.app.data.repository.initial_media_previews.InitialPreviewsMediaDataSourceFactory
import com.nasa.app.utils.POST_PER_PAGE
import javax.inject.Inject


class InitialPreviewsPagedListRepository @Inject constructor(
    private val initialPreviewsDataSourceFactory: InitialPreviewsMediaDataSourceFactory
) {
    lateinit var initialPreviewsPagedList: LiveData<PagedList<MediaPreview>>

    fun fetchLiveMediaPreviewPagedList(): LiveData<PagedList<MediaPreview>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        initialPreviewsPagedList = LivePagedListBuilder(initialPreviewsDataSourceFactory, config).build()

        return initialPreviewsPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<InitialPreviewsMediaDataSource, NetworkState>(
            initialPreviewsDataSourceFactory.previewsLiveDataSource,
            InitialPreviewsMediaDataSource::networkState
        )
    }
}