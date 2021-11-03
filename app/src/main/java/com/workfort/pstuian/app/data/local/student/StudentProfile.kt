package com.workfort.pstuian.app.data.local.student

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 03 Nov, 2021 at 18:00.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/03.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

data class StudentProfile (
    var student: StudentEntity,
    var faculty: FacultyEntity,
    var batch: BatchEntity,
    var isSignedIn: Boolean = false,
)