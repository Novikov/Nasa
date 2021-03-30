package com.nasa.app.ui.fragment_download_files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.nasa.app.R

class DownloadFilesFragment : DialogFragment() {
    companion object {
        private val DOWNLOAD_DIALOG_FRAGMET = "app_error_dialog_fragment"

        fun newInstance(urls: ArrayList<String>): DownloadFilesFragment {
            val args = Bundle()
            args.putStringArrayList(DOWNLOAD_DIALOG_FRAGMET, urls)
            val fragment = DownloadFilesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var fileUrls: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && requireArguments().containsKey(DOWNLOAD_DIALOG_FRAGMET)) {
            fileUrls = requireArguments().getStringArrayList(DOWNLOAD_DIALOG_FRAGMET)
        } else {
            throw IllegalArgumentException("Urls can't be empty")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_download_dialog, container, false)

        val listView = view.findViewById<ListView>(R.id.files_asset_list_view)

        val adapter = DownloadFilesAdapter(requireContext(), fileUrls!!)

        listView.adapter = adapter

        return view
    }
}



