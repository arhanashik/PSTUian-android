package com.workfort.pstuian.util.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.workfort.pstuian.R


class PlayStoreUtil (val context: Context) {
    fun openStore() {
        val appId = context.packageName
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=$appId")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            setPackage("com.android.vending")
        }
        context.startActivity(intent)
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
}