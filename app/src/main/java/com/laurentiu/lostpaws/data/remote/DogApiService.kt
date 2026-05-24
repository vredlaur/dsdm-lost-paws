package com.laurentiu.lostpaws.data.remote

import com.laurentiu.lostpaws.data.remote.dto.DogImageResponse
import retrofit2.http.GET

interface DogApiService {
    @GET("api/breeds/image/random")
    suspend fun getRandomDogImage(): DogImageResponse
}
