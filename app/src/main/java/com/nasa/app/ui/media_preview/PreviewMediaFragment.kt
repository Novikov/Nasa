package com.nasa.app.ui.media_preview


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
import com.nasa.app.ui.Activity
import com.nasa.app.ui.MainActivity
import javax.inject.Inject

class PreviewMediaFragment : Fragment() {
    private var activityContract: Activity? = null
    private lateinit var viewModel: PreviewMediaViewModel
    lateinit var mediaPreviewRecyclerView:RecyclerView

    @Inject lateinit var providerFactory: ViewModelProviderFactory
    @Inject lateinit var adapter:MediaPreviewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(Companion.TAG, "onAttach: ")
        try {
            activityContract = context as Activity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }

        (requireActivity().application as BaseApplication).appComponent.getPreviewComponent().create().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, providerFactory).get(PreviewMediaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityContract?.clearMsg()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_media_preview, container, false)

        val contentLayout = view.findViewById<ConstraintLayout>(R.id.content_layout)
        contentLayout.visibility = View.INVISIBLE

        mediaPreviewRecyclerView = view.findViewById(R.id.media_preview_recycler_view)
        initRecyclerView()

        viewModel.mediaPreviews.observe(viewLifecycleOwner, {
            if (it.mediaPreviewList.isNotEmpty()) {
                adapter.dataSource = it
                contentLayout.visibility = View.VISIBLE
            } else {
                activityContract?.showMsg("Nothing found")
            }
        })

        //network state status observing
        viewModel.networkState.observe(viewLifecycleOwner, {
            when (it) {
                NetworkState.LOADING -> activityContract?.showProgressBar()
                NetworkState.LOADED -> activityContract?.hideProgressBar()
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

    fun initRecyclerView(){
        mediaPreviewRecyclerView.layoutManager = LinearLayoutManager(context)
        mediaPreviewRecyclerView.adapter = adapter
    }

    companion object {
        const val TAG = "PreviewMediaFragment"
    }
}