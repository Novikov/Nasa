package com.nasa.app.ui

import com.nasa.app.data.model.ContentType
import com.nasa.app.utils.EMPTY_SEARCH
import com.nasa.app.utils.FIRST_PAGE
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchParams @Inject constructor() {
    private val beginDate = 1920.toString()
    private val endDate = Calendar.getInstance().get(Calendar.YEAR).toString()

    //search params
    var searchRequestQuery = EMPTY_SEARCH
    var startSearchYear = beginDate
    var endSearchYear = endDate
    var searchImage = true
    var searchVideo = true
    var searchAudio = true
    var searchPage = FIRST_PAGE

    fun getSearchMediaTypes(): String {
        var resultString = ""
        if (searchImage) {
            resultString = resultString + ContentType.IMAGE.contentType + ","
        }
        if (searchVideo) {
            resultString = resultString + ContentType.VIDEO.contentType + ","
        }
        if (searchAudio) {
            resultString = resultString + ContentType.AUDIO.contentType + ","
        }
        return resultString
    }
}

