package com.nasa.app.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nasa.app.R


class MainActivity : AppCompatActivity(), IActivity {
    lateinit var mProgress: ProgressBar
    lateinit var msgTextView: TextView
    var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.progressBar)
        msgTextView = findViewById(R.id.msg_text_view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        menuItem = menu?.findItem(R.id.action_search)

        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Type here to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                collapseSearchField()
                searchRequest(query ?: "apollo")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.search_settings) {
            collapseSearchField()
            try {
                val errorDialogFragment = SearchSettingsFragment.newInstance("Hello")
                errorDialogFragment.show(supportFragmentManager, "ErrorDialogFragment")
            } catch (ex: Exception) {
                Log.i("MainActivity", ex.message.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressBar() {
        Log.i("ProgressBar", "Loading")
        mProgress.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        Log.i("ProgressBar", "Hide")
        mProgress.visibility = ProgressBar.INVISIBLE
    }

    override fun searchRequest(query: String) {
        clearMsg()
        SEARCH_REQUEST_QUERY = query
        SEARCH_PAGE = 1
        val navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.app_navigation)
        graph.startDestination = R.id.mediaFragment
        navController.graph = graph
    }

    override fun collapseSearchField() {
        if (menuItem != null) {
            if (menuItem!!.isActionViewExpanded) {
                menuItem!!.collapseActionView()
            }
        }
    }

    override fun showMsg(msg: String) {
        msgTextView.setText(msg)
        msgTextView.visibility = View.VISIBLE
    }

    override fun clearMsg() {
        msgTextView.setText("")
        msgTextView.visibility = View.INVISIBLE
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }
}