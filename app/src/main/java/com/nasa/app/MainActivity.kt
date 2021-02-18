package com.nasa.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar

class MainActivity : AppCompatActivity(),IActivity {
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
}