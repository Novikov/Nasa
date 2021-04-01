package com.nasa.app.ui.fragment_media_preview


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.di.view_models.ViewModelProviderFactory
import com.nasa.app.ui.activity.Activity
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

        val currentSearchResultHashCode = viewModel.mediaPreviews.value.hashCode()
        viewModel.mediaPreviews.observe(viewLifecycleOwner, {
            if (currentSearchResultHashCode != it.hashCode()) {
                (mediaPreviewRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    0,
                    0
                )
            }
            if (it.mediaPreviewList.isNotEmpty()) {
                adapter.dataSource = it
                contentLayout.visibility = View.VISIBLE
            } else {
                activityContract?.showErrorMessage("Nothing found")
            }
        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> activityContract?.showProgressBar()
                NetworkState.LOADED -> activityContract?.hideProgressBar()
                NetworkState.NO_INTERNET -> {
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

    private fun initRecyclerView() {
        mediaPreviewRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MediaPreviewAdapter(viewModel,picasso,searchParams)
        mediaPreviewRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG = "PreviewMediaFragment"
    }
}