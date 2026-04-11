package com.workfort.pstuian.util.helper

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextHolder {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun get(): Context {
        return context ?: throw IllegalStateException("ContextHolder not initialized. Call ContextHolder.init(context) in your Application class.")
    }
}
