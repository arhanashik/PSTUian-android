package com.workfort.pstuian.util.helper

import android.content.Intent
import android.net.Uri

actual class LinkUtil {
    private val context = ContextHolder.get()

    actual fun callTo(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    actual fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(intent, "Email via...").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    actual fun openBrowser(url: String) {
        var urlToOpen = url
        if (!urlToOpen.startsWith("http://") && !urlToOpen.startsWith("https://"))
            urlToOpen = "http://$urlToOpen"

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(browserIntent, "Open with...").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
