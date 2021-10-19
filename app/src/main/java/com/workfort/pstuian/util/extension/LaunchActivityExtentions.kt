package com.workfort.pstuian.util.extension

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 7:39 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Any> Activity.launchActivity(
    extra: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    startActivity(newIntent<T>(this, init), extra)
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    extra: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    startActivityForResult(newIntent<T>(this, init), requestCode, extra)
}

inline fun <reified T : Any> Context.launchActivity(
    extra: Bundle? = null,
    noinline init: Intent.() -> Unit = {}) {

    startActivity(newIntent<T>(this, init), extra)
}

inline fun <reified T : Any> newIntent(
    context: Context,
    noinline init: Intent.() -> Unit = {}): Intent {
    val intent = Intent(context, T::class.java)
    intent.init()
    return  intent
}

inline fun <reified T : Any, reified M : Any> newIntent(
    context: Context,
    model: M,
    noinline init: Intent.() -> Unit = {}): Intent {
    val intent = Intent(context, T::class.java)

    intent.init()
    return  intent
}