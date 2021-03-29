package com.nasa.app.ui.fragments_media_detail.di

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.fragments_media_detail.AudioDetailFragment
import com.nasa.app.ui.fragments_media_detail.ImageDetailFragment
import com.nasa.app.ui.fragments_media_detail.VideoDetailFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@DetailScope
@Subcomponent(modules = [DetailModule::class, ViewModelFactoryModule::class, DetailMediaViewModulesModule::class])
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @Named("nasa id") nasaId: String): DetailComponent
    }

    fun inject(audioMediaFragment: AudioDetailFragment)
    fun inject(imageMediaFragment: ImageDetailFragment)
    fun inject(videoMediaFragment: VideoDetailFragment)
}