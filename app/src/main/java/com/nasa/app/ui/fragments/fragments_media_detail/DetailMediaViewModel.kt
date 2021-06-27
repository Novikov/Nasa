package com.nasa.app.ui.fragments.fragments_media_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.fragments_media_detail.di.MediaDetailsCompositeDisposable
import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

class DetailMediaViewModel @AssistedInject constructor(
    private val mediaRepository: DetailMediaRepository,
    @Named("Media Details Composite Disposable") private val compositeDisposable: CompositeDisposable,
    @Assisted private val nasaId: String
) : ViewModel(){

    class Factory(
        private val assistedFactory: DetailViewModelAssistedFactory,
        private val nasaId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(nasaId) as T
        }
    }

    val mediaDetails: LiveData<MediaDetailResponse> by lazy {
        mediaRepository.getMediaDetail(nasaId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepository.getMediaDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}