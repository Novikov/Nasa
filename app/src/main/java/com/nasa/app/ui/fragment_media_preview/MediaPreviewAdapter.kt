package com.nasa.app.ui.fragment_media_preview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.R
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.utils.SearchParams
import com.nasa.app.utils.POST_PER_PAGE
import com.squareup.picasso.Picasso
import javax.inject.Inject


class MediaPreviewAdapter @Inject constructor(val mediaRepository: PreviewMediaRepository) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var searchParams: SearchParams

    private val SEARCH_INFO_TEXTVIEW_VIEW = 0
    private val MEDIA_PREVIEW_VIEW = 1
    private val NEXT_BUTTON_VIEW = 2
    private val BACK_AND_NEXT_BUTTON_VIEW = 3
    private val BACK_BUTTON_VIEW = 4

    private val EMPTY_VIEW = 5

    var dataSource: MediaPreviewResponse =
        MediaPreviewResponse(listOf(MediaPreview("Apollo 11 For All Mankind", "https://images-assets.nasa.gov/video/Apollo 11 For All Mankind/Apollo 11 For All Mankind~thumb.jpg", ContentType.IMAGE, "2014-07-16T00:00:00Z", "A documentary of the Apollo 11 launch, lunar landing and exploration and return to earth which included a stay in quarantine.")), 1, 1, 1)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var navController: NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_info_item, parent, false)
                SearchInfoViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.media_preview_item, parent, false)
                navController = Navigation.findNavController(parent)
                MediaPreviewViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.next_navigation_button_item, parent, false)
                NextButtonNavigationViewHolder(view)
            }
            3 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.two_navigation_button_item, parent, false)
                TwoButtonNavigationViewHolder(view)
            }

            4 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.back_navigation_button_item, parent, false)
                BackButtonNavigationViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_view_item, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            0 -> {
                Log.i("Position", "search: ${position}")
                val result = searchParams.searchRequestQuery.substring(0, 1)
                    .toUpperCase() + searchParams.searchRequestQuery.substring(1).toLowerCase()
                val viewHolder = holder as SearchInfoViewHolder
                if (searchParams.searchRequestQuery == "\"\"") {
                    viewHolder.searchInfoTextView.text = "Random uploads"
                } else if (dataSource.totalResults < 100) {
                    viewHolder.searchInfoTextView.text =
                        "${dataSource.totalResults} results returned for \"$result\""
                } else if (dataSource.totalResults > 100) {
                    var endResult = dataSource.page * POST_PER_PAGE
                    var startResult = endResult - 99
                    if (endResult > dataSource.totalResults) {
                        endResult = dataSource.totalResults
                    }
                    viewHolder.searchInfoTextView.text =
                        "$startResult - $endResult of ${dataSource.totalResults} for \"$result\""
                } else {
                    viewHolder.searchInfoTextView.text = ""
                }
            }
            1 -> {
                val viewHolder = holder as MediaPreviewViewHolder
                val mediaPreview = dataSource.mediaPreviewList[position-1]
                val hideDivider = (position-1) == dataSource.mediaPreviewList.lastIndex
                Log.i("Position", "real position : ${position}, position - 1 == ${position-1}")
                viewHolder.bind(mediaPreview, hideDivider)
                viewHolder.itemView.setOnClickListener {
                    when (mediaPreview.mediaType) {
                        ContentType.AUDIO -> {
                            navController?.navigate(
                                PreviewMediaFragmentDirections.actionMediaFragmentToAudioDetailFragment(
                                    mediaPreview.nasaId,
                                    mediaPreview.mediaType
                                )
                            )
                        }
                        ContentType.VIDEO -> {
                            navController?.navigate(
                                PreviewMediaFragmentDirections.actionMediaFragmentToVideoDetailFragment(
                                    mediaPreview.nasaId,
                                    mediaPreview.mediaType
                                )
                            )
                        }

                        ContentType.IMAGE -> {
                            navController?.navigate(
                                PreviewMediaFragmentDirections.actionMediaFragmentToImageDetailFragment(
                                    mediaPreview.nasaId,
                                    mediaPreview.mediaType
                                )
                            )
                        }
                    }
                }
            }
            2 -> {
                //next button
                val viewHolder = holder as NextButtonNavigationViewHolder
                searchParams.searchPage = dataSource.page + 1
                viewHolder.nextButton.setOnClickListener {
                    mediaRepository.updateMediaPreviews()
                }
            }
            3 -> {
                //back and next buttons
                val viewHolder = holder as TwoButtonNavigationViewHolder
                viewHolder.nextButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page + 1
                    mediaRepository.updateMediaPreviews()
                }

                viewHolder.previousButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page - 1
                    mediaRepository.updateMediaPreviews()
                }
            }
            4 -> {
                val viewHolder = holder as BackButtonNavigationViewHolder
                viewHolder.backButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page - 1
                    mediaRepository.updateMediaPreviews()
                }
            }
            5 -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            //search info textView for 0 position
            (position == 0) -> {
                0
            }

            //mediaPreviewItem for 1 - 100 position
            (position <= dataSource.mediaPreviewList.size) -> {
                1
            }

            //navigation view or empty view for 101 position
            (position == dataSource.mediaPreviewList.size + 1) -> {
                //first page and other doesn't exist
                if ((dataSource.page == 1) && (dataSource.totalPages - dataSource.page == 0)) {
                    5
                }
                //first page and other exists
                else if (dataSource.page == 1 && (dataSource.totalPages - dataSource.page > 0)) {
                    2
                }
                //middle
                else if (dataSource.page > 1 && (dataSource.totalPages - dataSource.page > 0)) {
                    3
                }
                //end
                else if (dataSource.page > 1 && (dataSource.totalPages - dataSource.page == 0)) {
                    4
                }
                //unreachable
                else {
                    5
                }
            }
            else -> {
                0
            }
        }
    }

    //0 - searchInfoTV, 100 - mediaPreviewItems, 101 - next|prev_and_next|prev buttons
    override fun getItemCount(): Int {
            return dataSource.mediaPreviewList.size + 2
    }

    inner class MediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaPreviewImageView: ImageView =
            view.findViewById(R.id.media_preview_recycler_view_image)
        val audioBackgroundImageView: ImageView =
            view.findViewById(R.id.audio_background_image_view)
        val playVideoImageView: ImageView = view.findViewById(R.id.play_video_image_view)
        val playAudioImageView: ImageView = view.findViewById(R.id.play_audio_image_view)
        val descriptionTextView: TextView = view.findViewById(R.id.description_text_view)
        val dateCreatedTextView: TextView = view.findViewById(R.id.date_created_text_view)
        val divider: View = view.findViewById(R.id.divider)

        fun bind(mediaPreview: MediaPreview, hideDivider: Boolean) {
            descriptionTextView.text = mediaPreview.description
            dateCreatedTextView.text = mediaPreview.dateCreated
            divider.visibility = View.VISIBLE
            if (hideDivider) {
                divider.visibility = View.INVISIBLE
            }
            when (mediaPreview.mediaType) {
                ContentType.IMAGE -> {
                    mediaPreviewImageView.visibility = View.VISIBLE
                    audioBackgroundImageView.visibility = View.INVISIBLE
                    Log.i("Position", "preview url: ${mediaPreview.previewUrl}")
                    picasso
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView)
                    playVideoImageView.visibility = View.INVISIBLE
                    playAudioImageView.visibility = View.INVISIBLE
                }
                ContentType.VIDEO -> {
                    mediaPreviewImageView.visibility = View.VISIBLE
                    audioBackgroundImageView.visibility = View.INVISIBLE
                    Log.i("Position", "preview url: ${mediaPreview.previewUrl}")
                    picasso
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView);
                    playVideoImageView.visibility = View.VISIBLE
                    playAudioImageView.visibility = View.INVISIBLE

                }
                ContentType.AUDIO -> {
                    Log.i("Position", "preview url: - audio")
                    mediaPreviewImageView.visibility = View.INVISIBLE
                    audioBackgroundImageView.visibility = View.VISIBLE
                    playVideoImageView.visibility = View.INVISIBLE
                    playAudioImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class NextButtonNavigationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nextButton: Button = view.findViewById(R.id.next_page_button)
    }

    inner class BackButtonNavigationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val backButton: Button = view.findViewById(R.id.back_page_button)
    }

    inner class TwoButtonNavigationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nextButton: Button = view.findViewById(R.id.next_page_button)
        val previousButton: Button = view.findViewById(R.id.previous_page_button)
    }

    inner class SearchInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val searchInfoTextView: TextView = view.findViewById(R.id.search_info_ext_view)
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}