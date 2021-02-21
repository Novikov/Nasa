package com.nasa.app.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nasa.app.R

class MainActivity : AppCompatActivity(), IActivity {
    lateinit var mProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.progressBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Type here to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                menuItem.collapseActionView()
                query.let {
                    SEARCH_REQUEST_QUERY = it!!
                }
                val navController = findNavController(R.id.nav_host_fragment)
                val navHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val inflater = navHostFragment.navController.navInflater
                val graph = inflater.inflate(R.navigation.app_navigation)
                graph.startDestination = R.id.mediaFragment
                navController.graph = graph
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun showProgressBar() {
        Log.e("ProgressBar", "Loading")
        mProgress.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        Log.e("ProgressBar", "Hide")
        mProgress.visibility = ProgressBar.INVISIBLE
    }

    override fun showErrorDialog(msg: String) {
        try {
            val errorDialogFragment = ErrorDialogFragment.newInstance(msg)
            errorDialogFragment.show(supportFragmentManager, "ErrorDialogFragment")
        }
        catch (ex: Exception){
            Log.e("MainActivity", ex.message.toString())
        }
    }


}