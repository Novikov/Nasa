package com.nasa.app.ui.fragments.fragment_media_preview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_media_preview_item.view.*
import java.lang.Exception

class NewMediaPreviewAdapter (val context: Context) : PagedListAdapter<MediaPreview, RecyclerView.ViewHolder>(MovieDiffCallback()){

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.image_media_preview_item, parent, false)
        return MediaPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewMediaPreviewAdapter.MediaPreviewViewHolder).bind(getItem(position)!!)
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

    inner class MediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {
            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated

            Glide.with(itemView.context)
                .load(mediaPreview.previewUrl)
                .into(itemView.media_preview_recycler_view_image)
        }
    }
}