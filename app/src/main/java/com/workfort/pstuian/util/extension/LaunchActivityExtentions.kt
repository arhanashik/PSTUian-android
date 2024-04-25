package com.workfort.pstuian.util.extension

import android.content.Context
import android.content.Intent
import java.security.InvalidParameterException


inline fun <reified T : Any> Context.launchActivity(
    vararg extras: Pair<String, Any>,
    noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this, init).apply {
        extras.forEach {
            when(val value = it.second) {
                is String -> this.putExtra(it.first, value)
                is Int -> this.putExtra(it.first, value)
                else -> throw InvalidParameterException("Invalid type")
            }
        }
    }
    startActivity(intent)
}

inline fun <reified T : Any> newIntent(
    context: Context,
    noinline init: Intent.() -> Unit = {}): Intent {
    val intent = Intent(context, T::class.java)
    intent.init()
    return  intent
}