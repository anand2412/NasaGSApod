package com.gs.apod.utils

import android.util.Log

object LogUtil {

    private const val DEBUG = true

    fun loge(tag: String, message: String) {
        if(DEBUG) {
            Log.e(tag, message)
        }
    }

    fun logw(tag: String, message: String) {
        if(DEBUG) {
            Log.w(tag, message)
        }
    }

    fun logd(tag: String, message: String) {
        if(DEBUG) {
            Log.d(tag, message)
        }
    }

    fun logi(tag: String, message: String) {
        if(DEBUG) {
            Log.i(tag, message)
        }
    }
}