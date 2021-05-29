package com.nasa.app.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nasa.app.BaseApplication
import com.nasa.app.R
import com.nasa.app.ui.activity.di.ActivityComponent
import com.nasa.app.ui.fragments.fragment_media_preview.initial.InitialPreviewMediaFragmentDirections
import com.nasa.app.ui.fragments.fragment_search_settings.SearchSettingsFragment
import com.nasa.app.utils.EMPTY_SEARCH_STRING
import com.nasa.app.utils.EMPTY_STRING
import com.nasa.app.utils.SearchParams
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Activity {
    private lateinit var progressBar: ProgressBar
    private lateinit var errorMessageTextView: TextView
    private var menuItem: MenuItem? = null
    private var isErrorMessageShoved = false
    lateinit var activityComponent:ActivityComponent

    @Inject
    lateinit var searchParams: SearchParams

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as BaseApplication).appComponent.getActivityComponent().create()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        errorMessageTextView = findViewById(R.id.msg_text_view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.Type_here_to_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                collapseSearchField()
                searchRequest(query ?: EMPTY_SEARCH_STRING)
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
                searchSettingsFragment.show(
                    supportFragmentManager,
                    getString(R.string.SearchSettingsFragmentTag)
                )
            } catch (ex: Exception) {
                Log.i(TAG, ex.message.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressBar() {
        progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = ProgressBar.INVISIBLE
    }

    override fun searchRequest(query: String) {
//        if (errorMessageTextView.visibility == View.VISIBLE) {
//            clearErrorMessage()
//        }
//        searchParams.initNewSearchRequestParams(query)
//
        val navController = findNavController(R.id.nav_host_fragment)
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val inflater = navHostFragment.navController.navInflater
//        val graph = inflater.inflate(R.navigation.app_navigation)
//        graph.startDestination = R.id.mediaFragment
//        navController.graph = graph
        navController?.navigate(
            InitialPreviewMediaFragmentDirections.actionMediaFragmentToFoundPreviewMediaFragment()
        )
    }

    override fun collapseSearchField() {
        if (menuItem != null) {
            if (menuItem!!.isActionViewExpanded) {
                menuItem!!.collapseActionView()
            }
        }
    }

    override fun showErrorMessage(msg: String) {
        errorMessageTextView.text = msg
        errorMessageTextView.visibility = View.VISIBLE
        isErrorMessageShoved = true
    }

    override fun clearErrorMessage() {
        errorMessageTextView.text = EMPTY_STRING
        errorMessageTextView.visibility = View.INVISIBLE
        isErrorMessageShoved = false
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }

    override fun isErrorMessageShoved(): Boolean {
        return isErrorMessageShoved
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}