package com.workfort.pstuian.util.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.ui.webview.WebViewActivity
import com.workfort.pstuian.util.extension.launchActivity

class PlayStoreUtil (val context: Context) {
    fun openStore(appId: String = context.packageName) {
        val url = "https://play.google.com/store/apps/details?id=$appId"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setPackage("com.android.vending")
        }
        if(context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            != null) {
            context.startActivity(intent)
        } else {
            context.launchActivity<WebViewActivity>(Pair(Const.Key.URL, url))
        }
    }

    fun showMoreApps(@StringRes devName: Int) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.url_market_search_app) + context.getString(devName))
                )
            )
        } catch (ex: ActivityNotFoundException) {
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