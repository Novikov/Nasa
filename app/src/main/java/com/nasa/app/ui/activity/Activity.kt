package com.nasa.app.ui.activity

interface Activity {
    fun showProgressBar()
    fun hideProgressBar()
    fun searchRequest(query: String)
    fun collapseSearchField()
    fun showErrorMessage(msg: String)
    fun clearErrorMessage()
    fun hideActionBar()
    fun showActionBar()
    fun isErrorMessageShoved():Boolean
}