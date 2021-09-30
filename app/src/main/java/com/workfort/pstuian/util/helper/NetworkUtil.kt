package com.workfort.pstuian.util.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import com.workfort.pstuian.PstuianApp


object NetworkUtil {

    private val mNetworkStateLiveData = MutableLiveData<Boolean>()
    private var mPrevState = isNetworkAvailable()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            val currState = isNetworkAvailable()
            if(currState == mPrevState) return

            mNetworkStateLiveData.postValue(currState)
            mPrevState = currState
        }
    }

    @Suppress("DEPRECATION")
    fun from(context: Context): MutableLiveData<Boolean> {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, intentFilter)

        refresh()

        return mNetworkStateLiveData
    }

    fun refresh() {
        mNetworkStateLiveData.value = isNetworkAvailable()
    }

    fun unregister(context: Context) = try {
        context.unregisterReceiver(receiver)
    } catch (ex: IllegalArgumentException) {
        ex.printStackTrace()
    }

    private fun getActiveNetworkInfo(): NetworkInfo? {
        val context = PstuianApp.getBaseApplicationContext()
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager?

        return connectivityManager?.activeNetworkInfo
    }

    fun isNetworkAvailable(): Boolean {
        val info = getActiveNetworkInfo()

        return info != null && info.isConnected
    }

    fun hasNoNetwork(): Boolean = !isNetworkAvailable()
}