package com.gs.apod.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gs.apod.db.NasaApodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.gs.apod.data.Result
import javax.inject.Inject

@HiltViewModel
class NasaApodViewModel @Inject constructor(
    private val nasaApodRepository: NasaApodRepository,
    private val nasaApodFavoriteRepository: NasaApodFavoriteRepository) :ViewModel() {

    val nasaApod by lazy { nasaApodRepository.observeApod() }

    val fetchByDateResult = MutableLiveData<Result<NasaApodEntity>>()
    val updateResult = MutableLiveData<Int>()

    fun fetchByDateResult(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = nasaApodRepository.fetchPictureByDate(date)
            fetchByDateResult.postValue(result)
        }
    }

    fun updateFavorite(nasaApodEntity: NasaApodEntity, isFavorite : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if(isFavorite) {
                nasaApodEntity.isFavorite = 1
            } else {
                nasaApodEntity.isFavorite = 0
            }
            val res = nasaApodFavoriteRepository.updateFavorite(nasaApodEntity)
            updateResult.postValue(res)
        }
    }

    fun getFavoriteList(): LiveData<List<NasaApodEntity>> {
        return nasaApodFavoriteRepository.favoriteList()
    }
}