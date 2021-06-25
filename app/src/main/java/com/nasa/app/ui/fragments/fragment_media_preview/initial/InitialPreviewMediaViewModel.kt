package com.nasa.app.ui.fragments.fragment_media_preview.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.fragment_media_preview.initial.di.InitialMediaPreviewsCompositeDisposable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class InitialPreviewMediaViewModel @Inject constructor(
    val mediaRepositoryInitial: InitialPreviewsPagedListRepository,
    @InitialMediaPreviewsCompositeDisposable val compositeDisposable: CompositeDisposable
) : ViewModel(){

    val initialMediaPreviews: LiveData<PagedList<MediaPreview>> by lazy {
        mediaRepositoryInitial.fetchLiveMediaPreviewPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepositoryInitial.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return initialMediaPreviews.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}