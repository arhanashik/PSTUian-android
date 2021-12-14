package com.workfort.pstuian.app.ui.common.locationpicker.viewstate

import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:32.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class CheckInLocationListState {
    object Idle: CheckInLocationListState()
    object Loading: CheckInLocationListState()
    data class Success(val data: List<CheckInLocationEntity>): CheckInLocationListState()
    data class Error(val message: String): CheckInLocationListState()
}

sealed class CheckInLocationState {
    object Idle: CheckInLocationState()
    object Loading: CheckInLocationState()
    data class Success(val data: CheckInLocationEntity): CheckInLocationState()
    data class Error(val message: String): CheckInLocationState()
}
