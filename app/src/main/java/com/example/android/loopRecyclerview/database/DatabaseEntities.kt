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

package com.example.android.loopRecyclerview.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.loopRecyclerview.domain.Datum
import com.example.android.loopRecyclerview.domain.Quote

@Entity
data class DatabaseDatum constructor(
        @PrimaryKey
        val id: Int,
        val cmc_rank: Int,
        val name: String,
        val quote: Quote,
        val last_updated: String?
)

fun List<DatabaseDatum>.asDomainModel(): List<Datum> {
    return map {
        Datum(
                id= it.id,
                cmc_rank = it.cmc_rank,
                name = it.name,
                quote = it.quote,
                last_updated = it.last_updated
        )
    }
}

