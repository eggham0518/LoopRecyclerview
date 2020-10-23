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

package com.example.android.loopRecyclerview.domain

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Datum(
        val id : Int,
        val cmc_rank: Int,
        val name: String,
        val quote: Quote,
        val last_updated: String?
)

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
