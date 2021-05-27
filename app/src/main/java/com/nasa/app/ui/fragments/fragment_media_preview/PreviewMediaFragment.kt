package com.nasa.app.ui.fragments.fragment_media_preview


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
import com.nasa.app.R
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.di.view_models.ViewModelProviderFactory
import com.nasa.app.ui.activity.Activity
import com.nasa.app.ui.activity.MainActivity
import com.nasa.app.ui.fragments.fragment_media_preview.di.PreviewComponent
import com.nasa.app.utils.SearchParams
import com.squareup.picasso.Picasso
import javax.inject.Inject

class PreviewMediaFragment : Fragment() {
    private var activityContract: Activity? = null
    private lateinit var viewModel: PreviewMediaViewModel
    lateinit var adapter: NewMediaPreviewAdapter

    lateinit var mediaPreviewComponent: PreviewComponent

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

        mediaPreviewComponent =  (requireActivity() as MainActivity).activityComponent.getPreviewComponent().create()
        mediaPreviewComponent.inject(this)
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.media_preview_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(12)
        adapter = NewMediaPreviewAdapter(requireContext())
        recyclerView.adapter = adapter

        viewModel.mediaPreviews.observe(viewLifecycleOwner, {
            Log.i(TAG, "media preview: ${it.hashCode()}")
            adapter.submitList(it)
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

    override fun onDestroy() {
        super.onDestroy()
        activityContract = null
    }

    companion object {
        const val TAG = "PreviewMediaFragment"
    }
}