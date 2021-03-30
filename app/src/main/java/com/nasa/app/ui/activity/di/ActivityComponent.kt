package com.nasa.app.ui.activity.di

import com.nasa.app.ui.activity.MainActivity
import dagger.Subcomponent


@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
}