package com.nasa.app.ui.fragments.fragment_media_preview.initial

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.repository.NetworkState
import kotlinx.android.synthetic.main.image_media_preview_item.view.date_created_text_view
import kotlinx.android.synthetic.main.image_media_preview_item.view.description_text_view
import kotlinx.android.synthetic.main.image_media_preview_item.view.media_preview_recycler_view_image
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.video_media_preview_item.view.*


class InitialMediaPreviewAdapter (val context: Context) : PagedListAdapter<MediaPreview, RecyclerView.ViewHolder>(
    MovieDiffCallback()
){

    val IMAGE_VIEW_TYPE = 1
    val VIDEO_VIEW_TYPE = 2
    val AUDIO_VIEW_TYPE = 3
    val NETWORK_VIEW_TYPE = 4

    private var networkState: NetworkState? = null

    var navController: NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        navController = Navigation.findNavController(parent)
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View

        return when (viewType) {
            1 -> {
                view = layoutInflater.inflate(R.layout.image_media_preview_item, parent, false)
                return ImageMediaPreviewViewHolder(view)
            }
            2 -> {
                view = layoutInflater.inflate(R.layout.video_media_preview_item, parent, false)
                return VideoMediaPreviewViewHolder(view)
            }
            3 -> {
                view = layoutInflater.inflate(R.layout.audio_media_preview_item, parent, false)
                return AudioMediaPreviewViewHolder(view)
            }
            else -> {
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                return NetworkStateItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            IMAGE_VIEW_TYPE->{
                (holder as ImageMediaPreviewViewHolder).bind(getItem(position)!!)
            }
            VIDEO_VIEW_TYPE->{
                (holder as VideoMediaPreviewViewHolder).bind(getItem(position)!!)
            }
            AUDIO_VIEW_TYPE->{
                (holder as AudioMediaPreviewViewHolder).bind(getItem(position)!!)
            }
            NETWORK_VIEW_TYPE->{
                (holder as NetworkStateItemViewHolder).bind(networkState)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
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
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MediaPreview>() {
        override fun areItemsTheSame(oldItem: MediaPreview, newItem: MediaPreview): Boolean {
            return oldItem.nasaId == newItem.nasaId
        }

        override fun areContentsTheSame(oldItem: MediaPreview, newItem: MediaPreview): Boolean {
            return oldItem == newItem
        }
    }

    inner class ImageMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mediaPreview: MediaPreview) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.WHITE)
            circularProgressDrawable.start()

            Glide.with(itemView.context)
                .load(mediaPreview.previewUrl)
                .placeholder(circularProgressDrawable)
                .into(itemView.media_preview_recycler_view_image)

                itemView.setOnClickListener {
                    navController?.navigate(
                        InitialPreviewMediaFragmentDirections.actionMediaFragmentToImageDetailFragment(
                            mediaPreview.nasaId,
                            mediaPreview.mediaType
                        )
                    )
                }

            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated

            }
    }

    inner class VideoMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(Color.WHITE)
            circularProgressDrawable.start()

            Glide.with(itemView.context)
                .load(mediaPreview.previewUrl)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.play_video_image_view.visibility = View.VISIBLE
                        return false
                    }

                })
                .placeholder(circularProgressDrawable)
                .into(itemView.media_preview_recycler_view_image)

            itemView.setOnClickListener {

                navController?.navigate(
                    InitialPreviewMediaFragmentDirections.actionMediaFragmentToVideoDetailFragment(
                        mediaPreview.nasaId,
                        mediaPreview.mediaType
                    )
                )
            }

            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated
        }
    }


    inner class AudioMediaPreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(mediaPreview: MediaPreview) {
            itemView.description_text_view.text = mediaPreview.description
            itemView.date_created_text_view.text = mediaPreview.dateCreated

            itemView.setOnClickListener {
                navController?.navigate(
                    InitialPreviewMediaFragmentDirections.actionMediaFragmentToAudioDetailFragment(
                        mediaPreview.nasaId,
                        mediaPreview.mediaType
                    )
                )
            }
        }
    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.TIMEOUT) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
                itemView.progress_bar_item.visibility = View.VISIBLE
            }

            else if (networkState != null && networkState == NetworkState.NO_INTERNET) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
                itemView.progress_bar_item.visibility = View.VISIBLE;
            }

            else if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }
}