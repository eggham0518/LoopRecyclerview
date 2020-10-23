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

package com.example.android.loopRecyclerview.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.loopRecyclerview.database.DatumDao
import com.example.android.loopRecyclerview.database.asDomainModel
import com.example.android.loopRecyclerview.domain.Datum
import com.example.android.loopRecyclerview.network.CoinNetwork
import com.example.android.loopRecyclerview.network.asDatabaseModel
import com.example.android.loopRecyclerview.util.LruCacheManager
import com.example.android.loopRecyclerview.util.LruCacheManager.Companion.getDiskCacheDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatumsRepository(private val database: DatumDao.DatumsDatabase, val context: Context) {
    val datums: LiveData<List<Datum>> = Transformations.map(database.datumDao.getDatums()) {
        it.asDomainModel()
    }

    val lruCacheManager = LruCacheManager.openCache(getDiskCacheDir(context, DISK_CACHE_SUBDIR), DISK_CACHE_SIZE)

    suspend fun refreshDatums(start: Int = 1, limit: Int? = 20) {
        withContext(Dispatchers.IO) {
            val datumList = CoinNetwork.service_pro.getDatumlist(start, limit)
            database.datumDao.insertAll(datumList.asDatabaseModel())
        }
    }

    suspend fun getImage(logoID: Int, ivLogo: ImageView?) {
        var cacheBitmap: Bitmap? = lruCacheManager?.get("$logoID")
        if (cacheBitmap != null) {
            ivLogo?.setImageBitmap(cacheBitmap)
            return
        }

        withContext(Dispatchers.IO) {
            val responseBody = CoinNetwork.service_s2.getLogo(logoID).execute().body()
            val inputStream = responseBody?.byteStream()

            cacheBitmap = BitmapFactory.decodeStream(inputStream)

            lruCacheManager?.put("$logoID", cacheBitmap)
        }
        ivLogo?.setImageBitmap(cacheBitmap)
    }

    companion object {
        private const val DISK_CACHE_SIZE = 1024 * 1024 * 10L // 10MB
        private const val DISK_CACHE_SUBDIR = "thumbnails"
    }
}
