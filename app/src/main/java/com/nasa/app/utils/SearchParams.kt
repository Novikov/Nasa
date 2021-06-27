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
    val defaultSearchParams = "image,video,audio"

    fun initNewSearchRequestParams(query:String){
        searchRequestQuery = query
    }

    fun clearSearchParams(){
        searchRequestQuery = EMPTY_SEARCH_STRING
        startSearchYear = beginDate
        endSearchYear = endDate
        searchImage = true
        searchVideo = true
        searchAudio = true
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
            resultString = resultString + ContentType.AUDIO.contentType
        }
        return resultString
    }

    fun getDefaultSearchMediaTypes(): String {
        return ContentType.IMAGE.contentType + "," + ContentType.VIDEO.contentType + "," + ContentType.AUDIO.contentType
    }

    fun getDefaultSearchQuery(): String {
        return EMPTY_SEARCH_STRING
    }
}

