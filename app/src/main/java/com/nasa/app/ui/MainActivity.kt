package com.nasa.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import com.nasa.app.R

class MainActivity : AppCompatActivity(), IActivity {
    lateinit var mProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.progressBar)
    }

    override fun showProgressBar() {
        Log.e("ProgressBar", "Loading")
        mProgress.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        Log.e("ProgressBar", "Hide")
        mProgress.visibility = ProgressBar.INVISIBLE
    }

    override fun showErrorDialog(msg: String) {
        try {
            val errorDialogFragment = ErrorDialogFragment.newInstance(msg)
            errorDialogFragment.show(supportFragmentManager,"ErrorDialogFragment")
        }
        catch (ex: Exception){
            Log.e("MainActivity", ex.message.toString())
        }
    }


}