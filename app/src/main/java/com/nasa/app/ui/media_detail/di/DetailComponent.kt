package com.nasa.app.ui.media_detail.di

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.media_preview.PreviewMediaFragment
import dagger.Subcomponent

@DetailScope
@Subcomponent(modules = [])
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailComponent
    }

    fun inject(previewMediaFragment: PreviewMediaFragment)
}