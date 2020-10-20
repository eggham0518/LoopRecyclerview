/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.domain.Datum
import com.example.android.devbyteviewer.repository.DatumsRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class DatumViewModel(application: Application) : AndroidViewModel(application) {

    private val datumsRepository = DatumsRepository(getDatabase(application))
    var datumlist = datumsRepository.datums

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val timer: CountDownTimer

    companion object {
        private const val DONE = 0L
        private const val ONE_MIN = 1000 * 60L
        private const val COUNTDOWN_TIME = 60000L
    }

    init {
        refreshDataFromRepository()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_MIN) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                start()
            }
        }

        timer.start()
    }

    fun refreshDataFromRepository(start: Int = 1, limit: Int = 20) {
        viewModelScope.launch {
            try {
                datumsRepository.refreshDatums(start,limit)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
                _isLoading.value = true
            } catch (e: Exception) {
                if (datumlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
            _isLoading.value = false
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DatumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DatumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
