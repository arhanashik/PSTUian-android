package com.workfort.pstuian.app.ui.common.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 9:44 AM.
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
sealed class AuthIntent {
    object GetConfig : AuthIntent()
    object GetSignInUser : AuthIntent()
    object SignOut : AuthIntent()
}
