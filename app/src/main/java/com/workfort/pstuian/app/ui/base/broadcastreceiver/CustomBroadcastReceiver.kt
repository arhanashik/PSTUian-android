package com.workfort.pstuian.app.ui.common.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Oct, 2021 at 0:48.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/30.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BroadcastReceiver(
    private val callback: BroadcastReceiverCallback
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        callback.onReceived(intent)
    }
}

interface BroadcastReceiverCallback {
    fun onReceived(intent: Intent?)
}