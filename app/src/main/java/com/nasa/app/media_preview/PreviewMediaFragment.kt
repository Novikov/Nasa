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
import com.nasa.app.data.model.ContentType

class PreviewMediaFragment : Fragment() {
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view =  inflater.inflate(R.layout.fragment_media_preview, container, false)

        val buttonToImage = view.findViewById<Button>(R.id.button_to_image)

        buttonToImage.setOnClickListener {
           val action =  PreviewMediaFragmentDirections.actionMediaFragmentToDetailMediaFragment("201210220003HQ",ContentType.IMAGE)
           findNavController().navigate(action)
        }

        val buttonToVideo = view.findViewById<Button>(R.id.button_to_video)

        buttonToVideo.setOnClickListener {
            val action =  PreviewMediaFragmentDirections.actionMediaFragmentToDetailMediaFragment("Expedition_58_Museum_Red_Square_Visit_with_Interpreter_2018_1115_1545_724102",ContentType.VIDEO)
            findNavController().navigate(action)
        }

        val buttonToAudio = view.findViewById<Button>(R.id.button_to_audio)

        buttonToAudio.setOnClickListener {
            val action =  PreviewMediaFragmentDirections.actionMediaFragmentToDetailMediaFragment("Ep72_ISS_pt1",ContentType.AUDIO)
            findNavController().navigate(action)
        }

        return view
    }


}