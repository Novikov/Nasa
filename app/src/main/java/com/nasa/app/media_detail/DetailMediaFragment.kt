package com.nasa.app.media_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.nasa.app.R
import com.squareup.picasso.Picasso


class DetailMediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_media_detail, container, false)

//        val img = view.findViewById<ImageView>(R.id.image_media)
//
//        Picasso
//            .get()
//            .load("https://images-assets.nasa.gov/image/MSFC-202100007/MSFC-202100007~medium.jpg")
//            .into(img);
//
//        img.adjustViewBounds = true


        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)

        val player = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem: MediaItem = MediaItem.fromUri("https://images-assets.nasa.gov/video/0091%20NEA%20Scout%20Music%20Rev29%2002102021/0091%20NEA%20Scout%20Music%20Rev29%2002102021~orig.mp4")
        player.setMediaItem(mediaItem)
        playerView.player = player
        player.prepare()
        player.play()

//
        return view
    }


}