package com.nasa.app.di

import android.content.Context
import com.nasa.app.ui.media_preview.di.PreviewComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun getPreviewComponent(): PreviewComponent.Factory
}