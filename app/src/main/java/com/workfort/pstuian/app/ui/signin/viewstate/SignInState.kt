package com.workfort.pstuian.app.ui.signin.viewstate

import com.workfort.pstuian.app.data.local.student.StudentEntity

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
    data class Success(val student: StudentEntity) : SignInState()
    data class Error(val error: String?) : SignInState()
}
