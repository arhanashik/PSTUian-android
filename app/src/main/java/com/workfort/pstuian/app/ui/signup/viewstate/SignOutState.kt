package com.workfort.pstuian.app.ui.signup.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 8:15 PM.
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

sealed class SignOutState {
    object Idle : SignOutState()
    object Loading : SignOutState()
    data class Success(val message: String) : SignOutState()
    data class Error(val error: String?) : SignOutState()
}
