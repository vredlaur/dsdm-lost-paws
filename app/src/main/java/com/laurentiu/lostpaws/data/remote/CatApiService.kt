package com.laurentiu.lostpaws.data.remote

import com.laurentiu.lostpaws.data.remote.dto.CatImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getCatImages(@Query("limit") limit: Int = 10): List<CatImageResponse>
}
