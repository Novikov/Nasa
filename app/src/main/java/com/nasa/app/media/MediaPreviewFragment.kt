package com.nasa.app.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.nasa.app.R
import kotlinx.android.synthetic.main.fragment_media.*

class MediaFragment : Fragment() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_media, container, false)

        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_imageFragmentDetails)
        }

        return view
    }


}