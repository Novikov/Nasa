package com.nasa.app.media_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.nasa.app.R


class DetailMediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_media_detail, container, false)

//        val container = view.findViewById<RelativeLayout>(R.id.media_container)

//        val img = ImageView(requireContext())
//
//        Picasso
//            .get()
//            .load("https://images-assets.nasa.gov/image/MSFC-202100014/MSFC-202100014~thumb.jpg")
//            .into(img);

//        container.addView(img)



        val player = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem: MediaItem = MediaItem.fromUri("https://images-assets.nasa.gov/video/0091%20NEA%20Scout%20Music%20Rev29%2002102021/0091%20NEA%20Scout%20Music%20Rev29%2002102021~orig.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()

        val playerView = view.findViewById<PlayerView>(R.id.media_container)

        playerView.player = player

//        container.addView(playerView)

        return view
    }


}