package com.workfort.pstuian.util.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:40 PM.
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

object PermissionUtil {

    private const val REQUEST_CODE_PERMISSION_DEFAULT = 1

    fun request(
        activity: Activity,
        requestCode: Int = REQUEST_CODE_PERMISSION_DEFAULT,
        vararg permissions: String
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val finalArgs = ArrayList<String>()
        for (permission in permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(permission)
            }
        }

        if (finalArgs.isEmpty()) return

        ActivityCompat.requestPermissions(activity, finalArgs.toTypedArray(), requestCode)
    }

    fun request(
        fragment: Fragment,
        requestCode: Int = REQUEST_CODE_PERMISSION_DEFAULT,
        vararg permissions: String
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val finalArgs = ArrayList<String>()
        for (aStr in permissions) {
            if (fragment.context?.checkSelfPermission(aStr) != PackageManager.PERMISSION_GRANTED) {
                finalArgs.add(aStr)
            }
        }

        if (finalArgs.isEmpty()) return

        fragment.requestPermissions(finalArgs.toTypedArray(), requestCode)
    }

    fun isAllowed(context: Context, permission: String): Boolean {

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}