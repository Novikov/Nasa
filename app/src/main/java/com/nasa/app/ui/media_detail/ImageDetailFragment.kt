package com.nasa.app.ui.media_detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.data.api.NasaApiClient
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentAudioDetailBinding
import com.nasa.app.databinding.FragmentImageDetailBinding
import com.nasa.app.ui.Activity
import com.nasa.app.ui.DownloadDialogFragment
import com.nasa.app.ui.ExoMediaPlayer
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ImageDetailFragment : Fragment() {
    private lateinit var viewModel: DetailMediaViewModel
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    var activityContract: Activity? = null

    @Inject
    lateinit var detailMediaRepository: DetailMediaRepository

    val TAG = "AudioDetailFragment"
    val PLAYER_TIME = "PlayerTime"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: ")
        try {
            activityContract = context as Activity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface Activity")
        }

        //getting fragment params
        if (arguments != null) {
            val args = AudioDetailFragmentArgs.fromBundle(requireArguments())
            nasaId = args.nasaId
            contentType = args.contentType

        } else {
            throw Exception("arguments can't be null")
        }

        (requireActivity().application as BaseApplication).appComponent.getDetailComponent()
            .create(nasaId).inject(this)
    }

    fun getViewModel(
        nasaId: String,
        detailMediaRepository: DetailMediaRepository
    ): DetailMediaViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailMediaViewModel(detailMediaRepository, nasaId) as T
            }
        })[DetailMediaViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")

        viewModel = getViewModel(nasaId,detailMediaRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val binding = DataBindingUtil.inflate<FragmentImageDetailBinding>(
            inflater,
            R.layout.fragment_image_detail,
            container,
            false
        )

        activityContract?.clearMsg()
        activityContract?.collapseSearchField()

        val view = binding.root

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE
        val button = view.findViewById<Button>(R.id.update_results_button)
        val imageView = view.findViewById<ImageView>(R.id.image_media_view)

        val orientation = getResources().getConfiguration().orientation
        Log.i("Device orientation", orientation.toString())
        when (orientation) {
            1 -> {
            }
            2 -> {
                activityContract?.hideActionBar()
            }
        }

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetail ->
            Picasso.get().load(mediaDetail.previewUrl).into(
                imageView,
                object :
                    Callback {
                    override fun onSuccess() {
                        contentLayout.visibility = View.VISIBLE
                        activityContract?.hideProgressBar()
                    }

                    override fun onError(e: java.lang.Exception?) {
                        activityContract?.hideProgressBar()
                        activityContract?.showMsg("Image loading error: ${e?.message}")
                    }
                })

            binding.mediaDetail = mediaDetail

            if (mediaDetail.description == "") {
                val secondDivider = view.findViewById<View>(R.id.second_divider)
                secondDivider.visibility = View.INVISIBLE
            }

            //Keywords initialization
            val keyWordFlexBox = view.findViewById<FlexboxLayout>(R.id.key_word_flex_box_container)
            for (i in mediaDetail.keywords.indices) {
                var keywordTextView =
                    TextView(requireContext(), null, 0, R.style.key_word_text_view_style)
                if (i < mediaDetail.keywords.size - 1) {
                    keywordTextView.text = "${mediaDetail.keywords.get(i)}, "
                } else {
                    keywordTextView.text = "${mediaDetail.keywords.get(i)}"
                }
                keyWordFlexBox.addView(keywordTextView)
            }

            //editText initialization
            val keyToOriginalAsset = mediaDetail.assets?.keys?.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetail.assets?.get(keyToOriginalAsset),
                TextView.BufferType.EDITABLE
            )

            //linkImageView initialization
            val linkImageView = view.findViewById<ImageView>(R.id.link_image_view)
            linkImageView.setOnClickListener {
                activityContract?.collapseSearchField()
                val address: Uri = Uri.parse(mediaDetail.assets?.get(keyToOriginalAsset))
                val intent = Intent(Intent.ACTION_VIEW, address)
                startActivity(intent)
            }

            //download button initialization
            button.setOnClickListener {
                activityContract?.collapseSearchField()
                val urlList = mutableListOf<String>()
                mediaDetail.assets?.values?.forEach {
                    urlList.add(it)
                }

                try {
                    val downloadDialogFragment =
                        DownloadDialogFragment.newInstance(urlList as ArrayList<String>)
                    downloadDialogFragment.show(parentFragmentManager, "ErrorDialogFragment")
                } catch (ex: Exception) {
                    Log.i("MainActivity", ex.message.toString())
                }
            }
        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> activityContract?.showProgressBar()
                NetworkState.NO_INTERNET -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showMsg(it.msg)
                }
                NetworkState.ERROR -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showMsg(it.msg)
                }
            }
        })

        return view
    }
}