package com.gs.apod.viewmodel

import androidx.lifecycle.distinctUntilChanged
import com.gs.apod.data.resultLiveData
import com.gs.apod.db.NasaApodDao
import com.gs.apod.network.ApodNetworkDataSource
import javax.inject.Inject

class NasaApodRepository @Inject constructor(var nasaapodDao: NasaApodDao,
                                             var apodNetworkDataSource: ApodNetworkDataSource) {

    fun observeApod() = resultLiveData(
        databaseQuery = { nasaapodDao.getAllPictures() },
        networkCall = { apodNetworkDataSource.fetchNasaApod() },
        saveCallResult = { nasaapodDao.insertPicture(it) })
        .distinctUntilChanged()

    suspend fun fetchPictureByDate(date: String) =
           apodNetworkDataSource.fetchNasaApodByDate(date)
}