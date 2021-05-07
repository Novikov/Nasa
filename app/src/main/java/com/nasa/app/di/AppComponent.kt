package com.nasa.app.di

import com.nasa.app.ui.activity.di.ActivityComponent
import com.nasa.app.ui.fragments.fragment_download_files.di.DownloadFilesComponent
import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailComponent
import com.nasa.app.ui.fragments.fragment_media_preview.di.PreviewComponent
import com.nasa.app.ui.fragments.fragment_search_settings.di.SearchSettingsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SubcomponentsModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun getActivityComponent():ActivityComponent.Factory
}