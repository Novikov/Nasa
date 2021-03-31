package com.nasa.app.ui.fragment_download_files.di

import android.content.Context
import com.nasa.app.ui.fragment_download_files.DownloadFilesFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface DownloadFilesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DownloadFilesComponent
    }

    fun inject(downloadFilesFragment: DownloadFilesFragment)
}