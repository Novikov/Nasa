package com.nasa.app.ui.fragments.fragment_media_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.di.FragmentScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class PreviewMediaViewModel @Inject constructor(
    private val mediaRepository: NewPreviewsPagedListRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val mediaPreviews: LiveData<PagedList<MediaPreview>> by lazy {
        mediaRepository.fetchLiveMediaPreviewPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}