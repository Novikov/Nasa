package com.nasa.app.utils

import com.nasa.app.data.model.ContentType
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchParams @Inject constructor() {
    val beginDate = 1920.toString()
    val endDate = Calendar.getInstance().get(Calendar.YEAR).toString()

    //search params
    var searchRequestQuery = EMPTY_SEARCH_STRING
    var startSearchYear = beginDate
    var endSearchYear = endDate
    var searchImage = true
    var searchVideo = true
    var searchAudio = true
    var searchPage = FIRST_PAGE

    fun initNewSearchRequestParams(query:String){
        searchRequestQuery = query
        searchPage = FIRST_PAGE
    }

    fun clearSearchParams(){
        searchRequestQuery = EMPTY_SEARCH_STRING
        startSearchYear = beginDate
        endSearchYear = endDate
        searchImage = true
        searchVideo = true
        searchAudio = true
        searchPage = FIRST_PAGE
    }

    fun updateSearchParams(startSearchYear:String,endSearchYear:String,searchImage:Boolean,searchVideo:Boolean,searchAudio:Boolean){
        this.startSearchYear = startSearchYear
        this.endSearchYear = endSearchYear
        this.searchImage = searchImage
        this.searchVideo = searchVideo
        this.searchAudio = searchAudio
    }

    fun getSearchMediaTypes(): String {
        var resultString = EMPTY_STRING
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

