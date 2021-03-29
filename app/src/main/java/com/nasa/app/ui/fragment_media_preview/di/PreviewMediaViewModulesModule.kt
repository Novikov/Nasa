package com.nasa.app.ui.fragment_media_preview.di

import androidx.lifecycle.ViewModel
import com.nasa.app.di.view_models.ViewModelKey
import com.nasa.app.ui.fragment_media_preview.PreviewMediaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PreviewMediaViewModulesModule {

    @Binds
    @IntoMap
    @ViewModelKey(PreviewMediaViewModel::class)
    abstract fun bindAuthViewModel(viewModel: PreviewMediaViewModel): ViewModel?
}