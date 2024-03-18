package com.workfort.pstuian.util.helper

import android.content.Context
import android.widget.Toast
import com.workfort.pstuian.PstuianApp

object Toaster {

    private fun getDefaultContext(): Context {
        return PstuianApp.getBaseApplicationContext()
    }

    fun show(message: String, context: Context = getDefaultContext()) {
        show(context, message, Toast.LENGTH_SHORT)
    }

    fun show(message: Int, context: Context = getDefaultContext()) {
        show(context, message, Toast.LENGTH_SHORT)
    }

    fun showLong(message: String, context: Context = getDefaultContext()) {
        show(context, message, Toast.LENGTH_LONG)
    }

    fun showLong(message: Int, context: Context = getDefaultContext()) {
        show(context, message, Toast.LENGTH_LONG)
    }

    private fun show(context: Context, message: String, length: Int) {
        Toast.makeText(context, message, length).show()
    }

    private fun show(context: Context, message: Int, length: Int) {
        Toast.makeText(context, message, length).show()
    }
}