package com.workfort.pstuian.util

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Logger {
    
    fun i(message: String, tag: String? = null) {
        Napier.i(message, tag = tag)
    }
    
    fun d(message: String, tag: String? = null) {
        Napier.d(message, tag = tag)
    }
    
    fun w(message: String, tag: String? = null) {
        Napier.w(message, tag = tag)
    }
    
    fun e(message: String, tag: String? = null) {
        Napier.e(message, tag = tag)
    }
    
    fun v(message: String, tag: String? = null) {
        Napier.v(message, tag = tag)
    }
    
    fun init() {
        Napier.base(DebugAntilog())
    }
}
