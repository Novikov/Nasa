package com.nasa.app.ui

import com.nasa.app.data.model.ContentType
import com.nasa.app.utils.FIRST_PAGE

val EMPTY_SEARCH = "\"\""
val BEGIN_DATE = "1920"
val END_DATE = "2021"


//search params
var SEARCH_REQUEST_QUERY = EMPTY_SEARCH
var SEARCH_YEAR_START = BEGIN_DATE
var SEARCH_YEAR_END = END_DATE
var SEARCH_IMAGE = true
var SEARCH_VIDEO = true
var SEARCH_AUDIO = true
var SEARCH_PAGE = FIRST_PAGE

fun getSearchMediaTypes(): String {
    var resultString = ""
    if (SEARCH_IMAGE) {
        resultString = resultString + ContentType.IMAGE.contentType + ","
    }
    if (SEARCH_VIDEO) {
        resultString = resultString + ContentType.VIDEO.contentType + ","
    }
    if (SEARCH_AUDIO) {
        resultString = resultString + ContentType.AUDIO.contentType + ","
    }
    return resultString
}