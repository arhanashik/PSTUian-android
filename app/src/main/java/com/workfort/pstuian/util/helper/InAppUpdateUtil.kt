package com.workfort.pstuian.util.helper

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Oct, 2021 at 4:03 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/16/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class InAppUpdateUtil(val context: Context) {
    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    fun checkForUpdate(callback: AppUpdateCheckCallback) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                callback.onDownloadedUpdateAvailable()
                return@addOnSuccessListener
            }
            when(appUpdateInfo.updateAvailability()) {
                UpdateAvailability.UPDATE_AVAILABLE -> {
                    val isImmediate = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                    // 0-1: default/low priority, 2-3: medium priority, 4-5: high priority
                    val needForceUpdate = appUpdateInfo.updatePriority() >= 4
                    callback.onAvailable(appUpdateInfo, needForceUpdate, isImmediate)
                }
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    callback.onAlreadyInProgress(appUpdateInfo)
                }
                else -> {
                    callback.onUpdated()
                }
            }
        }
            .addOnFailureListener { exception -> callback.onError(exception)}
    }

    fun requestUpdate(
        activity: Activity,
        appUpdateInfo: AppUpdateInfo,
        needForceUpdate: Boolean,
        requestCode: Int,
        listener: InstallStateUpdatedListener? = null
    ): AppUpdateManager {
        if(listener != null) appUpdateManager.registerListener(listener)
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            if(needForceUpdate) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE,
            activity,
            requestCode
        )

        return appUpdateManager
    }

    fun removeListener(listener: InstallStateUpdatedListener) {
        appUpdateManager.unregisterListener(listener)
    }

    fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }

    abstract class AppUpdateCheckCallback {
        open fun onUpdated() = Unit
        open fun onDownloadedUpdateAvailable() = Unit
        open fun onAvailable(
            appUpdateInfo: AppUpdateInfo,
            needForceUpdate: Boolean,
            isImmediate: Boolean
        ) = Unit
        open fun onAlreadyInProgress(appUpdateInfo: AppUpdateInfo) = Unit
        open fun onError(exception: Exception) = Unit
    }

    interface DownloadedUpdateAvailableCallback {
        fun onSuccess(isAvailable: Boolean)
    }
}