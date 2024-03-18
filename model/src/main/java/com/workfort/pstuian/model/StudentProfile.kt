package com.workfort.pstuian.model

/**
 *  ****************************************************************************
 *  * Created by : arhan on 03 Nov, 2021 at 18:00.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

data class StudentProfile (
    var student: StudentEntity,
    var faculty: FacultyEntity,
    var batch: BatchEntity,
    var isSignedIn: Boolean = false,
)