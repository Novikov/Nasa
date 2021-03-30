package com.nasa.app.ui.fragments_media_detail

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
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentVideoDetailBinding
import com.nasa.app.di.view_models.ViewModelProviderFactory
import com.nasa.app.ui.activity.Activity
import com.nasa.app.ui.fragment_download_files.DownloadFilesFragment
import javax.inject.Inject

class VideoDetailFragment : Fragment() {

    private lateinit var viewModel: DetailMediaViewModel
    var exoMediaPlayerTime: Long? = null
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    var activityContract: Activity? = null
    var isExoPlayerPrepared = false

    @Inject
    lateinit var exoPlayerWrapper: ExoPlayerWrapper
    @Inject
    lateinit var detailMediaRepository: DetailMediaRepository
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

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
            .create(nasaId,requireContext()).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        exoMediaPlayerTime = savedInstanceState?.getLong(EXO_MEDIA_PLAYER_TIME)
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(DetailMediaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(Companion.TAG, "onCreateView: ")
        val binding = DataBindingUtil.inflate<FragmentVideoDetailBinding>(
            inflater,
            R.layout.fragment_video_detail,
            container,
            false
        )

        activityContract?.clearErrorMessage()
        activityContract?.collapseSearchField()

        val view = binding.root

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.VISIBLE

        val contentDataLayout = view.findViewById<ConstraintLayout>(R.id.content_data_layout)
        val exoPlayerProgressBar = view.findViewById<ProgressBar>(R.id.exo_player_progress_bar)
        exoPlayerProgressBar.visibility = View.VISIBLE

        val contentDataProgressBar = view.findViewById<ProgressBar>(R.id.content_data_progress_bar)
        contentDataProgressBar.visibility = View.VISIBLE

        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        playerView.player = exoPlayerWrapper.getPlayer()
        val button = view.findViewById<Button>(R.id.update_results_button)

        exoPlayerWrapper.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if(state==Player.STATE_BUFFERING){
                    exoPlayerProgressBar.visibility = View.VISIBLE
                }
                if (state == Player.STATE_READY) {
                    exoPlayerProgressBar.visibility = View.INVISIBLE
                    isExoPlayerPrepared = true
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                contentLayout.visibility = View.INVISIBLE
                activityContract?.showErrorMessage("ExoPlayer loading error")
            }
        })

        val orientation = resources.configuration.orientation
        Log.i("Device orientation", orientation.toString())
        when(orientation){
            1 -> {
                contentDataLayout.visibility = View.INVISIBLE
            }
            2 -> {
                contentDataLayout.visibility = View.INVISIBLE
                activityContract?.hideActionBar()
            }
        }

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetailResponse ->

            var videoUrlString = ""

            for (asset in mediaDetailResponse.item.assets!!) {
                if (asset.value.contains("mp4")) {
                    videoUrlString = asset.value
                    break
                }
            }

            val substring = videoUrlString.substringAfter("//")
            videoUrlString = "https://$substring"
            Log.i("VideoUrl", "videoUrl $videoUrlString")

            val videoUri = Uri.parse(videoUrlString)

            exoPlayerWrapper.playPlayer(videoUri, exoMediaPlayerTime ?: 0)

            binding.mediaDetail = mediaDetailResponse.item

            if (mediaDetailResponse.item.description == "") {
                val secondDivider = view.findViewById<View>(R.id.second_divider)
                secondDivider.visibility = View.INVISIBLE
            }

            //Keywords initialization
            val keyWordFlexBox = view.findViewById<FlexboxLayout>(R.id.key_word_flex_box_container)
            for (i in mediaDetailResponse.item.keywords.indices) {
                var keywordTextView =
                    TextView(requireContext(), null, 0, R.style.key_word_text_view_style)
                if (i < mediaDetailResponse.item.keywords.size - 1) {
                    keywordTextView.text = "${mediaDetailResponse.item.keywords.get(i)}, "
                } else {
                    keywordTextView.text = "${mediaDetailResponse.item.keywords.get(i)}"
                }
                keyWordFlexBox.addView(keywordTextView)
            }

            //editText initialization
            val keyToOriginalAsset = mediaDetailResponse.item.assets.keys.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetailResponse.item.assets[keyToOriginalAsset],
                TextView.BufferType.EDITABLE
            )

            //linkImageView initialization
            val linkImageView = view.findViewById<ImageView>(R.id.link_image_view)
            linkImageView.setOnClickListener {
                activityContract?.collapseSearchField()
                val address: Uri = Uri.parse(mediaDetailResponse.item.assets[keyToOriginalAsset])
                val intent = Intent(Intent.ACTION_VIEW, address)
                startActivity(intent)
            }

            //download button initialization
            button.setOnClickListener {
                activityContract?.collapseSearchField()
                val urlList = mutableListOf<String>()
                mediaDetailResponse.item.assets.values.forEach {
                    urlList.add(it)
                }

                try {
                    val downloadDialogFragment =
                        DownloadFilesFragment.newInstance(urlList as ArrayList<String>)
                    downloadDialogFragment.show(parentFragmentManager, "ErrorDialogFragment")
                } catch (ex: Exception) {
                    Log.i("MainActivity", ex.message.toString())
                }
            }

        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> {
                    contentDataProgressBar.visibility = View.VISIBLE
                }
                NetworkState.LOADED -> {
                    contentDataLayout.visibility = View.VISIBLE
                    contentDataProgressBar.visibility = View.INVISIBLE
                }
                NetworkState.NO_INTERNET -> {
                    contentLayout.visibility = View.INVISIBLE
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.BAD_REQUEST -> {
                    contentLayout.visibility = View.INVISIBLE
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.NOT_FOUND -> {
                    contentLayout.visibility = View.INVISIBLE
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.ERROR -> {
                    contentLayout.visibility = View.INVISIBLE
                    activityContract?.showErrorMessage(it.msg)
                }
            }
        })

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: ")
        outState.putLong(Companion.EXO_MEDIA_PLAYER_TIME, exoPlayerWrapper.getPlayerTime())
        Log.i(TAG, "time onSavedInstanceState ${exoPlayerWrapper.getPlayerTime()}")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
        exoPlayerWrapper.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
        exoPlayerWrapper.releasePlayer()
    }

    companion object {
        const val TAG = "VideoDetailFragment"
        const val EXO_MEDIA_PLAYER_TIME = "ExoMediaPlayerTime"
    }
}