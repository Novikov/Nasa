package com.nasa.app.ui.media_preview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview

class PreviewMediaFragment : Fragment() {
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_media_preview, container, false)

        val mediaPreviewRecyclerView = view.findViewById<RecyclerView>(R.id.media_preview_recycler_view)
        mediaPreviewRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MediaPreviewAdapter(
            listOf(MediaPreview("201210220003HQ",
                "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                ContentType.IMAGE,
                "21:21:21",
                "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello"),
                MediaPreview("201210220003HQ",
                    "http://images-assets.nasa.gov/image/201210220003HQ/201210220003HQ~thumb.jpg",
                    ContentType.IMAGE,
                    "21:21:21",
                    "Hello")
            )
        )
        mediaPreviewRecyclerView.adapter = adapter
        return view
    }


}