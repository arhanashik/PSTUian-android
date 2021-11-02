package com.workfort.pstuian.app.ui.students.viewstate

import com.workfort.pstuian.app.data.local.student.StudentEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 04:59 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/02/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class StudentsState {
    object Idle : StudentsState()
    object Loading : StudentsState()
    data class Students(val students: List<StudentEntity>) : StudentsState()
    data class Error(val error: String?) : StudentsState()
}