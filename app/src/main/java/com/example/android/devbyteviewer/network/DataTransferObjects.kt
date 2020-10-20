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

package com.example.android.devbyteviewer.network

import com.example.android.devbyteviewer.database.DatabaseDatum
import com.example.android.devbyteviewer.domain.Datum
import com.example.android.devbyteviewer.domain.Quote
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkDatumContainer(val data: List<NetworkDatum>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkDatum(
        val id: String,
        val cmc_rank: Int,
        val name: String,
        val quote: Quote,
        val last_updated: String?
)

/**
 * Convert Network results to database objects
 */
fun NetworkDatumContainer.asDomainModel(): List<Datum> {
    return data.map {
        Datum(
                id = it.id,
                cmc_rank = it.cmc_rank,
                name = it.name,
                quote = it.quote,
                last_updated = it.last_updated
        )
    }
}

fun NetworkDatumContainer.asDatabaseModel(): List<DatabaseDatum> {
    return data.map {
        DatabaseDatum(
                id = it.id,
                cmc_rank = it.cmc_rank,
                name = it.name,
                quote = it.quote,
                last_updated = it.last_updated
        )
    }
}
