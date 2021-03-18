package com.nasa.app.ui.media_preview


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.NavController
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
    lateinit var navController: NavController
    private lateinit var viewModel: PreviewMediaViewModel

    @Inject lateinit var previewMediaRepository: PreviewMediaRepository

    @Inject lateinit var providerFactory: ViewModelProviderFactory

    val TAG = "PreviewMediaFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "onAttach: ")
        try {
            activityContract = context as Activity
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Activity have to implement interface IActivityView")
        }

        ((activityContract as MainActivity).application as BaseApplication).appComponent.getPreviewComponent().inject(this)
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

        val mediaPreviewRecyclerView =
            view.findViewById<RecyclerView>(R.id.media_preview_recycler_view)
        mediaPreviewRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.mediaPreviews.observe(viewLifecycleOwner, {
            if (it.mediaPreviewList.isNotEmpty()) {
                val adapter = MediaPreviewAdapter(it, previewMediaRepository)
                mediaPreviewRecyclerView.adapter = adapter
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

    private fun getViewModel(): PreviewMediaViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PreviewMediaViewModel(previewMediaRepository) as T
            }
        })[PreviewMediaViewModel::class.java]
    }
}