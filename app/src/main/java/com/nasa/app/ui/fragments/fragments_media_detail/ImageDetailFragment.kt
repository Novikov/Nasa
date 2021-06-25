package com.nasa.app.ui.fragments.fragments_media_detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentImageDetailBinding
import com.nasa.app.ui.activity.Activity
import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragments.fragment_download_files.DownloadFilesFragment
import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailComponent
import com.nasa.app.utils.DOWNLOAD_DIALOG_FRAGMENT_TAG
import com.nasa.app.utils.EMPTY_STRING
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class ImageDetailFragment : Fragment() {
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    var activityContract: Activity? = null
    lateinit var detailComponent: DetailComponent

    @Inject
    lateinit var viewModel: DetailMediaViewModel

    @Inject
    lateinit var detailMediaRepository: DetailMediaRepository

    @Inject
    lateinit var picasso: Picasso

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

        val entryPoint = EntryPointAccessors.fromApplication(requireActivity().applicationContext, DetailEntryPoint::class.java)
        detailComponent = entryPoint.detailComponent().create(requireContext())
        detailComponent.inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        setHasOptionsMenu(true)
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

        val view = binding.root

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE

        val progressBar = view.findViewById<ProgressBar>(R.id.fragment_progress_bar)

        val errorTextView = view.findViewById<TextView>(R.id.fragment_error_text_view)

        val button = view.findViewById<Button>(R.id.update_results_button)
        val imageView = view.findViewById<ImageView>(R.id.image_media_view)

        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val orientation = resources.configuration.orientation
        when (orientation) {
            1 -> {
                if (!activityContract?.isActionBarShowing()!!){
                    activityContract?.showActionBar()
                }
            }
            2 -> {
                activityContract?.hideActionBar()
            }
        }

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetailResponse ->
            picasso.load(mediaDetailResponse.item.previewUrl).into(
                imageView,
                object :
                    Callback {
                    override fun onSuccess() {
                        contentLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Log.i(TAG, "picasso loading image error: ${e.toString()}")
                    }
                })

            binding.mediaDetail = mediaDetailResponse.item

            if (mediaDetailResponse.item.description == EMPTY_STRING) {
                val secondDivider = view.findViewById<View>(R.id.second_divider)
                secondDivider.visibility = View.INVISIBLE
            }

            //Keywords initialization
            val keyWordFlexBox = view.findViewById<FlexboxLayout>(R.id.key_word_flex_box_container)
            for (i in mediaDetailResponse.item.keywords.indices) {
                var keywordTextView =
                    TextView(requireContext(), null, 0, R.style.key_word_text_view_style)
                if (i < mediaDetailResponse.item.keywords.size - 1) {
                    keywordTextView.text = "${mediaDetailResponse.item.keywords[i]}, "
                } else {
                    keywordTextView.text = "${mediaDetailResponse.item.keywords[i]}"
                }
                keyWordFlexBox.addView(keywordTextView)
            }

            //editText initialization
            val keyToOriginalAsset = mediaDetailResponse.item.assets?.keys?.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetailResponse.item.assets?.get(keyToOriginalAsset),
                TextView.BufferType.EDITABLE
            )

            //linkImageView initialization
            val linkImageView = view.findViewById<ImageView>(R.id.link_image_view)
            linkImageView.setOnClickListener {
                val address: Uri =
                    Uri.parse(mediaDetailResponse.item.assets?.get(keyToOriginalAsset))
                val intent = Intent(Intent.ACTION_VIEW, address)
                startActivity(intent)
            }

            //download button initialization
            button.setOnClickListener {
                val urlList = mutableListOf<String>()
                mediaDetailResponse.item.assets?.values?.forEach {
                    urlList.add(it)
                }

                try {
                    val downloadDialogFragment =
                        DownloadFilesFragment.newInstance(urlList as ArrayList<String>)
                    downloadDialogFragment.show(parentFragmentManager, DOWNLOAD_DIALOG_FRAGMENT_TAG)
                } catch (ex: Exception) {
                    Log.i(TAG, ex.message.toString())
                }
            }
        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING ->
                    progressBar.visibility = View.VISIBLE
                NetworkState.NO_INTERNET -> {
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.BAD_REQUEST -> {
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.TIMEOUT -> {
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.NOT_FOUND -> {
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
            }
        })
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        activityContract= null
    }

    companion object {
        const val TAG = "ImageDetailFragment"
    }
}