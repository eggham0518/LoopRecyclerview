package com.example.android.loopRecyclerview.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DatumViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatumViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}