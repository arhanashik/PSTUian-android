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
 *  ****************************************************************************
 */
sealed class AuthIntent {
    data class GetAllDevices(val page: Int) : AuthIntent()
    data object RegisterDevice : AuthIntent()
    data object GetConfig : AuthIntent()
    data object GetSignInUser : AuthIntent()
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
    data class SignOut(val fromAllDevices: Boolean = false) : AuthIntent()
    data class DeleteAccount(val password: String) : AuthIntent()
}
