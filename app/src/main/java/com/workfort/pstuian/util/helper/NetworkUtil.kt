package com.workfort.pstuian.util.helper

import android.content.Context
import android.net.ConnectivityManager
import com.workfort.pstuian.PstuianApp


class NetworkUtil {
    companion object {
        fun isNetworkAvailable(): Boolean {
            val cm = PstuianApp.getBaseApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
        }
    }
}