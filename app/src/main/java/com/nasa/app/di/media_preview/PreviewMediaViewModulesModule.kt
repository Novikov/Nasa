package com.nasa.app.di.media_preview

import androidx.lifecycle.ViewModel
import com.nasa.app.di.view_models.ViewModelKey
import com.nasa.app.ui.media_preview.PreviewMediaViewModel
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