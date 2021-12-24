package com.workfort.pstuian.app.ui.splash.viewstate

import com.workfort.pstuian.app.data.local.device.DeviceEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 18:51.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class DeviceState {
    object Idle : DeviceState()
    object Loading : DeviceState()
    data class Success(val device: DeviceEntity) : DeviceState()
    data class Error(val message: String?) : DeviceState()
}

sealed class DevicesState {
    object Idle : DevicesState()
    object Loading : DevicesState()
    data class Success(val data: List<DeviceEntity>) : DevicesState()
    data class Error(val message: String) : DevicesState()
}
