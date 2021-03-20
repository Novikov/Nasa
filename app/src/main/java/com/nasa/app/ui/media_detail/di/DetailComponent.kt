package com.nasa.app.ui.media_detail.di

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.media_detail.AudioDetailFragment
import com.nasa.app.ui.media_detail.ImageDetailFragment
import com.nasa.app.ui.media_detail.VideoDetailFragment
import com.nasa.app.ui.media_preview.PreviewMediaFragment
import dagger.BindsInstance
import dagger.Subcomponent

@DetailScope
@Subcomponent(modules = [DetailModule::class,ViewModelFactoryModule::class, DetailMediaViewModulesModule::class])
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance nasaId:String): DetailComponent
    }

    fun inject(audioMediaFragment: AudioDetailFragment)
    fun inject(audioMediaFragment: ImageDetailFragment)
    fun inject(audioMediaFragment: VideoDetailFragment)
}