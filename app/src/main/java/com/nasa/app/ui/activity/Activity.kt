package com.nasa.app.ui.activity

interface Activity {
    fun showActionBar()
    fun hideActionBar()
    fun isActionBarShowing():Boolean
    fun searchRequest()
}