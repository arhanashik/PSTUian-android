package com.workfort.pstuian.app.ui.students.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 04:55 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

sealed class StudentsIntent {
    data object GetStudents : StudentsIntent()
}