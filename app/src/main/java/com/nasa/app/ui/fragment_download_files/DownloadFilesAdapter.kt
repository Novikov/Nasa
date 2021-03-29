package com.nasa.app.ui.fragment_download_files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nasa.app.R

class DownloadFilesAdapter(
    private val context: Context,
    private val dataSource: ArrayList<String>
) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
            holder.urlTextView =
                view.findViewById(R.id.download_fragment_item_text_view) as TextView
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
            ContextCompat.startActivity(context, intent, null)
        }

        return view
    }

    private class ViewHolder {
        lateinit var urlTextView: TextView
    }

}