package com.nasa.app.di

import com.nasa.app.ui.activity.di.ActivityComponent
import com.nasa.app.ui.fragment_download_files.di.DownloadFilesComponent
import com.nasa.app.ui.fragments_media_detail.di.DetailComponent
import com.nasa.app.ui.fragment_media_preview.di.PreviewComponent
import com.nasa.app.ui.fragment_search_settings.di.SearchSettingsComponent
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
    fun getActivityComponent():ActivityComponent.Factory
    fun getSearchSettingsComponent():SearchSettingsComponent.Factory
    fun getDownloadFilesComponent():DownloadFilesComponent.Factory
}