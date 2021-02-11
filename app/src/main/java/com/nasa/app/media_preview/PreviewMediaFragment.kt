package com.nasa.app.media_preview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nasa.app.R

class PreviewMediaFragment : Fragment() {
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_media_preview, container, false)

        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_detailMediaFragment)
        }

        return view
    }


}