package com.workfort.pstuian.common.navigation

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface AppScreen {
    object Splash : AppScreen
    object SignIn : AppScreen
    object SignUp : AppScreen
    object ChangePassword : AppScreen
    object ForgotPassword : AppScreen
    object EmailVerification : AppScreen
    object ContactUs : AppScreen
    object Home : AppScreen
    data class Students(val batchId: Int) : AppScreen
    data class Teachers(val userId: Int) : AppScreen
    data class Employees(val userId: Int) : AppScreen
    object BloodDonationRequestList : AppScreen
    object BloodDonationRequestCreate : AppScreen
    data class Profile(val userId: Int, val userType: UserType) : AppScreen
    data class MyBloodDonationList(val userId: Int, val userType: UserType) : AppScreen
    data class MyCheckInList(val userId: Int, val userType: UserType) : AppScreen
    data class MyDeviceList(val userId: Int, val userType: UserType) : AppScreen
    data class StudentProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    data class TeacherProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    data class EmployeeProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    data class DeleteAccount(val userId: Int, val userType: UserType) : AppScreen
    object LocationPicker : AppScreen
    object Donate : AppScreen
    data class FacultyPicker(
        val mode: FacultySelectionMode,
        val facultyId: Int?,
        val batchId: Int?,
    ) : AppScreen
    data class ImageUpload(val userId: Int, val userType: UserType) : AppScreen
    data class ImagePreview(val encodedImageUrl: String) : AppScreen
    data class DownloadCv(
        val userId: Int,
        val userType: UserType,
        val url: String,
    ) : AppScreen
    data class UploadCv(val userId: Int, val userType: UserType) : AppScreen
}