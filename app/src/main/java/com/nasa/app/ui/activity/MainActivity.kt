package com.nasa.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.nasa.app.R
import com.nasa.app.ui.activity.di.ActivityComponent
import com.nasa.app.ui.fragments.fragment_media_preview.initial.InitialPreviewMediaFragmentDirections
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent


class MainActivity : AppCompatActivity(), Activity {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface ActivityEntryPoint {
        fun activityComponent(): ActivityComponent.Factory
    }

    lateinit var activityComponent:ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        val entryPoint = EntryPointAccessors.fromApplication(applicationContext, ActivityEntryPoint::class.java)
        activityComponent = entryPoint.activityComponent().create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun isActionBarShowing(): Boolean {
        return  supportActionBar?.isShowing!!
    }

    override fun searchRequest() {
        val navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        if(navController.currentDestination?.id != R.id.mediaFragment){
            navController.navigateUp()
        }
        navController.navigate(InitialPreviewMediaFragmentDirections.actionMediaFragmentToFoundPreviewMediaFragment())
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}