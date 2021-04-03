package com.nasa.app.ui.fragment_download_files

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.nasa.app.BaseApplication
import com.nasa.app.R
import javax.inject.Inject

class DownloadFilesFragment : DialogFragment() {
    private var emptyArgumentsErrorMessage = "Urls can't be empty"
    private var fileStringsUris: ArrayList<String>? = null
    private val filesUris = arrayListOf<Uri>()
    @Inject
    lateinit var downloadFilesAdapter: DownloadFilesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.getDownloadFilesComponent().create(requireContext()).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && requireArguments().containsKey(DOWNLOAD_DIALOG_FRAGMET)) {
            fileStringsUris = requireArguments().getStringArrayList(DOWNLOAD_DIALOG_FRAGMET)
        } else {
            throw IllegalArgumentException(emptyArgumentsErrorMessage)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_download_dialog, container, false)

        val listView = view.findViewById<ListView>(R.id.files_asset_list_view)
        fileStringsUris?.forEach {
            filesUris.add(Uri.parse(it))
        }

        downloadFilesAdapter.dataSource = filesUris

        listView.adapter = downloadFilesAdapter

        return view
    }

    companion object {
        const val DOWNLOAD_DIALOG_FRAGMET = "files_download_dialog_fragment"

        fun newInstance(urls: ArrayList<String>): DownloadFilesFragment {
            val args = Bundle()
            args.putStringArrayList(DOWNLOAD_DIALOG_FRAGMET, urls)
            val fragment = DownloadFilesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}



