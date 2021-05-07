package com.nasa.app.ui.fragments.fragment_search_settings.di

import com.nasa.app.ui.fragments.fragment_search_settings.SearchSettingsFragment
import dagger.Subcomponent


@Subcomponent
interface SearchSettingsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchSettingsComponent
    }

    fun inject(searchSettingsFragment: SearchSettingsFragment)
}