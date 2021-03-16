package com.nasa.app.ui

interface Activity {
    fun showProgressBar()
    fun hideProgressBar()
    fun searchRequest(query: String)
    fun collapseSearchField()
    fun showMsg(msg: String)
    fun clearMsg()
    fun hideActionBar()
    fun showActionBar()
}