package com.workfort.pstuian.ui.common.navigation

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen {
    @Serializable
    object Splash : AppScreen
    @Serializable
    object SignIn : AppScreen
    @Serializable
    object SignUp : AppScreen
    @Serializable
    object ChangePassword : AppScreen
    @Serializable
    object ForgotPassword : AppScreen
    @Serializable
    object EmailVerification : AppScreen
    @Serializable
    object ContactUs : AppScreen
    @Serializable
    object Home : AppScreen
    @Serializable
    data class Students(val batchId: Int) : AppScreen
    @Serializable
    data class Teachers(val userId: Int) : AppScreen
    @Serializable
    data class Employees(val userId: Int) : AppScreen
    @Serializable
    object BloodDonationRequestList : AppScreen
    @Serializable
    object BloodDonationRequestCreate : AppScreen
    @Serializable
    data class BloodDonationRequestEdit(val donationId: Int) : AppScreen
    @Serializable
    data class Profile(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class MyBloodDonationList(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class MyCheckInList(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class MyDeviceList(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class StudentProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    @Serializable
    data class TeacherProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    @Serializable
    data class EmployeeProfileEdit(
        val userId: Int,
        val action: ProfileEditMode,
    ) : AppScreen
    @Serializable
    data class DeleteAccount(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    object LocationPicker : AppScreen
    @Serializable
    object Donate : AppScreen
    @Serializable
    data class FacultyPicker(
        val mode: FacultySelectionMode,
        val facultyId: Int?,
        val batchId: Int?,
    ) : AppScreen
    @Serializable
    data class ImageUpload(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class ImagePreview(val encodedImageUrl: String) : AppScreen
    @Serializable
    data class DownloadCv(
        val userId: Int,
        val userType: UserType,
        val url: String,
    ) : AppScreen
    @Serializable
    data class UploadCv(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    object Settings : AppScreen
    @Serializable
    data class Faculty(val facultyId: Int) : AppScreen
    @Serializable
    object Donors : AppScreen
    @Serializable
    object Notification : AppScreen
}
