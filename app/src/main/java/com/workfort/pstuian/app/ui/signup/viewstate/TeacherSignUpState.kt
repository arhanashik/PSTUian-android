package com.workfort.pstuian.app.ui.signup.viewstate

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 31 Oct, 2021 at 14:40.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/31.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class TeacherSignUpState {
    object Idle : TeacherSignUpState()
    object Loading : TeacherSignUpState()
    data class Success(val teacher: TeacherEntity) : TeacherSignUpState()
    data class Error(val error: String?) : TeacherSignUpState()
}
