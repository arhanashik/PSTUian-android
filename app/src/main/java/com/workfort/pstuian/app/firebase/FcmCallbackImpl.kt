package com.workfort.pstuian.app.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.R
import com.workfort.pstuian.featuredomain.model.NotificationType
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import kotlin.random.Random

class FcmCallbackImpl(
    private val sharedPrefRepository: SharedPrefRepository,
) : FcmCallback {

    override fun onMessageReceived(data: FcmMessageData) {
        // TODO: update prefs to add new notification state
//        prefs.hasNewNotification = true
        handleNotification(data)
    }

    override fun onNewToken(token: String) {
        sharedPrefRepository.putString(SharedPrefKey.FCM_TOKEN, token)
    }

    /**
     * Show notification when app is open
     * */
    private fun handleNotification(data: FcmMessageData) {
        val context = PstuianApp.getBaseApplicationContext()
        val showNotification = sharedPrefRepository.getBoolean(SharedPrefKey.SHOW_NOTIFICATION, true)
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED || showNotification
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

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            manager.createNotificationChannel(channel)
        }

        manager.notify(Random.nextInt(), notification)
    }
}