package com.nasa.app.ui.media_detail.di

import androidx.lifecycle.ViewModel
import com.nasa.app.di.view_models.ViewModelKey
import com.nasa.app.ui.media_detail.DetailMediaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailMediaViewModulesModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailMediaViewModel::class)
    abstract fun bindAuthViewModel(viewModel: DetailMediaViewModel): ViewModel?
}