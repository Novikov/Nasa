package com.nasa.app.media_detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nasa.app.R
import com.squareup.picasso.Picasso

class DetailMediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_media_detail, container, false)

        val container = view.findViewById<RelativeLayout>(R.id.media_container)

        val img = ImageView(requireContext())

        Picasso
            .get()
            .load("https://images-assets.nasa.gov/image/MSFC-202100014/MSFC-202100014~thumb.jpg")
            .into(img);

        container.addView(img)


        return view
    }


}