package com.nasa.app.ui

interface IActivity {
    fun showProgressBar()
    fun hideProgressBar()
    fun showErrorDialog(msg: String)
    fun searchRequest(query:String)
    fun collapseSearchField()
}