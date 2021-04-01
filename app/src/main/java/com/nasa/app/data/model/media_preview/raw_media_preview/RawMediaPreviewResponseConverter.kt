package com.nasa.app.data.model.media_preview.raw_media_preview

import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.utils.EMPTY_STRING
import com.nasa.app.utils.POST_PER_PAGE
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RawMediaPreviewResponseConverter @Inject constructor() {
    lateinit var previewUrl: String
    lateinit var nasaId: String
    lateinit var mediaType: ContentType
    lateinit var dateCreated: String
    lateinit var description: String

    private val maxDescriptionSymbolsCount = 200

    fun getMediaPreviewResponse(rawMediaPreviewResponse: RawMediaPreviewResponse): MediaPreviewResponse {
        val page = rawMediaPreviewResponse.collection.href.substringAfter("page=").toInt()
        val totalResults = rawMediaPreviewResponse.collection.metadata.total_hits
        val totalPages = if ((totalResults % POST_PER_PAGE) != 0) {
            (totalResults / POST_PER_PAGE) + 1
        } else {
            (totalResults / POST_PER_PAGE)
        }

        val previewsList = ArrayList<MediaPreview>()

        rawMediaPreviewResponse.collection.items.forEach { item ->

            //preview url for audio item is always null
            val tmpLinks: List<AssetLink>? = item.links

            previewUrl = if (tmpLinks != null) {
                item.links.first().href
            } else {
                EMPTY_STRING
            }

            nasaId = item.data.first().nasa_id

            val tmpMediaType = item.data.first().media_type

            when (tmpMediaType) {
                ContentType.IMAGE.contentType -> {
                    mediaType = ContentType.IMAGE
                }
                ContentType.AUDIO.contentType -> {
                    mediaType = ContentType.AUDIO
                }
                ContentType.VIDEO.contentType -> {
                    mediaType = ContentType.VIDEO
                }
            }

            val tmpDateCreated = item.data.first().date_created
            dateCreated = convertDate(tmpDateCreated)

            val tmpDescription: String? = item.data.first().description
            description = if (tmpDescription != null) {
                val rawDescription = item.data.first().description
                if (rawDescription.length > maxDescriptionSymbolsCount) {
                    rawDescription.substring(0, maxDescriptionSymbolsCount)
                } else {
                    item.data.first().description
                }
            } else {
                EMPTY_STRING
            }
            val mediaPreview = MediaPreview(nasaId, previewUrl, mediaType, dateCreated, description)

            previewsList.add(mediaPreview)
        }

        val mediaPreviewResponse =
            MediaPreviewResponse(previewsList, page, totalPages, totalResults)
        return mediaPreviewResponse
    }

    private fun convertDate(dateStr: String): String {
        val parsedDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val parsedDate: Date? = parsedDateFormat.parse(dateStr)

        if (parsedDate != null) {

            val dayDateFormat = SimpleDateFormat("dd")
            val day = dayDateFormat.format(parsedDate)

            val monthDateFormat = SimpleDateFormat("MMMM")
            val month = monthDateFormat.format(parsedDate)

            val yearDateFormat = SimpleDateFormat("yyyy")
            val year = yearDateFormat.format(parsedDate)

            val dateCreated = "$month $day, $year"

            return dateCreated

        } else {
            return EMPTY_STRING
        }
    }
}
