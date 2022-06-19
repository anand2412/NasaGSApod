package com.gs.apod.viewmodel

import com.gs.apod.db.NasaApodDao
import com.gs.apod.db.NasaApodEntity
import javax.inject.Inject

class NasaApodFavoriteRepository @Inject constructor(private var nasaApodDao: NasaApodDao) {

    //update favorite
    suspend fun updateFavorite(entity: NasaApodEntity) =
         nasaApodDao.insertOrUpdate(entity)

    fun favoriteList() = nasaApodDao.getFavoriteList() //get favorite list


}