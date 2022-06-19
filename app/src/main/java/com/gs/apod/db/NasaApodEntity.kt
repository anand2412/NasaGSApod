package com.gs.apod.db

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "nasaapod")
data class NasaApodEntity(

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @field:SerializedName("date")
    val date: Date? = null,

    @field:SerializedName("copyright")
    var copyright: String? = null,

    @field:SerializedName("media_type")
    val mediaType: String? = null,

    @field:SerializedName("hdurl")
    val hdurl: String? = null,

    @field:SerializedName("service_version")
    val serviceVersion: String? = null,

    @field:SerializedName("explanation")
    val explanation: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    //favorite field is added in existing db to avoid unnecessary space
    @field:SerializedName("isFavorite")
    var isFavorite: Int

) : Parcelable