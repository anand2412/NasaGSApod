package com.gs.apod.network

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Network data source
 * @constructor NasaApodWebService
 */
@Singleton
class ApodNetworkDataSource @Inject constructor(
    private val nasaApodWebService: NasaApodWebService
) : BaseDataSource(){

    suspend fun fetchNasaApod()
            = getResult { nasaApodWebService.getPictureOfTheDay() }

    suspend fun fetchNasaApodByDate(date: String)
        = getResult { nasaApodWebService.getPictureOfTheDay(date) }

}