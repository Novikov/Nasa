package com.nasa.app.ui.activity.di

import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragments_media_detail.AudioDetailFragment
import com.nasa.app.ui.fragments_media_detail.ImageDetailFragment
import com.nasa.app.ui.fragments_media_detail.VideoDetailFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named


@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}