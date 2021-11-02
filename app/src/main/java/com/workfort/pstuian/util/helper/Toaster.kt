package com.workfort.pstuian.util.helper

import android.content.Context
import android.widget.Toast
import com.workfort.pstuian.PstuianApp

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 11:15 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

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