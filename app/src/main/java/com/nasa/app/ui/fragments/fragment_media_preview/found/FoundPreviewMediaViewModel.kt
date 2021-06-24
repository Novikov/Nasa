package com.nasa.app.ui.fragments.fragment_media_preview.found

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.di.FragmentScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class FoundPreviewMediaViewModel @Inject constructor(
     val mediaRepositoryFound: FoundPreviewsPagedListRepository,
    @Named("found media previews composite disposable") val compositeDisposable: CompositeDisposable
) {

    val foundMediaPreviews: LiveData<PagedList<MediaPreview>> by lazy {
        mediaRepositoryFound.fetchLiveMediaPreviewPagedList()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepositoryFound.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return foundMediaPreviews.value?.isEmpty() ?: true
    }

//    override fun onCleared() {
//        super.onCleared()
//        compositeDisposable.dispose()
//    }
}