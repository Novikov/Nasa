package com.nasa.app.ui.media_detail

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

open class DetailFragment : Fragment() {
    protected fun getViewModel(nasaId: String, detailMediaRepository: DetailMediaRepository): DetailMediaViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailMediaViewModel(detailMediaRepository, nasaId) as T
            }
        })[DetailMediaViewModel::class.java]
    }
}