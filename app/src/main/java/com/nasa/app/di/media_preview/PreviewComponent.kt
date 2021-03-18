package com.nasa.app.di.media_preview

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.media_preview.PreviewMediaFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelFactoryModule::class, PreviewMediaViewModulesModule::class])
interface PreviewComponent {
    fun inject(previewMediaFragment: PreviewMediaFragment)
}