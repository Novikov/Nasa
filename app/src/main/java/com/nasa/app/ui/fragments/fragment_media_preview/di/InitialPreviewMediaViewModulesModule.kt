package com.nasa.app.ui.fragments.fragment_media_preview.di

import androidx.lifecycle.ViewModel
import com.nasa.app.di.view_models.ViewModelKey
import com.nasa.app.ui.fragments.fragment_media_preview.found.FoundPreviewMediaViewModel
import com.nasa.app.ui.fragments.fragment_media_preview.initial.InitialPreviewMediaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InitialPreviewMediaViewModulesModule {

    @Binds
    @IntoMap
    @ViewModelKey(InitialPreviewMediaViewModel::class)
    abstract fun bindAuthViewModel(viewModelInitial: InitialPreviewMediaViewModel): ViewModel?
}