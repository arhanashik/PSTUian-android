package com.workfort.pstuian.app.ui.checkin.viewstate

import com.workfort.pstuian.app.data.local.checkin.CheckInEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:42.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class CheckInListState {
    object Idle: CheckInListState()
    object Loading: CheckInListState()
    data class CheckInList(val list: List<CheckInEntity>) : CheckInListState()
    data class Error(val error: String?) : CheckInListState()
}

sealed class CheckInState {
    object Idle: CheckInState()
    object Loading: CheckInState()
    data class Success(val data: CheckInEntity) : CheckInState()
    data class Error(val error: String?) : CheckInState()
}

sealed class CheckInVisibilityState {
    object Idle: CheckInVisibilityState()
    object Loading: CheckInVisibilityState()
    data class Success(val data: CheckInEntity) : CheckInVisibilityState()
    data class Error(val error: String?) : CheckInVisibilityState()
}