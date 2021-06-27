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
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentAudioDetailBinding
import com.nasa.app.ui.activity.Activity
import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragments.fragment_download_files.DownloadFilesFragment
import com.nasa.app.utils.DOWNLOAD_DIALOG_FRAGMENT_TAG
import com.nasa.app.utils.EMPTY_STRING
import com.nasa.app.utils.EXO_MEDIA_PLAYER_INITIAL_TIME
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@AndroidEntryPoint
class AudioDetailFragment : Fragment() {
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    var exoMediaPlayerTime: Long? = null
    var activityContract: Activity? = null
    var isExoPlayerPrepared = false

    val viewModel: DetailMediaViewModel by viewModels()

    @Inject
    lateinit var exoPlayerWrapper: ExoPlayerWrapper

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
            throw Exception("Fragment arguments can't be null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        exoMediaPlayerTime = savedInstanceState?.getLong(EXO_MEDIA_PLAYER_TIME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val binding = DataBindingUtil.inflate<FragmentAudioDetailBinding>(
            inflater,
            R.layout.fragment_audio_detail,
            container,
            false
        )

        val view = binding.root

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.VISIBLE

        val errorTextView = view.findViewById<TextView>(R.id.fragment_error_text_view)

        val contentDataLayout = view.findViewById<ConstraintLayout>(R.id.content_data_layout)
        contentDataLayout.visibility = View.INVISIBLE

        val exoPlayerProgressBar = view.findViewById<ProgressBar>(R.id.exo_player_progress_bar)
        val contentDataProgressBar = view.findViewById<ProgressBar>(R.id.content_data_progress_bar)
        exoPlayerProgressBar.visibility = View.VISIBLE

        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        playerView.player = exoPlayerWrapper.getPlayer()
        val button = view.findViewById<Button>(R.id.update_results_button)

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

        exoPlayerWrapper.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if (state == Player.STATE_BUFFERING) {
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
                errorTextView.text = getString(R.string.Exo_player_error_message)
                errorTextView.visibility = View.VISIBLE
            }
        })

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetailResponse ->

            //audio content initialization
            var audioUrlString = EMPTY_STRING
            for (asset in mediaDetailResponse.item.assets!!) {
                if (asset.value.contains(getString(R.string.mp3_uri_substring))) {
                    audioUrlString = asset.value
                    break
                }
            }
            val substring = audioUrlString.substringAfter("//")
            audioUrlString = "https://$substring"

            val audioUri = Uri.parse(audioUrlString)
            exoPlayerWrapper.playPlayer(audioUri, exoMediaPlayerTime ?: EXO_MEDIA_PLAYER_INITIAL_TIME)
            binding.mediaDetail = mediaDetailResponse.item

            if (mediaDetailResponse.item.description == EMPTY_STRING) {
                val secondDivider = view.findViewById<View>(R.id.second_divider)
                secondDivider.visibility = View.INVISIBLE
            }

            //Keywords initialization
            val keyWordFlexBox =
                view.findViewById<FlexboxLayout>(R.id.key_word_flex_box_container)
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
            val keyToOriginalAsset = mediaDetailResponse.item.assets.keys.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetailResponse.item.assets[keyToOriginalAsset],
                TextView.BufferType.EDITABLE
            )

            //linkImageView initialization
            val linkImageView = view.findViewById<ImageView>(R.id.link_image_view)
            linkImageView.setOnClickListener {
                val address: Uri = Uri.parse(mediaDetailResponse.item.assets[keyToOriginalAsset])
                val intent = Intent(Intent.ACTION_VIEW, address)
                startActivity(intent)
            }

            //download button initialization
            button.setOnClickListener {
                val urlList = mutableListOf<String>()
                mediaDetailResponse.item.assets.values.forEach {
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
                NetworkState.LOADING -> {
                    contentDataProgressBar.visibility = View.VISIBLE
                }
                NetworkState.LOADED -> {
                    contentDataLayout.visibility = View.VISIBLE
                    contentDataProgressBar.visibility = View.INVISIBLE
                }
                NetworkState.NO_INTERNET -> {
                    contentLayout.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.TIMEOUT -> {
                    contentLayout.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.BAD_REQUEST -> {
                    contentLayout.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.NOT_FOUND -> {
                    contentLayout.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
                NetworkState.ERROR -> {
                    contentLayout.visibility = View.INVISIBLE
                    errorTextView.text = it.msg
                    errorTextView.visibility = View.VISIBLE
                }
            }
        })
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: ")
        outState.putLong(EXO_MEDIA_PLAYER_TIME, exoPlayerWrapper.getPlayerTime())
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

    override fun onDestroy() {
        super.onDestroy()
        activityContract= null
    }
    companion object {
        const val TAG = "AudioDetailFragment"
        const val EXO_MEDIA_PLAYER_TIME = "ExoMediaPlayerTime"
    }
}


