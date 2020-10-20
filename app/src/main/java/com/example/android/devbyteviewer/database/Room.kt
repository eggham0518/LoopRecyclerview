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

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.devbyteviewer.domain.QuotePersistentConverter

@Dao
interface DatumDao {
    @Query("select * from databasedatum order by cmc_rank asc" )
    fun getDatums(): LiveData<List<DatabaseDatum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<DatabaseDatum>)

    @Database(entities = [DatabaseDatum::class], version = 1)
    @TypeConverters(QuotePersistentConverter::class)
    abstract class DatumsDatabase : RoomDatabase() {
        abstract val datumDao: DatumDao
    }
}

private lateinit var INSTANCE: DatumDao.DatumsDatabase

fun getDatabase(context: Context): DatumDao.DatumsDatabase {
    synchronized(DatumDao.DatumsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DatumDao.DatumsDatabase::class.java,
                    "datums").build()
        }
    }
    return INSTANCE
}

