package com.nasa.app.ui.activity

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
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.utils.SearchParams
import com.nasa.app.ui.fragment_search_settings.SearchSettingsFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Activity {
    lateinit var mProgress: ProgressBar
    lateinit var msgTextView: TextView
    var menuItem: MenuItem? = null
    @Inject
    lateinit var searchParams: SearchParams

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.getActivityComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.progressBar)
        msgTextView = findViewById(R.id.msg_text_view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.Type_here_to_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                collapseSearchField()
                searchRequest(query ?: "\"\"")
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
                val searchSettingsFragment = SearchSettingsFragment.newInstance()
                searchSettingsFragment.show(supportFragmentManager, "SearchSettingsFragment")
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
        searchParams.searchRequestQuery = query
        searchParams.searchPage = 1
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
        msgTextView.text = msg
        msgTextView.visibility = View.VISIBLE
    }

    override fun clearMsg() {
        msgTextView.text = ""
        msgTextView.visibility = View.INVISIBLE
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }
}