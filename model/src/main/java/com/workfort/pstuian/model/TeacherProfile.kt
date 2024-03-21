package com.workfort.pstuian.model

/**
 *  ****************************************************************************
 *  * Created by : arhan on 03 Nov, 2021 at 18:57.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

data class TeacherProfile(
    var teacher: TeacherEntity,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean = false
)
