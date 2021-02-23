package com.nasa.app.ui.media_preview

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
import com.nasa.app.data.api.json.MediaPreviewResponse
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview
import com.squareup.picasso.Picasso


class MediaPreviewAdapter(val dataSource: MediaPreviewResponse) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SEARCH_INFO_VIEW = 0
    val MEDIA_PREVIEW_VIEW = 1
    val NEXT_PAGE_VIEW = 2
    val PREVIOUS_PAGE_NEXT_PAGE_VIEW = 3

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
                    .inflate(R.layout.one_navigation_button_item, parent, false)
                OneButtonNavigationViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.two_navigation_button_item, parent, false)
                TwoButtonNavigationViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            1 -> {
                val viewHolder = holder as MediaPreviewViewHolder
                val mediaPreview = dataSource.mediaPreviewList[position]
                viewHolder.bind(mediaPreview)
                viewHolder.itemView.setOnClickListener {
            val action = PreviewMediaFragmentDirections.actionMediaFragmentToDetailMediaFragment(
                mediaPreview.nasaId,
                mediaPreview.mediaType
            )
            navController?.navigate(action)
        }
            }
            2 -> {}
            3 -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> {
                0
            }
            position < dataSource.mediaPreviewList.size -> {
                1
            }
            position == dataSource.mediaPreviewList.size -> {
                2
            }
            else -> {
                3
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.mediaPreviewList.size + 1
    }

    inner class MediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mediaPreviewImageView: ImageView =
            view.findViewById(R.id.media_preview_recycler_view_image)
        val playVideoImageView: ImageView = view.findViewById(R.id.play_video_image_view)
        val playAudioImageView: ImageView = view.findViewById(R.id.play_audio_image_view)
        val descriptionTextView: TextView = view.findViewById(R.id.description_text_view)
        val dateCreatedTextView: TextView = view.findViewById(R.id.date_created_text_view)
        val divider: View = view.findViewById(R.id.divider)

        fun bind(mediaPreview: MediaPreview) {
            descriptionTextView.setText(mediaPreview.description)
            dateCreatedTextView.setText(mediaPreview.dateCreated)
            when (mediaPreview.mediaType) {
                ContentType.IMAGE -> {
                    Picasso
                        .get()
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView)
                    playVideoImageView.visibility = View.GONE
                    playAudioImageView.visibility = View.GONE
                }
                ContentType.VIDEO -> {
                    Picasso
                        .get()
                        .load(mediaPreview.previewUrl)
                        .fit()
                        .centerCrop()
                        .into(mediaPreviewImageView);
                    playVideoImageView.visibility = View.VISIBLE
                    playAudioImageView.visibility = View.GONE

                }
                ContentType.AUDIO -> {
                    playVideoImageView.visibility = View.GONE
                    playAudioImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class OneButtonNavigationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nextButton: Button = view.findViewById(R.id.next_page_button)
    }

    inner class TwoButtonNavigationViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val nextButton: Button = view.findViewById(R.id.next_page_button)
        val previousButton: Button = view.findViewById(R.id.previous_page_button)
    }

    inner class SearchInfoViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val searchInfoTextView: TextView = view.findViewById(R.id.search_info_ext_view)
    }

}