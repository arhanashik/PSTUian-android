package com.workfort.pstuian.app.ui.studentprofile.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 12:00 PM.
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

sealed class GetProfileState {
    object Idle : GetProfileState()
    object Loading : GetProfileState()
    data class Success<T>(val data: T? = null) : GetProfileState()
    data class Error(val error: String?) : GetProfileState()
}

sealed class ChangeProfileInfoState {
    object Idle : ChangeProfileInfoState()
    object Loading : ChangeProfileInfoState()
    data class Success<T>(val data: T? = null) : ChangeProfileInfoState()
    data class Error(val error: String?) : ChangeProfileInfoState()
}
