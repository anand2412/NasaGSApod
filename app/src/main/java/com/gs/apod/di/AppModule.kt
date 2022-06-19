package com.gs.apod.di

import android.content.Context
import androidx.room.Room
import com.gs.apod.BuildConfig
import com.gs.apod.db.NasaApodDao
import com.gs.apod.db.NasaApodDb
import com.gs.apod.network.NasaApodWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val APOD_BASE_URL = "https://api.nasa.gov/"

    @Provides
    @Singleton
    fun provideApodDb(@ApplicationContext context: Context): NasaApodDb = Room
        .databaseBuilder(context, NasaApodDb::class.java, "apodapp.db")
        .build()

    @Provides
    @Singleton
    fun provideApodDao(apodDb: NasaApodDb): NasaApodDao = apodDb.NasaApodDao()


    @Provides
    @Singleton
    fun provideApodService(retrofit: Retrofit): NasaApodWebService =
        retrofit.create(NasaApodWebService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(APOD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor{chain -> return@addInterceptor addApiKeyToRequests(chain)}
            .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_APOD_KEY).build()
        request.url(newUrl)
        return chain.proceed(request.build())
    }
}