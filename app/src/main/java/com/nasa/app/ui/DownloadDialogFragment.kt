package com.nasa.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.nasa.app.R


class DownloadDialogFragment : DialogFragment() {
    companion object {
        private val DOWNLOAD_DIALOG_FRAGMET = "app_error_dialog_fragment"

        fun newInstance(urls: ArrayList<String>): DownloadDialogFragment {
            val args = Bundle()
            args.putStringArrayList(DOWNLOAD_DIALOG_FRAGMET, urls)
            val fragment = DownloadDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var sUrls: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!=null && requireArguments().containsKey(DOWNLOAD_DIALOG_FRAGMET)){
            sUrls = requireArguments().getStringArrayList(DOWNLOAD_DIALOG_FRAGMET)
        }
        else {
            throw IllegalArgumentException("urls can't be empty")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_download_dialog, container, false)

        val listView = view.findViewById<ListView>(R.id.files_asset_list_view)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sUrls!!.toArray())

        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), (view as TextView).text, Toast.LENGTH_SHORT).show()
        }

        listView.adapter = adapter

        return view
    }
}