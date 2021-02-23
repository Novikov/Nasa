package com.nasa.app.ui.media_preview


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nasa.app.R
import com.nasa.app.data.api.NasaApiClient
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.IActivity
import com.nasa.app.ui.media_detail.DetailMediaRepository
import com.nasa.app.ui.media_detail.DetailMediaViewModel
import org.w3c.dom.Text

class PreviewMediaFragment : Fragment() {
    private var activityContract: IActivity? = null
    lateinit var navController: NavController
    lateinit var previewMediaRepository: PreviewMediaRepository
    private lateinit var viewModel: PreviewMediaViewModel

    val TAG = "PreviewMediaFragment"

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

        val apiService = NasaApiClient.getClient()
        previewMediaRepository = PreviewMediaRepository(apiService)
        viewModel = getViewModel()
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
            if (it.isNotEmpty()) {
                val adapter = MediaPreviewAdapter(it)
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