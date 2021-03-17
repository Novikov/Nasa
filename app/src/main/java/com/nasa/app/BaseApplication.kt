package com.nasa.app

import android.app.Application
import com.nasa.app.di.AppComponent
import com.nasa.app.di.DaggerAppComponent

class BaseApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}