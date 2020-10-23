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

package com.example.android.loopRecyclerview.network

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.ByteArrayInputStream


interface Service_pro {
    @Headers("X-CMC_PRO_API_KEY: 3069b872-06b0-40f1-bf44-5a3f7f73d796")
    @GET("/v1/cryptocurrency/listings/latest?")
    suspend fun getDatumlist(
            @Query("start") start: Int,
            @Query("limit") limit: Int?
    ): NetworkDatumContainer
}

interface Service_s2 {
    @GET("/static/img/coins/64x64/{id}.png")
     fun getLogo(
            @Path("id") id: Int
    ): Call<ResponseBody>
}

var gson = GsonBuilder()
        .setLenient()
        .create()

object CoinNetwork {
    private val retrofit_pro = Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofit_s2 = Retrofit.Builder()
            .baseUrl("https://s2.coinmarketcap.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    val service_pro = retrofit_pro.create(Service_pro::class.java)
    val service_s2 = retrofit_s2.create(Service_s2::class.java)
}

