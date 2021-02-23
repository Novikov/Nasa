package com.nasa.app.ui.media_preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.databinding.MediaPreviewViewHolderBinding
import com.nasa.app.ui.UNREACHABLE_IMAGE_URL
import com.squareup.picasso.Picasso


class MediaPreviewAdapter(val dataSource: List<MediaPreview> ):RecyclerView.Adapter<MediaPreviewAdapter.MediaPreviewViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPreviewViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<MediaPreviewViewHolderBinding>(inflater, R.layout.media_preview_view_holder,parent,false)
        navController = Navigation.findNavController(parent)
        return MediaPreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaPreviewViewHolder, position: Int) {
        val mediaPreview = dataSource[position]
        holder.binding.mediaPreview = mediaPreview
        holder.itemView.setOnClickListener {
           val action = PreviewMediaFragmentDirections.actionMediaFragmentToDetailMediaFragment(mediaPreview.nasaId,mediaPreview.mediaType)
           navController?.navigate(action)
        }

        holder.bind(mediaPreview)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class MediaPreviewViewHolder(val binding: MediaPreviewViewHolderBinding) : RecyclerView.ViewHolder(binding.root){
        val mediaPreviewImageView: ImageView = itemView.findViewById(R.id.media_preview_recycler_view_image)
        val playVideoImageView: ImageView = itemView.findViewById(R.id.play_video_image_view)
        val playAudioImageView: ImageView = itemView.findViewById(R.id.play_audio_image_view)

        fun bind(mediaPreview: MediaPreview){
            when(mediaPreview.mediaType){
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
}