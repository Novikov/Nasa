package com.nasa.app.ui.activity.di

import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragments.fragment_download_files.di.DownloadFilesComponent
import com.nasa.app.ui.fragments.fragment_media_preview.di.PreviewComponent
import com.nasa.app.ui.fragments.fragment_search_settings.di.SearchSettingsComponent
import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailComponent
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)

    fun getPreviewComponent(): PreviewComponent.Factory
    fun getDetailComponent(): DetailComponent.Factory
    fun getSearchSettingsComponent(): SearchSettingsComponent.Factory
    fun getDownloadFilesComponent(): DownloadFilesComponent.Factory
}