package com.workfort.pstuian.app.ui.signup.viewstate

import com.workfort.pstuian.app.data.local.student.StudentEntity

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
sealed class StudentSignUpState {
    object Idle : StudentSignUpState()
    object Loading : StudentSignUpState()
    data class Success(val student: StudentEntity) : StudentSignUpState()
    data class Error(val error: String?) : StudentSignUpState()
}
