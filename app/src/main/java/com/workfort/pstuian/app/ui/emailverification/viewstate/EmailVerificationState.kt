package com.workfort.pstuian.app.ui.emailverification.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 10:58.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/16.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class EmailVerificationState {
    object Idle : EmailVerificationState()
    object Loading : EmailVerificationState()
    data class Success(val message: String) : EmailVerificationState()
    data class Error(val message: String) : EmailVerificationState()
}
