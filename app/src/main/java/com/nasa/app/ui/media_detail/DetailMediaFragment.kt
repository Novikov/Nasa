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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class DetailMediaFragment : Fragment() {

    private var activityContract: IActivity? = null
    private lateinit var viewModel: DetailMediaViewModel
    lateinit var detailMediaRepository: DetailMediaRepository
    lateinit var nasaId: String
    lateinit var contentType:ContentType
    lateinit var contentLayout:ConstraintLayout
    lateinit var exoMediaPlayer: ExoMediaPlayer
    var time:Long? = null

    val TAG = "DetailMediaFragment"
    val PLAYER_TIME = "PlayerTime"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach: ")
        try {
            activityContract = context as IActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ")

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
        Log.e(TAG, "onCreateView: ")
        val binding = DataBindingUtil.inflate<FragmentMediaDetailBinding>(
            inflater,
            R.layout.fragment_media_detail,
            container,
            false
        )

        val view =  binding.root

        contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE
        val playerView = view.findViewById<PlayerView>(R.id.exo_player_video_view)
        playerView.player = exoMediaPlayer.getPlayer(requireContext())
        val imageView = view.findViewById<ImageView>(R.id.image_media_view)

        val button = view.findViewById<Button>(R.id.download_button)



        when(contentType){
            ContentType.IMAGE -> {
                prepareViewForImageContent(playerView, imageView)
            }
            ContentType.AUDIO -> {
                prepareViewForAudioContent(playerView)
            }
        }

        viewModel.mediaDetails.observe(viewLifecycleOwner, { mediaDetail ->

            when (contentType) {
                ContentType.IMAGE -> {
                    mediaDetail.assets?.let { assets ->
                        initViewByImageContent(
                            view,
                            mediaDetail,
                            assets
                        )
                    }
                }
                ContentType.VIDEO -> {
                    mediaDetail.assets?.let { asset -> initViewByVideoContent(mediaDetail, asset) }
                }
                ContentType.AUDIO -> {
                    mediaDetail.assets?.let { asset -> initViewByAudioContent(mediaDetail, asset) }
                }
            }


            binding.mediaDetail = mediaDetail


            //keywords initialization
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

            //EditText initialization
            val keyToOriginalAsset = mediaDetail.assets?.keys?.first().toString()
            val editText = view.findViewById<EditText>(R.id.url_edit_text)
            editText.setText(
                mediaDetail.assets?.get(keyToOriginalAsset),
                TextView.BufferType.EDITABLE
            )

            //LinkImageView initialization
            val linkImageView = view.findViewById<ImageView>(R.id.link_image_view)
            linkImageView.setOnClickListener {
                val address: Uri = Uri.parse(mediaDetail.assets?.get(keyToOriginalAsset))
                val intent = Intent(Intent.ACTION_VIEW, address)
                startActivity(intent)
            }

//            //Spinner initialization
//            val spinner: Spinner = view.findViewById(R.id.download_spinner)
//            val adapter = ArrayAdapter(
//                requireContext(),
//                R.layout.custom_spinner,
//                mediaDetail.assets?.keys?.toTypedArray()!!
//            )
//            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
//            spinner.adapter = adapter
//
//            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
//                ) {
////                    Toast.makeText(requireContext(), spinner.selectedItem.toString(), Toast.LENGTH_SHORT).show()
////                    val address: Uri = Uri.parse(mediaDetail.assets?.get(spinner.selectedItem.toString()))
////                    val intent = Intent(Intent.ACTION_VIEW, address)
////                    startActivity(intent)
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                }
//
//            }

            button.setOnClickListener {
                val urlList = mutableListOf<String>()
                mediaDetail.assets?.keys?.forEach {
                    urlList.add(it)
                }

                try {
                    val downloadDialogFragment = DownloadDialogFragment.newInstance(urlList as ArrayList<String>)
                    downloadDialogFragment.show(parentFragmentManager,"ErrorDialogFragment")
                }
                catch (ex: Exception){
                    Log.e("MainActivity", ex.message.toString())
                }
            }


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

        exoMediaPlayer.playPlayer(audioUrl!!, time ?: 0)
        contentLayout.visibility = View.VISIBLE
    }

    private fun prepareViewForAudioContent(playerView: PlayerView) {
        val orientation = getResources().getConfiguration().orientation
        Log.e("Device orientation", orientation.toString())
        when(orientation)
        {
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
        mediaDetail: MediaDetail,
        assets: Map<String, String>,
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

        exoMediaPlayer.playPlayer(videoUrl!!, time ?: 0)

        contentLayout.visibility = View.VISIBLE
    }




    private fun initViewByImageContent(
        view: View,
        it: MediaDetail,
        assets: Map<String, String>
    ) {
        var imageUrl = "https://visualsound.com/wp-content/uploads/2019/05/unavailable-image.jpg"

        for (key in it.assets?.keys!!) {
            if (key.contains("jpg")) {
                imageUrl = assets[key].toString()
                Log.e("ImageUrl", imageUrl)
                break
            }
        }

        Picasso.get().load(imageUrl).into(
            view.findViewById<ImageView>(R.id.image_media_view),
            object :
                Callback {
                override fun onSuccess() {
                    contentLayout.visibility = View.VISIBLE
                }

                override fun onError(e: java.lang.Exception?) {
                    TODO("Not yet implemented")
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
        Log.e(TAG, "onSaveInstanceState: ")
        outState.putLong(PLAYER_TIME, exoMediaPlayer.getPlayerTime())
        Log.e(TAG, "time onSavedInstanceState ${exoMediaPlayer.getPlayerTime()}")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated: ")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ")
        exoMediaPlayer.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: ")
        exoMediaPlayer.releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "onDetach: ")
    }


}