package com.nasa.app.ui.fragments.fragment_media_preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class NewMediaPreviewAdapter (val context: Context,private val picasso: Picasso) : PagedListAdapter<MediaPreview, RecyclerView.ViewHolder>(MovieDiffCallback()){

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.media_preview_item, parent, false)
        return MediaPreviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NewMediaPreviewAdapter.MediaPreviewViewHolder
        val mediaPreview = getItem(position)
        viewHolder.bind(mediaPreview!!, false)
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

//    fun setNetworkState(newNetworkState: NetworkState) {
//        val previousState = this.networkState
//        val hadExtraRow = hasExtraRow()
//        this.networkState = newNetworkState
//        val hasExtraRow = hasExtraRow()
//
//        if (hadExtraRow != hasExtraRow) {
//            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
//                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
//            } else {                                       //hasExtraRow is true and hadExtraRow false
//                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
//            }
//        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
//            notifyItemChanged(itemCount - 1)       //add the network message at the end
//        }
//
//    }

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

        private val progressBar: ProgressBar = view.findViewById(R.id.item_progress_bar)

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
                        .into(mediaPreviewImageView, object : Callback {
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
                        .into(mediaPreviewImageView, object : Callback {
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


}