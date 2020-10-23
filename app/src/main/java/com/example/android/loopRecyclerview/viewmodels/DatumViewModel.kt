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

package com.example.android.loopRecyclerview.viewmodels

import android.app.Application
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import android.text.TextUtils
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.loopRecyclerview.database.getDatabase
import com.example.android.loopRecyclerview.network.CoinNetwork
import com.example.android.loopRecyclerview.repository.DatumsRepository
import com.example.android.loopRecyclerview.ui.MainFragment
import com.example.android.loopRecyclerview.util.sdf
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.util.*

class DatumViewModel(application: Application) : AndroidViewModel(application) {

    private val datumsRepository = DatumsRepository(getDatabase(application), application)
    val datumlist = datumsRepository.datums

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val timer: CountDownTimer

    private val _isTimerFinished = MutableLiveData(false)
    val isTimerFinished: LiveData<Boolean>
        get() = _isTimerFinished

    companion object {
        private const val ONE_SEC = 1000L
        private const val COUNTDOWN_TIME = 6000 * 3L
    }

    init {
        refreshDataFromRepository()
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SEC) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                onTimerFinish()
                onTimerStart()
            }
        }
        onTimerStart()
    }

    fun onTimerStart() {
        _isTimerFinished.value = false
        timer.start()
    }

    fun onTimerFinish() {
        _isTimerFinished.value = true
    }

    fun refreshDataFromRepository(start: Int = 1, limit: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                if (!isNeedUpdate(getDate(start))) {
                    _isLoading.value = false
                    return@launch
                }

                datumsRepository.refreshDatums(start, limit)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (e: Exception) {
                _eventNetworkError.value = true
                _isLoading.value = false
                Timber.d("익셉션 ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun refreshLogo(logoID: Int, ivLogo: ImageView) {
        viewModelScope.launch {
            datumsRepository.getImage(logoID, ivLogo)
        }
    }

    private fun getDate(start: Int): Date? {
        val date: Date?
        val list = datumlist.value

        if (list.isNullOrEmpty() || start > list.size) {
            return null
        }

        val lastUpdateTime: String? = list.get(start).last_updated

        if (TextUtils.isEmpty(lastUpdateTime)) {
            return null
        }

        date = sdf.parse(lastUpdateTime!!)
        return date
    }

    private fun isNeedUpdate(date: Date?): Boolean {
        if (date == null) {
            return true
        }

        val currentTime = System.currentTimeMillis() //UTC +0
        val gap = currentTime - date.time

        Timber.d("updateCheck 3 $gap")
        if (gap > MainFragment.MIN * 2) {
            return true
        }
        Timber.d("updateCheck 4 $gap")
        return false
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
}
