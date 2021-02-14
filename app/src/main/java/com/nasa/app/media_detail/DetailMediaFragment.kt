package com.nasa.app.media_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.nasa.app.R
import com.squareup.picasso.Picasso


class DetailMediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_media_detail, container, false)

//Spinner
        val spinner: Spinner = view.findViewById(R.id.download_button)
        val adapter = ArrayAdapter(
            requireContext(), R.layout.custom_spinner, resources.getStringArray(R.array.list)
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.adapter = adapter;




//        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
//
//        playerView.visibility = View.GONE
//
//        val img = view.findViewById<ImageView>(R.id.image_media_view)
//
//        Picasso
//            .get()
//            .load("https://images-assets.nasa.gov/image/MSFC-202100007/MSFC-202100007~medium.jpg")
//            .into(img);
//
//        img.adjustViewBounds = true


        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)

        val player = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem: MediaItem = MediaItem.fromUri("https://images-assets.nasa.gov/video/Expedition_58_Museum_Red_Square_Visit_with_Interpreter_2018_1115_1545_724102/Expedition_58_Museum_Red_Square_Visit_with_Interpreter_2018_1115_1545_724102~orig.mp4")
        player.setMediaItem(mediaItem)
        playerView.player = player
        player.prepare()
        player.play()



        return view
    }


}