package com.nasa.app.di

import com.nasa.app.di.media_preview.PreviewComponent
import com.nasa.app.di.media_preview.PreviewMediaViewModulesModule
import com.nasa.app.di.view_models.ViewModelFactoryModule
import com.nasa.app.ui.media_preview.PreviewMediaFragment
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun getPreviewComponent():PreviewComponent
}