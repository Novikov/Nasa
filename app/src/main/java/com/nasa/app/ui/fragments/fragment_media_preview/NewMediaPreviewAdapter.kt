package com.nasa.app.ui.fragments.fragment_media_preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import kotlinx.android.synthetic.main.image_media_preview_item.view.*

class NewMediaPreviewAdapter (val context: Context) : PagedListAdapter<MediaPreview, RecyclerView.ViewHolder>(MovieDiffCallback()){

    val IMAGE_VIEW_TYPE = 1
    val VIDEO_VIEW_TYPE = 2
    val AUDIO_VIEW_TYPE = 3
    val NETWORK_VIEW_TYPE = 4

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View

        when(viewType)
        {
            1->{
                view = layoutInflater.inflate(R.layout.image_media_preview_item, parent, false)
                return ImageMediaPreviewViewHolder(view)
            }
            2->{
                view = layoutInflater.inflate(R.layout.video_media_preview_item, parent, false)
                return VideoMediaPreviewViewHolder(view)
            }
            3->{
                view = layoutInflater.inflate(R.layout.audio_media_preview_item, parent, false)
                return AudioMediaPreviewViewHolder(view)
            }
            else -> {
                view = layoutInflater.inflate(R.layout.image_media_preview_item, parent, false)
                return ImageMediaPreviewViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==IMAGE_VIEW_TYPE) {
            (holder as ImageMediaPreviewViewHolder).bind(getItem(position)!!)
        }

        if(getItemViewType(position)==VIDEO_VIEW_TYPE) {
            (holder as VideoMediaPreviewViewHolder).bind(getItem(position)!!)
        }

        if(getItemViewType(position)==AUDIO_VIEW_TYPE) {
            (holder as AudioMediaPreviewViewHolder).bind(getItem(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(getItem(position)!!.mediaType){
            ContentType.IMAGE -> {
                return IMAGE_VIEW_TYPE
            }
            ContentType.VIDEO -> {
                return VIDEO_VIEW_TYPE
            }
            ContentType.AUDIO -> {
                return AUDIO_VIEW_TYPE
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<MediaPreview>() {
        override fun areItemsTheSame(oldItem: MediaPreview, newItem: MediaPreview): Boolean {
            return oldItem.nasaId == newItem.nasaId
        }

        override fun areContentsTheSame(oldItem: MediaPreview, newItem: MediaPreview): Boolean {
            return oldItem == newItem
        }
    }

    class ImageMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {
            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated

            Glide.with(itemView.context)
                .load(mediaPreview.previewUrl)
                .into(itemView.media_preview_recycler_view_image)
        }
    }

    class VideoMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {
            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated

            Glide.with(itemView.context)
                .load(mediaPreview.previewUrl)
                .into(itemView.media_preview_recycler_view_image)
        }
    }

    class AudioMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {
            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated
        }
    }
}