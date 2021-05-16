package com.nasa.app.ui.fragments.fragment_media_preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.R
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.utils.EMPTY_SEARCH_STRING
import com.nasa.app.utils.SearchParams
import com.nasa.app.utils.POST_PER_PAGE
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class MediaPreviewAdapter (var dataSource: MediaPreviewResponse, private val picasso: Picasso, private val searchParams: SearchParams,private val callback:()-> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SEARCH_INFO_TEXTVIEW_VIEW = 0
    private val MEDIA_PREVIEW_VIEW = 1
    private val NEXT_BUTTON_VIEW = 2
    private val BACK_AND_NEXT_BUTTON_VIEW = 3
    private val BACK_BUTTON_VIEW = 4
    private val EMPTY_VIEW = 5
    var navController: NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        navController = Navigation.findNavController(parent)
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_info_item, parent, false)
                SearchInfoViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.media_preview_item, parent, false)
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
                //Search results view initialization
                val result = searchParams.searchRequestQuery.substring(0, 1)
                    .toUpperCase() + searchParams.searchRequestQuery.substring(1).toLowerCase()
                val viewHolder = holder as SearchInfoViewHolder
                when {
                    searchParams.searchRequestQuery == EMPTY_SEARCH_STRING -> {
                        viewHolder.searchInfoTextView.text = "Random uploads"
                    }
                    dataSource.totalResults < 100 -> {
                        viewHolder.searchInfoTextView.text =
                            "${dataSource.totalResults} results returned for \"$result\""
                    }
                    dataSource.totalResults > 100 -> {
                        var endResult = dataSource.page * POST_PER_PAGE
                        var startResult = endResult - 99
                        if (endResult > dataSource.totalResults) {
                            endResult = dataSource.totalResults
                        }
                        viewHolder.searchInfoTextView.text =
                            "$startResult - $endResult of ${dataSource.totalResults} for \"$result\""
                    }
                    else -> {
                        viewHolder.searchInfoTextView.text = ""
                    }
                }
            }
            1 -> {
                //Media preview view initialization
                val viewHolder = holder as MediaPreviewViewHolder
                val mediaPreview = dataSource.mediaPreviewList[position-1]
                val hideDivider = (position-1) == dataSource.mediaPreviewList.lastIndex
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
                //Next button view initialization
                val viewHolder = holder as NextButtonNavigationViewHolder
                searchParams.searchPage = dataSource.page + 1
                viewHolder.nextButton.setOnClickListener {
                    callback()
                }
            }
            3 -> {
                //Back and next buttons view initialization
                val viewHolder = holder as TwoButtonNavigationViewHolder
                viewHolder.nextButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page + 1
                    callback()
                }

                viewHolder.previousButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page - 1
                    callback()
                }
            }
            4 -> {
                //Back button view initialization
                val viewHolder = holder as BackButtonNavigationViewHolder
                viewHolder.backButton.setOnClickListener {
                    searchParams.searchPage = dataSource.page - 1
                    callback()
                }
            }
            5 -> {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            //Search results view for 0 position
            (position == 0) -> {
                0
            }

            //MediaPreviewItem view for 1 - 100 position
            (position <= dataSource.mediaPreviewList.size) -> {
                1
            }

            //Navigation view or empty view for 101 position
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

    //0 - searchInfoView, 100 - mediaPreviewItemsViews, 101 - next|prev_and_next|prev buttons views
    override fun getItemCount(): Int {
            return dataSource.mediaPreviewList.size + 2
    }

    inner class MediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mediaPreviewImageView: ImageView =
            view.findViewById(R.id.media_preview_recycler_view_image)
        private val audioBackgroundImageView: ImageView =
            view.findViewById(R.id.audio_background_image_view)
        private val playVideoImageView: ImageView = view.findViewById(R.id.play_video_image_view)
        private val playAudioImageView: ImageView = view.findViewById(R.id.play_audio_image_view)
        private val descriptionTextView: TextView = view.findViewById(R.id.description_text_view)
        private val dateCreatedTextView: TextView = view.findViewById(R.id.date_created_text_view)
        private val divider: View = view.findViewById(R.id.divider)

        private val progressBar:ProgressBar = view.findViewById(R.id.item_progress_bar)

        fun bind(mediaPreview: MediaPreview, hideDivider: Boolean) {
            playAudioImageView.visibility = View.INVISIBLE
            playVideoImageView.visibility = View.INVISIBLE

            descriptionTextView.text = mediaPreview.description
            dateCreatedTextView.text = mediaPreview.dateCreated
            divider.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            if (hideDivider) {
                divider.visibility = View.INVISIBLE
            }
            when (mediaPreview.mediaType) {
                ContentType.IMAGE -> {
                    mediaPreviewImageView.visibility = View.VISIBLE
                    audioBackgroundImageView.visibility = View.INVISIBLE

                    picasso
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView, object : Callback{
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                                playVideoImageView.visibility = View.INVISIBLE
                                playAudioImageView.visibility = View.INVISIBLE
                            }

                            override fun onError(e: Exception?) {

                            }
                        })
                }
                ContentType.VIDEO -> {
                    mediaPreviewImageView.visibility = View.VISIBLE
                    audioBackgroundImageView.visibility = View.INVISIBLE

                    picasso
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView, object : Callback{
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                                playVideoImageView.visibility = View.VISIBLE
                                playAudioImageView.visibility = View.INVISIBLE
                            }

                            override fun onError(e: Exception?) {

                            }

                        })
                }
                ContentType.AUDIO -> {
                    progressBar.visibility = View.GONE
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