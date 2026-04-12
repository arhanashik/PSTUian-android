package com.workfort.pstuian.util.helper

expect class LinkUtil {
    fun callTo(number: String)
    fun sendEmail(email: String)
    fun openBrowser(url: String)
}
