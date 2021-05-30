package com.nasa.app.ui.activity

interface Activity {
    fun showProgressBar()
    fun hideProgressBar()
    fun searchRequest(query: String)
    fun showErrorMessage(msg: String)
    fun clearErrorMessage()
    fun isErrorMessageShoved():Boolean
}