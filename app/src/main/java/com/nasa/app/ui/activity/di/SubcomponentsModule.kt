package com.nasa.app.ui.activity.di

import com.nasa.app.ui.fragments.fragment_download_files.di.DownloadFilesComponent
import com.nasa.app.ui.fragments.fragment_media_preview.di.PreviewComponent
import com.nasa.app.ui.fragments.fragment_search_settings.di.SearchSettingsComponent
import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailComponent
import dagger.Module

@Module(subcomponents = [DownloadFilesComponent::class, PreviewComponent::class, SearchSettingsComponent::class, DetailComponent::class])
class SubcomponentsModule