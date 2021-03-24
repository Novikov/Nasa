package com.nasa.app.di

import android.content.Context
import com.nasa.app.ui.MainActivity
import com.nasa.app.ui.SearchSettingsFragment
import com.nasa.app.ui.media_detail.di.DetailComponent
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
    fun getDetailComponent(): DetailComponent.Factory

    fun inject(mainActivity: MainActivity)
    fun inject(searchSettingsFragment: SearchSettingsFragment)
}