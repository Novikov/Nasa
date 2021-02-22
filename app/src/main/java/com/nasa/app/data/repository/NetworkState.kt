package com.nasa.app.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED

}

class NetworkState(val status: Status, val msg: String) {

    companion object {

        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val NO_INTERNET:NetworkState
        val BAD_REQUEST:NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")

            LOADING = NetworkState(Status.RUNNING, "Running")

            ERROR = NetworkState(Status.FAILED, "Something went wrong")

            NO_INTERNET = NetworkState(Status.FAILED,"No internet connection")

            BAD_REQUEST = NetworkState(Status.FAILED, "Bad request")

            ENDOFLIST = NetworkState(Status.FAILED, "You have reached the end")
        }
    }
}