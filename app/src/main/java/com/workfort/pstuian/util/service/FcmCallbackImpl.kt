package com.workfort.pstuian.util.service

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.firebase.fcm.callback.FcmCallback
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.sharedpref.Prefs
import com.workfort.pstuian.util.helper.AndroidUtil
import kotlinx.coroutines.runBlocking

class FcmCallbackImpl(private val authRepo: AuthRepository) : FcmCallback {
    override fun onMessageReceived(data: Intent) {
        // update prefs to add new notification state
        Prefs.hasNewNotification = true

        AndroidUtil.vibrate()

        val context = PstuianApp.getBaseApplicationContext()
        LocalBroadcastManager.getInstance(context).also {
            it.sendBroadcast(data)
        }
    }

    override fun onNewToken(token: String) {
        runBlocking {
            try {
                authRepo.updateFcmToken(token)
            }catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}