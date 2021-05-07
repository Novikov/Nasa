package com.nasa.app.ui.fragments.fragment_media_preview.di

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.fragments.di.FragmentScope
import com.nasa.app.ui.fragments.fragment_media_preview.PreviewMediaFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ViewModelFactoryModule::class, PreviewMediaViewModulesModule::class, PreviewModule::class])
interface PreviewComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PreviewComponent
    }

    fun inject(previewMediaFragment: PreviewMediaFragment)
}