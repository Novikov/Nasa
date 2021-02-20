package com.nasa.app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
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

//        val adapter = ArrayAdapter(requireContext(), R.layout.download_list_view_item, sUrls!!.toArray())
        val adapter = DownloadFilesAdapter(requireContext(),sUrls!!)

        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), (view as TextView).text, Toast.LENGTH_SHORT).show()
        }

        listView.adapter = adapter

        return view
    }
}

class DownloadFilesAdapter (private val context: Context,
                            private val dataSource: ArrayList<String>) : BaseAdapter(){
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.download_list_view_item, parent, false)
            holder = ViewHolder()
            holder.urlTextView = view.findViewById(R.id.download_fragment_item_text_view) as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val textViewText = dataSource.get(position).substringAfter("~")
        holder.urlTextView.setText(textViewText)

        holder.urlTextView.setOnClickListener {
            val address: Uri = Uri.parse(dataSource.get(position))
            val intent = Intent(Intent.ACTION_VIEW, address)
            startActivity(context,intent,null)
        }

        return view
    }

    private class ViewHolder {
        lateinit var urlTextView: TextView
    }

}

