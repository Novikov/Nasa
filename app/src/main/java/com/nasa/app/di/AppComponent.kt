package com.nasa.app.di

import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragment_search_settings.SearchSettingsFragment
import com.nasa.app.ui.fragments_media_detail.di.DetailComponent
import com.nasa.app.ui.fragment_media_preview.di.PreviewComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun getPreviewComponent(): PreviewComponent.Factory
    fun getDetailComponent(): DetailComponent.Factory

    fun inject(mainActivity: MainActivity)
    fun inject(searchSettingsFragment: SearchSettingsFragment)
}