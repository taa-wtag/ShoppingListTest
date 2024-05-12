package com.example.shoppinglisttest.data.remote

import com.example.shoppinglisttest.BuildConfig
import com.example.shoppinglisttest.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("https://pixabay.com/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}