package com.workfort.pstuian.util.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import com.workfort.pstuian.R


class PlayStoreUtil (val context: Context){

    fun checkForUpdate(
        applicationId: String) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.url_market_details) + applicationId)
                )
            )
        } catch (ex: android.content.ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.url_playstore_app) + applicationId)
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    R.string.install_google_play_store,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun showMoreApps(context: Context, @StringRes devName: Int) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.url_market_search_app) + context.getString(devName))
                )
            )
        } catch (ex: android.content.ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.url_playstore_search_app) + context.getString(devName))
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    R.string.install_google_play_store,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun rateMyApp(context: Context, applicationId: String) {
        try {
            val uri = Uri.parse(context.getString(R.string.url_market_details) + applicationId)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
                flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            else
                flags = flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
            intent.addFlags(flags)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            checkForUpdate(applicationId)
        }
    }
}