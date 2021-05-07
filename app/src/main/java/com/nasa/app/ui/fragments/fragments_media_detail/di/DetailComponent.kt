package com.nasa.app.ui.fragments.fragments_media_detail.di

import android.content.Context
import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.fragments.di.FragmentScope
import com.nasa.app.ui.fragments.fragments_media_detail.AudioDetailFragment
import com.nasa.app.ui.fragments.fragments_media_detail.ImageDetailFragment
import com.nasa.app.ui.fragments.fragments_media_detail.VideoDetailFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@FragmentScope
@Subcomponent(modules = [DetailModule::class, ViewModelFactoryModule::class, DetailMediaViewModulesModule::class])
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @Named("nasa id") nasaId: String, @BindsInstance context: Context): DetailComponent
    }

    fun inject(audioMediaFragment: AudioDetailFragment)
    fun inject(imageMediaFragment: ImageDetailFragment)
    fun inject(videoMediaFragment: VideoDetailFragment)
}