package com.workfort.pstuian.app.ui.faculty.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 04:55 AM.
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

sealed class FacultyIntent {
    object GetBatches : FacultyIntent()
    object GetTeachers : FacultyIntent()
    object GetCourses : FacultyIntent()
    object GetEmployees : FacultyIntent()
}