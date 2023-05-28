package com.example.premierleague.core.data.source.remote

import com.example.premierleague.core.data.model.api.MainResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MainService {
    @GET("competitions/2021/matches")
    suspend fun getMatches(
        @Header("X-Auth-Token") token: String = "4aabf4ea7664444c9794b68f5af117cb",
        @Query("dateFrom") dateFrom: String? = null,
        @Query("dateTo") dateTo: String? = null,
        @Query("status") status: String? = null
    ): MainResponse
}