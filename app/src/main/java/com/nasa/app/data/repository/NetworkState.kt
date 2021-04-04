package com.nasa.app.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        val LOADED: NetworkState
        val NOTHING_FOUND: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val TIMEOUT: NetworkState
        val NO_INTERNET: NetworkState
        val BAD_REQUEST: NetworkState
        val NOT_FOUND: NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            NOTHING_FOUND = NetworkState(Status.SUCCESS, "Nothing found")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong!")
            TIMEOUT = NetworkState(Status.FAILED, "Timeout error\n" + "This page isn't available!")
            BAD_REQUEST = NetworkState(Status.FAILED, "HTTP Error 400.\nThis page isn't working!")
            NOT_FOUND = NetworkState(Status.FAILED, "HTTP Error 404.\nThis page not found!")
            NO_INTERNET = NetworkState(Status.FAILED, "No internet connection!")
            ENDOFLIST = NetworkState(Status.FAILED, "You have reached the end!")
        }
    }
}