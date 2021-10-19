package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.remote.apihelper.StudentApiHelperImpl

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 4:03 PM.
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

class StudentRepository(
    private val helper: StudentApiHelperImpl
) {
    suspend fun changeProfileImage(id: Int, imageUrl: String): String {
        return helper.changeProfileImage(id, imageUrl)
    }
}