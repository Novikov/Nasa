package com.nasa.app.media_detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.flexbox.FlexboxLayout
import com.nasa.app.IActivity
import com.nasa.app.R
import com.nasa.app.data.api.NasaApiClient
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaDetail
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.databinding.FragmentMediaDetailBinding
import com.squareup.picasso.Picasso


class DetailMediaFragment : Fragment() {

    private var activityContract: IActivity? = null
    private lateinit var viewModel: DetailMediaViewModel
    lateinit var detailMediaRepository: DetailMediaRepository
    lateinit var nasaId: String
    lateinit var contentType:ContentType

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("VideoPlayerFragment", "onAttach is called")
        try {
            activityContract = context as IActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            val args = DetailMediaFragmentArgs.fromBundle(requireArguments())
            nasaId = args.nasaId
            contentType = args.contentType
        } else {
            throw Exception("arguments can't be null")
        }

        val apiService = NasaApiClient.getClient()
        detailMediaRepository = DetailMediaRepository(apiService)
        viewModel = getViewModel(nasaId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding = DataBindingUtil.inflate<FragmentMediaDetailBinding>(
            inflater,
            R.layout.fragment_media_detail,
            container,
            false
        )

        val view =  binding.root

        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE


        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetail ->

            when(contentType){
                ContentType.IMAGE -> {
                    mediaDetail.assets?.let { assets -> initViewByImageContent(playerView, view, mediaDetail, assets) }
                }
                ContentType.VIDEO -> {
                    mediaDetail.assets?.let { asset -> initViewByVideoContent(mediaDetail, asset, view) }
                }
                ContentType.AUDIO -> {
                    mediaDetail.assets?.let { asset -> initViewByAudioContent(mediaDetail, asset, view) }
                }
            }


            binding.mediaDetail = mediaDetail

            //keywords initializing
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

            //EditText initializing
            val keyToOriginalAsset = mediaDetail.assets?.keys?.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetail.assets?.get(keyToOriginalAsset),
                TextView.BufferType.EDITABLE
            )

            //Spinner initializing
            val spinner: Spinner = view.findViewById(R.id.download_spinner)
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.custom_spinner,
                mediaDetail.assets?.keys?.toTypedArray()!!
            )
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            spinner.adapter = adapter;





            contentLayout.visibility = View.VISIBLE

        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkState.LOADING -> activityContract?.showProgressBar()
                NetworkState.LOADED -> activityContract?.hideProgressBar()
            }
        })









        return view
    }

    private fun initViewByAudioContent(
        mediaDetail: MediaDetail,
        assets: Map<String, String>,
        view: View
    ) {
        var audioUrl: String? = null

        for (key in mediaDetail.assets?.keys!!) {
            if (key.contains("mp3")) {
                audioUrl = assets[key].toString()
                break
            }
            break
        }

        val substring = audioUrl!!.substringAfter("//")
        audioUrl = "https://$substring"

        Log.e("AudioUrl", "audioUrl ${audioUrl!!}")

        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        val player = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem: MediaItem = MediaItem.fromUri(audioUrl!!)
        player.setMediaItem(mediaItem)
        playerView.player = player
        player.prepare()
        player.play()
    }

    private fun initViewByVideoContent(
        mediaDetail: MediaDetail,
        assets: Map<String, String>,
        view: View
    ) {
        var videoUrl: String? = null

        for (key in mediaDetail.assets?.keys!!) {
            if (key.contains("mp4")) {
                videoUrl = assets[key].toString()
                break
            }
            break
        }

        val substring = videoUrl!!.substringAfter("//")
        videoUrl = "https://$substring"

        Log.e("VideoUrl", "videoUrl ${videoUrl!!}")

        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        val player = SimpleExoPlayer.Builder(requireContext()).build()

        val mediaItem: MediaItem =
            MediaItem.fromUri(videoUrl!!)
        player.setMediaItem(mediaItem)
        playerView.player = player
        player.prepare()
        player.play()
    }

    private fun initViewByImageContent(
        playerView: PlayerView,
        view: View,
        it: MediaDetail,
        assets: Map<String, String>
    ) {
        playerView.visibility = View.GONE
        val img = view.findViewById<ImageView>(R.id.image_media_view)
        img.adjustViewBounds = true

        var imageUrl: String =
            "https://visualsound.com/wp-content/uploads/2019/05/unavailable-image.jpg"

        for (key in it.assets?.keys!!) {
            if (key.contains("jpg")) {
                imageUrl = assets[key].toString()
                Log.e("ImageUrl", imageUrl)
                break
            }
        }

        Picasso
            .get()
            .load(imageUrl)
            .into(img)
    }

    private fun getViewModel(nasaId: String): DetailMediaViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailMediaViewModel(detailMediaRepository, nasaId) as T
            }
        })[DetailMediaViewModel::class.java]
    }
}