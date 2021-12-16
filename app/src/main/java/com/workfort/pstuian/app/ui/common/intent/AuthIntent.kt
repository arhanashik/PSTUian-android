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
    object RegisterDevice : AuthIntent()
    object GetConfig : AuthIntent()
    object GetSignInUser : AuthIntent()
    data class SignUpStudent(
        val name: String,
        val id: String,
        val reg: String,
        val facultyId: Int,
        val batchId: Int,
        val session:String,
        val email: String
    ) : AuthIntent()
    data class ChangePassword(val oldPassword: String, val newPassword: String) : AuthIntent()
    data class EmailVerification(val userType: String, val email: String) : AuthIntent()
    object SignOut : AuthIntent()
}
