package com.nasa.app.ui.fragments.fragment_media_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.NewPreviewsMediaDataSource
import com.nasa.app.data.repository.NewPreviewsMediaDataSourceFactory
import com.nasa.app.ui.fragments.di.FragmentScope
import com.nasa.app.utils.POST_PER_PAGE
import javax.inject.Inject

@FragmentScope
class NewPreviewsPagedListRepository @Inject constructor(
    private val previewsDataSourceFactory: NewPreviewsMediaDataSourceFactory
) {
    lateinit var previewsPagedList: LiveData<PagedList<MediaPreview>>

    fun fetchLiveMoviePagedList(): LiveData<PagedList<MediaPreview>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        previewsPagedList = LivePagedListBuilder(previewsDataSourceFactory, config).build()

        return previewsPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<NewPreviewsMediaDataSource, NetworkState>(
            previewsDataSourceFactory.previewsLiveDataSource,
            NewPreviewsMediaDataSource::networkState
        )
    }
}