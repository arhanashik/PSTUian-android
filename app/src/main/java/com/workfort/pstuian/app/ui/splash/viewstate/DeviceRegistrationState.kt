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
sealed class DeviceRegistrationState {
    object Idle : DeviceRegistrationState()
    object Loading : DeviceRegistrationState()
    data class Success(val device: DeviceEntity) : DeviceRegistrationState()
    data class Error(val error: String?) : DeviceRegistrationState()
}
