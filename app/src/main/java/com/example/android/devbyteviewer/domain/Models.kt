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

package com.example.android.devbyteviewer.domain

import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

/**
 * Videos represent a devbyte that can be played.
 */
data class Datum(
        val id : String,
        val cmc_rank: Int,
        val name: String,
        val quote: Quote,
        val last_updated: String?
)

val gson = Gson()

class QuotePersistentConverter {
    @TypeConverter
    fun storeRepoOwnerToString(data: Quote): String = gson.toJson(data)

    @TypeConverter
    fun storeStringToRepoOwner(value: String): Quote = gson.fromJson(value, Quote::class.java)
}

data class Quote(
        @SerializedName("USD")
        @Expose
        val usd: USD) {
}

data class USD(
        var price: Double?,
        val volume24h: Double?,
        val percentChange1h: Double?,
        val percentChange24h: Double?,
        val percentChange7d: Double?,
        val marketCap: Double?
)
