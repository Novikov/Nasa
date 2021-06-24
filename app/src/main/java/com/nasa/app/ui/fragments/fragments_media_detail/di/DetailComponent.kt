package com.nasa.app.ui.fragments.fragments_media_detail.di

import android.content.Context
import com.nasa.app.ui.fragments.di.FragmentScope
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

//@FragmentScope
//@Subcomponent(modules = [DetailModule::class, DetailMediaViewModulesModule::class])
//interface DetailComponent {
//
//    @Subcomponent.Factory
//    interface Factory {
//        fun create(@BindsInstance @Named("nasa id") nasaId: String, @BindsInstance context: Context): DetailComponent
//    }
//
//    fun inject(audioMediaFragment: AudioDetailFragment)
//    fun inject(imageMediaFragment: ImageDetailFragment)
//    fun inject(videoMediaFragment: VideoDetailFragment)
//}