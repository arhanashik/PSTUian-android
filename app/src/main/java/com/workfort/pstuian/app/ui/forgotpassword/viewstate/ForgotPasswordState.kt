package com.workfort.pstuian.app.ui.forgotpassword.viewstate

import com.workfort.pstuian.app.ui.signup.viewstate.SignOutState

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Oct, 2021 at 16:54.
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
sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    data class Success(val message: String) : ForgotPasswordState()
    data class Error(val error: String?) : ForgotPasswordState()
}
