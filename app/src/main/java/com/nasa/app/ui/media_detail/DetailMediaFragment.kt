package com.nasa.app.ui.media_detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.R
import com.nasa.app.data.api.NasaApiClient
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaDetail
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentMediaDetailBinding
import com.nasa.app.ui.DownloadDialogFragment
import com.nasa.app.ui.ExoMediaPlayer
import com.nasa.app.ui.IActivity
import com.nasa.app.ui.UNREACHABLE_IMAGE_URL
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class DetailMediaFragment : Fragment() {

    private var activityContract: IActivity? = null
    private lateinit var viewModel: DetailMediaViewModel
    lateinit var detailMediaRepository: DetailMediaRepository
    lateinit var nasaId: String
    lateinit var contentType: ContentType
    lateinit var exoMediaPlayer: ExoMediaPlayer
    var time: Long? = null

    val TAG = "DetailMediaFragment"
    val PLAYER_TIME = "PlayerTime"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: ")
        try {
            activityContract = context as IActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")

        //getting fragment params
        if (arguments != null) {
            val args = DetailMediaFragmentArgs.fromBundle(requireArguments())
            nasaId = args.nasaId
            contentType = args.contentType

        } else {
            throw Exception("arguments can't be null")
        }

        time = savedInstanceState?.getLong(PLAYER_TIME)

        val apiService = NasaApiClient.getClient()
        detailMediaRepository = DetailMediaRepository(apiService)
        viewModel = getViewModel(nasaId)
        exoMediaPlayer = ExoMediaPlayer()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView: ")
        val binding = DataBindingUtil.inflate<FragmentMediaDetailBinding>(
            inflater,
            R.layout.fragment_media_detail,
            container,
            false
        )

        activityContract?.collapseSearchField()

        val view = binding.root

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE
        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        playerView.player = exoMediaPlayer.getPlayer(requireContext())
        val imageView = view.findViewById<ImageView>(R.id.image_media_view)
        val button = view.findViewById<Button>(R.id.update_results_button)

        //media content view preparation
        when (contentType) {
            ContentType.IMAGE -> {
                prepareViewForImageContent(playerView, imageView)
            }
            ContentType.AUDIO -> {
                prepareViewForAudioContent(playerView)
            }
        }

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetail ->

            //view media content initialization
            when (contentType) {
                ContentType.IMAGE -> {
                    mediaDetail.assets?.let {
                        initViewByImageContent(
                            imageView,
                            mediaDetail,
                            contentLayout
                        )
                    }
                }
                ContentType.VIDEO -> {
                    mediaDetail.assets?.let {
                        initViewByVideoContent(
                            exoMediaPlayer,
                            mediaDetail,
                            contentLayout
                        )
                    }
                }
                ContentType.AUDIO -> {
                    mediaDetail.assets?.let {
                        initViewByAudioContent(
                            exoMediaPlayer,
                            mediaDetail,
                            contentLayout
                        )
                    }
                }
            }

            binding.mediaDetail = mediaDetail

            if (mediaDetail.description==""){
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
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkState.LOADING -> activityContract?.showProgressBar()
                NetworkState.LOADED -> activityContract?.hideProgressBar()
            }
        })

        return view
    }

    private fun initViewByAudioContent(
        exoMediaPlayer: ExoMediaPlayer,
        mediaDetail: MediaDetail,
        contentLayout: ConstraintLayout
    ) {


        var audioUrl: String? = null

        for (asset in mediaDetail.assets!!) {
            if (asset.value.contains("mp3")) {
                audioUrl = asset.value
                break
            }
        }

        val substring = audioUrl!!.substringAfter("//")
        audioUrl = "https://$substring"

        Log.i("AudioUrl", "audioUrl ${audioUrl!!}")

        exoMediaPlayer.playPlayer(audioUrl!!, time ?: 0)
        contentLayout.visibility = View.VISIBLE
    }

    private fun prepareViewForAudioContent(playerView: PlayerView) {
        val orientation = getResources().getConfiguration().orientation
        Log.i("Device orientation", orientation.toString())
        when (orientation) {
            1 -> {
                playerView.layoutParams =
                    ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 450)
            }
            2 -> {
                playerView.layoutParams =
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                    )
            }
        }
    }

    private fun initViewByVideoContent(
        exoMediaPlayer: ExoMediaPlayer,
        mediaDetail: MediaDetail,
        contentLayout: ConstraintLayout
    ) {
        var videoUrl: String? = null

        for (asset in mediaDetail.assets!!) {
            if (asset.value.contains("mp4")) {
                videoUrl = asset.value
                break
            }
        }

        val substring = videoUrl!!.substringAfter("//")
        videoUrl = "https://$substring"

        Log.i("VideoUrl", "videoUrl ${videoUrl!!}")

        exoMediaPlayer.playPlayer(videoUrl!!, time ?: 0)

        contentLayout.visibility = View.VISIBLE
    }


    private fun initViewByImageContent(
        imageView: ImageView,
        mediaDetail: MediaDetail,
        contentLayout: ConstraintLayout
    ) {
        var imageUrl:String? = null

        for (asset in mediaDetail.assets!!) {
            if (asset.value.contains("jpg").and(asset.value.contains("small"))) {
                imageUrl = asset.value
                Log.i("ImageUrl", imageUrl)
                break
            }
        }

        if (imageUrl==null){
            for (asset in mediaDetail.assets!!) {
                if (asset.value.contains("jpg").and(asset.value.contains("medium"))) {
                    imageUrl = asset.value
                    Log.i("ImageUrl", imageUrl)
                    break
                }
            }
        }
        else if (imageUrl==null) {
            for (asset in mediaDetail.assets!!) {
                if (asset.value.contains("jpg").and(asset.value.contains("large"))) {
                    imageUrl = asset.value
                    Log.i("ImageUrl", imageUrl)
                    break
                }
            }
        }

        Picasso.get().load(imageUrl?: UNREACHABLE_IMAGE_URL).into(
            imageView,
            object :
                Callback {
                override fun onSuccess() {
                    contentLayout.visibility = View.VISIBLE
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.e(TAG, "error loading image ${e!!.message}" )
                }
            })
    }

    private fun prepareViewForImageContent(
        playerView: PlayerView,
        imageView: ImageView
    ) {
        playerView.visibility = View.GONE
        imageView.adjustViewBounds = true
    }

    private fun getViewModel(nasaId: String): DetailMediaViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailMediaViewModel(detailMediaRepository, nasaId) as T
            }
        })[DetailMediaViewModel::class.java]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState: ")
        outState.putLong(PLAYER_TIME, exoMediaPlayer.getPlayerTime())
        Log.i(TAG, "time onSavedInstanceState ${exoMediaPlayer.getPlayerTime()}")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
        exoMediaPlayer.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
        exoMediaPlayer.releasePlayer()
    }
}