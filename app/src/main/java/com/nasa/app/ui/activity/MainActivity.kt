package com.nasa.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.ui.activity.di.ActivityComponent
import com.nasa.app.ui.fragments.fragment_media_preview.initial.InitialPreviewMediaFragmentDirections


class MainActivity : AppCompatActivity(), Activity {

    lateinit var activityComponent:ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as BaseApplication).appComponent.getActivityComponent().create()
        activityComponent.inject(this)
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