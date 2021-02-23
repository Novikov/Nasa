package com.nasa.app.ui

interface IActivity {
    fun showProgressBar()
    fun hideProgressBar()
    fun searchRequest(query: String)
    fun collapseSearchField()
    fun showMsg(msg: String)
    fun clearMsg()
    fun hideActionBar()
    fun showActionBar()
}