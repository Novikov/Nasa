package com.nasa.app.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.slider.RangeSlider
import com.nasa.app.BaseApplication
import com.nasa.app.R
import javax.inject.Inject

class SearchSettingsFragment @Inject constructor() : DialogFragment() {
    private val TAG = "SearchSettingsFragment"
    private var activityContract: Activity? = null
    @Inject lateinit var searchParams: SearchParams
    lateinit var tmpBeginSearchDateValue: String
    lateinit var tmpEndSearchDateValue: String

    var tmpIsCheckedImageCheckBox: Boolean = true
    var tmpIsCheckedVideoCheckBox: Boolean = true
    var tmpIsCheckedAudioCheckBox: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: ")
        try {
            activityContract = context as Activity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tmpIsCheckedImageCheckBox = searchParams.searchImage
        tmpIsCheckedVideoCheckBox = searchParams.searchVideo
        tmpIsCheckedAudioCheckBox = searchParams.searchAudio
        tmpBeginSearchDateValue = searchParams.startSearchYear
        tmpEndSearchDateValue = searchParams.endSearchYear
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_settings, container, false)

        //checkboxes
        val imageCheckBox = view.findViewById<CheckBox>(R.id.images_check_box)
        val videoCheckBox = view.findViewById<CheckBox>(R.id.videos_check_box)
        val audioCheckBox = view.findViewById<CheckBox>(R.id.audio_check_box)
        imageCheckBox.isChecked = tmpIsCheckedImageCheckBox
        videoCheckBox.isChecked = tmpIsCheckedVideoCheckBox
        audioCheckBox.isChecked = tmpIsCheckedAudioCheckBox

        //One checkboks have to be enabled
        imageCheckBox.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                tmpIsCheckedImageCheckBox = false
                //return state
                if (!(videoCheckBox.isChecked.or(audioCheckBox.isChecked))) {
                    it.isChecked = true
                    tmpIsCheckedImageCheckBox = true
                }
            } else {
                tmpIsCheckedImageCheckBox = true
            }
        }

        videoCheckBox.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                tmpIsCheckedVideoCheckBox = false
                if (!(imageCheckBox.isChecked.or(audioCheckBox.isChecked))) {
                    it.isChecked = true
                    tmpIsCheckedVideoCheckBox = true
                }
            } else {
                tmpIsCheckedVideoCheckBox = true
            }
        }

        audioCheckBox.setOnClickListener {
            if (!(it as CheckBox).isChecked) {
                tmpIsCheckedAudioCheckBox = false
                if (!(imageCheckBox.isChecked.or(videoCheckBox.isChecked))) {
                    it.isChecked = true
                    tmpIsCheckedAudioCheckBox = true
                }
            } else {
                tmpIsCheckedAudioCheckBox = true
            }
        }

        //updateResultButton
        val updateResultsButton = view.findViewById<Button>(R.id.update_results_button)
        updateResultsButton.setOnClickListener {
            this.dismiss()
            activityContract?.searchRequest(searchParams.searchRequestQuery)
            searchParams.startSearchYear = tmpBeginSearchDateValue
            searchParams.endSearchYear = tmpEndSearchDateValue
            searchParams.searchImage = tmpIsCheckedImageCheckBox
            searchParams.searchVideo = tmpIsCheckedVideoCheckBox
            searchParams.searchAudio = tmpIsCheckedAudioCheckBox
        }

        //date text views
        val beginDateTextView = view.findViewById<TextView>(R.id.begin_date_text_view)
        beginDateTextView.text = searchParams.startSearchYear
        val endDateTextView = view.findViewById<TextView>(R.id.end_date_text_view)
        endDateTextView.text = searchParams.endSearchYear

        //range slider
        val rangeSlider = view.findViewById<RangeSlider>(R.id.rangeSlider)
        rangeSlider.values = listOf(searchParams.startSearchYear.toFloat(), searchParams.endSearchYear.toFloat())

        rangeSlider.addOnChangeListener { slider, _, _ ->
            beginDateTextView.text = slider.values[0].toInt().toString()
            endDateTextView.text = slider.values[1].toInt().toString()
            tmpBeginSearchDateValue = slider.values[0].toInt().toString()
            tmpEndSearchDateValue = slider.values[1].toInt().toString()
        }

        return view
    }

    companion object {
        fun newInstance(): SearchSettingsFragment {
            val fragment = SearchSettingsFragment()
            return fragment
        }
    }
}