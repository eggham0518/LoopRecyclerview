package com.example.android.loopRecyclerview.util

import androidx.room.TypeConverter
import com.example.android.loopRecyclerview.database.gson
import com.example.android.loopRecyclerview.domain.Quote

class QuotePersistentConverter {

    @TypeConverter
    fun storeRepoOwnerToString(data: Quote): String = gson.toJson(data)

    @TypeConverter
    fun storeStringToRepoOwner(value: String): Quote = gson.fromJson(value, Quote::class.java)
}