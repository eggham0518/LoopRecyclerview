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

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a devbyte playlist.
 */
interface Service_pro {
    @Headers("X-CMC_PRO_API_KEY: 3069b872-06b0-40f1-bf44-5a3f7f73d796")
    @GET("/v1/cryptocurrency/listings/latest?")
    suspend fun getDatumlist(
            @Query("start") start: Int,
            @Query("limit") limit : Int?
    ): NetworkDatumContainer
}

interface Service_s2 {
    @GET("/static/img/coins/64x64/{id}.png")
    suspend fun getLogo(
            @Path("id") id: String
    ): NetworkDatumContainer
}

/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
object CoinNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit_pro = Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofit_s2 = Retrofit.Builder()
            .baseUrl("https://s2.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service_pro = retrofit_pro.create(Service_pro::class.java)
    val service_s2 = retrofit_s2.create(Service_pro::class.java)

}

