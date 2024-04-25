package com.workfort.pstuian.util.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.repository.SettingsRepository
import com.workfort.pstuian.firebase.fcm.FcmMessageData
import com.workfort.pstuian.firebase.fcm.callback.FcmCallback
import com.workfort.pstuian.model.NotificationType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.sharedpref.Prefs
import com.workfort.pstuian.util.helper.AndroidUtil
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class FcmCallbackImpl(
    private val authRepo: AuthRepository,
    private val settingsRepo: SettingsRepository,
) : FcmCallback {

    override fun onMessageReceived(data: FcmMessageData) {
        // update prefs to add new notification state
        Prefs.hasNewNotification = true

        AndroidUtil.vibrate()
        handleNotification(data)
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

    /**
     * Show notification when app is open
     * */
    private fun handleNotification(data: FcmMessageData) {
        val context = PstuianApp.getBaseApplicationContext()
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED || settingsRepo.shouldShowNotification().not()
        ) {
            return
        }

        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)

        val type = data.type?.let { NotificationType.create(it) } ?: NotificationType.DEFAULT
        val iconRes = when(type) {
            NotificationType.DEFAULT -> R.drawable.ic_bell_filled
            NotificationType.BLOOD_DONATION -> R.drawable.ic_blood_drop
            NotificationType.NEWS -> R.drawable.ic_newspaper
            NotificationType.HELP -> R.drawable.ic_hand_heart
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(data.title.ifEmpty { context.getString(R.string.txt_notification) })
            .setContentText(data.message.ifEmpty { "New notification received!" })
            .setSmallIcon(iconRes)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(Random.nextInt(), notification)
    }
}