package com.nasa.app.ui.fragments.fragments_media_detail.di

import com.nasa.app.ui.fragments.fragments_media_detail.DetailMediaViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface DetailViewModelAssistedFactory {
    fun create(nasaId: String): DetailMediaViewModel
}
