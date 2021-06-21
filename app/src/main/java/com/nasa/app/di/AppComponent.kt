package com.nasa.app.di

import com.nasa.app.ui.activity.di.ActivityComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun getActivityComponent():ActivityComponent.Factory
}