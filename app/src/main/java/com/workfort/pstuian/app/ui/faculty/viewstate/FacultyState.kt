package com.workfort.pstuian.app.ui.faculty.viewstate

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

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

sealed class BatchState {
    object Idle : BatchState()
    object Loading : BatchState()
    data class Batches(val batches: List<BatchEntity>) : BatchState()
    data class Error(val error: String?) : BatchState()
}

sealed class TeacherState {
    object Idle : TeacherState()
    object Loading : TeacherState()
    data class Teachers(val teachers: List<TeacherEntity>) : TeacherState()
    data class Error(val error: String?) : TeacherState()
}

sealed class CourseState {
    object Idle : CourseState()
    object Loading : CourseState()
    data class Courses(val cours: List<CourseEntity>) : CourseState()
    data class Error(val error: String?) : CourseState()
}

sealed class EmployeeState {
    object Idle : EmployeeState()
    object Loading : EmployeeState()
    data class Employees(val employees: List<EmployeeEntity>) : EmployeeState()
    data class Error(val error: String?) : EmployeeState()
}