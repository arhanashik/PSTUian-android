package com.workfort.pstuian.app.ui.signin.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 12:11 PM.
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

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    // user should be student or teacher
    data class Success(val user: Any) : SignInState()
    data class Error(val error: String?) : SignInState()
}
