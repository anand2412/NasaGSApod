package com.gs.apod.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gs.apod.R
import com.gs.apod.utils.LogUtil.loge
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getCurrentDate(calendar: Calendar): String {
        return "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DATE)}"
    }

    fun bindImageFromUrl(context: Context, apodImageview: ImageView, url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(apodImageview)
    }

    fun convertStringToDate(givenDate: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date = Date()
        try {
            date = format.parse(givenDate)
        } catch (e: ParseException) {
            loge(TAG, "Error in converting date")
        }
        return date
    }

    fun convertDateFormat(date: Date?): String {
        if (date == null) return ""
        val spf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return spf.format(date)
    }

    fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false }

    private const val TAG: String =  "NasaUtils"

}