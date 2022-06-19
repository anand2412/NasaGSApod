package com.gs.apod.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

/**
 * DAO interface for db CRUD operations
 */
@Dao
interface NasaApodDao {

    @Query("SELECT * FROM nasaapod ORDER BY date DESC")
    fun getAllPictures(): LiveData<List<NasaApodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(nasaApodEntity: NasaApodEntity) :Long

    @Update
    suspend fun update(nasaApodEntity: NasaApodEntity) :Int

    @Transaction
    suspend fun insertOrUpdate(entity: NasaApodEntity):Int  {
        if (insertPicture(entity) == -1L) {
            update(entity)
        }
        return 1
    }

    @Query("SELECT * FROM nasaapod WHERE isFavorite == 1 ORDER BY date DESC")
    fun getFavoriteList(): LiveData<List<NasaApodEntity>>
}