package com.nasa.app.ui.fragments.fragment_media_preview.initial


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.nasa.app.utils.EMPTY_SEARCH_STRING
import com.nasa.app.utils.SearchParams
import kotlinx.android.synthetic.main.fragment_media_preview.*
import javax.inject.Inject

class InitialPreviewMediaFragment : Fragment() {
    private var activityContract: Activity? = null
    private lateinit var viewModelInitial: InitialPreviewMediaViewModel
    lateinit var mediaPreviewComponent: PreviewComponent

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var searchParams: SearchParams

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
        setHasOptionsMenu(true)
        viewModelInitial =
            ViewModelProviders.of(this, providerFactory).get(InitialPreviewMediaViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.initial_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.Type_here_to_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchParams.clearSearchParams()
                searchParams.initNewSearchRequestParams(query!!)
                activityContract?.searchRequest()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!activityContract?.isActionBarShowing()!!){
            activityContract?.showActionBar()
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_media_preview, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.media_preview_recycler_view)

        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val adapterInitial = InitialMediaPreviewAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterInitial

        viewModelInitial.initialMediaPreviews.observe(viewLifecycleOwner, {
            Log.i(TAG, "media preview: ${it.hashCode()}")
            adapterInitial.submitList(it)
        })

        //network state status observing
        viewModelInitial.networkState.observe(viewLifecycleOwner, {
            progress_bar_popular.visibility = if (viewModelInitial.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (viewModelInitial.listIsEmpty()){
                when(it){
                    NetworkState.NOTHING_FOUND -> {
                        txt_error_popular.text = NetworkState.NOTHING_FOUND.msg
                        txt_error_popular.visibility = View.VISIBLE
                    }
                    NetworkState.NO_INTERNET -> {
                        txt_error_popular.text = NetworkState.NO_INTERNET.msg
                        txt_error_popular.visibility = View.VISIBLE
                    }
                    NetworkState.TIMEOUT -> {
                        txt_error_popular.text = NetworkState.TIMEOUT.msg
                        txt_error_popular.visibility = View.VISIBLE
                    }
                    NetworkState.ERROR -> {
                        txt_error_popular.text = NetworkState.ERROR.msg
                        txt_error_popular.visibility = View.VISIBLE
                    }
                }
            }

            if (!viewModelInitial.listIsEmpty()) {
                adapterInitial.setNetworkState(it)
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