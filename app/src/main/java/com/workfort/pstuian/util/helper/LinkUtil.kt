package com.workfort.pstuian.util.helper

import android.content.Context
import android.content.Intent
import android.net.Uri

class LinkUtil(val context: Context) {

    fun callTo(number: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        context.startActivity(intent)
    }

    fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        context.startActivity(Intent.createChooser(intent, "Email via..."))
    }

    fun openBrowser(url: String) {
        var urlToOpen = url
        if (!urlToOpen.startsWith("http://") && !urlToOpen.startsWith("https://"))
            urlToOpen = "http://$urlToOpen"

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
        context.startActivity(Intent.createChooser(browserIntent, "Open with..."))
    }
}