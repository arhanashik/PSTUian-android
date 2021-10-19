package com.workfort.pstuian.app.ui.studentprofile.intent

import com.workfort.pstuian.app.ui.common.intent.AuthIntent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 20 Oct, 2021 at 4:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/20/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class StudentProfileIntent {
    object ChangeProfileImage : StudentProfileIntent()
}