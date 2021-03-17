package com.nasa.app.di

import com.nasa.app.ui.media_preview.PreviewMediaFragment
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(previewMediaFragment: PreviewMediaFragment?)
}