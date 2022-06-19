package com.gs.apod.network

import com.gs.apod.db.NasaApodEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface
 */
interface NasaApodWebService {

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): Response<NasaApodEntity>

    @GET("planetary/apod/")
    suspend fun getPictureOfTheDay(@Query("date")date:String): Response<NasaApodEntity>

}