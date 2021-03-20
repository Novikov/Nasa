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
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentAudioDetailBinding
import com.nasa.app.di.view_models.ViewModelProviderFactory
import com.nasa.app.ui.ExoMediaPlayer
import com.nasa.app.ui.Activity
import com.nasa.app.ui.DownloadDialogFragment
import javax.inject.Inject

class AudioDetailFragment : Fragment() {
    private lateinit var viewModel: DetailMediaViewModel
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    var time: Long? = null
    var activityContract: Activity? = null
    var isExoPlayerPrepared = false

    @Inject lateinit var exoMediaPlayer: ExoMediaPlayer
    @Inject lateinit var detailMediaRepository: DetailMediaRepository
    @Inject lateinit var providerFactory: ViewModelProviderFactory

    val PLAYER_TIME = "PlayerTime"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(Companion.TAG, "onAttach: ")
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


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            Log.i(Companion.TAG, "onCreate: ")

            time = savedInstanceState?.getLong(PLAYER_TIME)

            viewModel = ViewModelProviders.of(this, providerFactory).get(DetailMediaViewModel::class.java)
            exoMediaPlayer = ExoMediaPlayer()
        }


        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            Log.i(Companion.TAG, "onCreateView: ")
            val binding = DataBindingUtil.inflate<FragmentAudioDetailBinding>(
                inflater,
                R.layout.fragment_audio_detail,
                container,
                false
            )

            activityContract?.clearMsg()
            activityContract?.collapseSearchField()

            val view = binding.root

            val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
            contentLayout.visibility = View.INVISIBLE
            val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
            playerView.player = exoMediaPlayer.getPlayer(requireContext())
            val button = view.findViewById<Button>(R.id.update_results_button)


            exoMediaPlayer.addListener(object : Player.EventListener {
                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    if (state == Player.STATE_READY) {
                        isExoPlayerPrepared = true
                        contentLayout.visibility = View.VISIBLE
                        activityContract?.hideProgressBar()
                    }
                    if (state == Player.STATE_BUFFERING) {
                        if(!isExoPlayerPrepared) {
                            contentLayout.visibility = View.INVISIBLE
                            activityContract?.showProgressBar()
                        }
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    activityContract?.hideProgressBar()
                    contentLayout.visibility = View.GONE
                    activityContract?.showMsg("ExoPlayer loading error")
                }
            })

            val orientation = getResources().getConfiguration().orientation
            Log.i("Device orientation", orientation.toString())
            when (orientation) {
                1 -> {
                    playerView.layoutParams =
                        ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            450
                        )
                }
                2 -> {
                    playerView.layoutParams =
                        ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                    activityContract?.hideActionBar()
                }
            }



            viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetail ->

                //audio content initialization
                var audioUrl = ""
                for (asset in mediaDetail.assets!!) {
                    if (asset.value.contains("mp3")) {
                        audioUrl = asset.value
                        break
                    }
                }
                val substring = audioUrl.substringAfter("//")
                audioUrl = "https://$substring"
                Log.i("AudioUrl", "audioUrl $audioUrl")
                exoMediaPlayer.playPlayer(audioUrl, time ?: 0)

                binding.mediaDetail = mediaDetail

                if (mediaDetail.description == "") {
                    val secondDivider = view.findViewById<View>(R.id.second_divider)
                    secondDivider.visibility = View.INVISIBLE
                }

                //Keywords initialization
                val keyWordFlexBox =
                    view.findViewById<FlexboxLayout>(R.id.key_word_flex_box_container)
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

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            Log.i(Companion.TAG, "onSaveInstanceState: ")
            outState.putLong(PLAYER_TIME, exoMediaPlayer.getPlayerTime())
            Log.i(Companion.TAG, "time onSavedInstanceState ${exoMediaPlayer.getPlayerTime()}")
        }

        override fun onPause() {
            super.onPause()
            Log.i(Companion.TAG, "onPause: ")
            exoMediaPlayer.pausePlayer()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            Log.i(Companion.TAG, "onDestroyView: ")
            exoMediaPlayer.releasePlayer()
        }

    companion object {
        const val TAG = "AudioDetailFragment"
    }
}


