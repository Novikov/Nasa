package com.nasa.app.ui.fragment_media_preview


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.di.view_models.ViewModelProviderFactory
import com.nasa.app.ui.activity.Activity
import com.nasa.app.utils.EMPTY_SEARCH_STRING
import com.nasa.app.utils.SearchParams
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PreviewMediaFragment : Fragment() {
    private var activityContract: Activity? = null
    private lateinit var viewModel: PreviewMediaViewModel
    lateinit var mediaPreviewRecyclerView: RecyclerView
    lateinit var adapter: MediaPreviewAdapter

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var searchParams: SearchParams
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

        (requireActivity().application as BaseApplication).appComponent.getPreviewComponent()
            .create().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(PreviewMediaViewModel::class.java)

        //Custom back navigation callback
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (searchParams.searchRequestQuery.equals(EMPTY_SEARCH_STRING)){
                searchParams.clearSearchParams()
                requireActivity().finish()
            }
            //If the back button has been pressed - show initial media previews!
            else if (viewModel.mediaPreviews.value!=viewModel.initialMediaPreviews.value){
                viewModel.putInitialDataToMediaPreviews()
                rewindRecyclerViewToBegining(mediaPreviewRecyclerView)
                searchParams.clearSearchParams()
                if (activityContract!!.isErrorMessageShoved()) {
                    activityContract!!.clearErrorMessage()
                }
            }
            //If the back button has been pressed again - close application!
            else{
                searchParams.clearSearchParams()
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityContract?.clearErrorMessage()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_media_preview, container, false)

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE

        mediaPreviewRecyclerView = view.findViewById(R.id.media_preview_recycler_view)
        initRecyclerView()

        viewModel.mediaPreviews.observe(viewLifecycleOwner, {
            Log.i(TAG, "media preview: ${it.hashCode()}")

            adapter.dataSource = it
            adapter.notifyDataSetChanged()
            rewindRecyclerViewToBegining(mediaPreviewRecyclerView)
        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> {
                    contentLayout.visibility = View.INVISIBLE
                    activityContract?.showProgressBar()
                }
                NetworkState.LOADED -> {
                    contentLayout.visibility = View.VISIBLE
                    activityContract?.hideProgressBar()
                }
                NetworkState.NOTHING_FOUND -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.NO_INTERNET -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.TIMEOUT -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showErrorMessage(it.msg)
                }
                NetworkState.ERROR -> {
                    activityContract?.hideProgressBar()
                    activityContract?.showErrorMessage(it.msg)
                }
            }
        })

        return view
    }

    private fun rewindRecyclerViewToBegining(mediaPreviewRecyclerView:RecyclerView) {
        (mediaPreviewRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            0,
            0
        )
    }

    private fun initRecyclerView() {
        mediaPreviewRecyclerView.layoutManager = LinearLayoutManager(context)
        val tmpMediaPreviewResponse = MediaPreviewResponse(listOf(MediaPreview("", "https://images-assets.nasa.gov/image/ARC-2002-ACD02-0056-22/ARC-2002-ACD02-0056-22~thumb.jpg", ContentType.IMAGE, "", "")), 1, 1, 1)
        adapter = MediaPreviewAdapter(tmpMediaPreviewResponse,picasso,searchParams,callback = {updateMediaPreviews()})
        mediaPreviewRecyclerView.adapter = adapter
    }

    fun updateMediaPreviews(){
        viewModel.updateMediaPreviews()
    }

    companion object {
        const val TAG = "PreviewMediaFragment"
    }
}