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
    object ChangePassword : AppScreen
    @Serializable
    object ContactUs : AppScreen
    @Serializable
    object Home : AppScreen
    @Serializable
    data class Students(val batchId: Int) : AppScreen
    @Serializable
    data class Teachers(val userId: String) : AppScreen
    @Serializable
    data class Employees(val userId: String) : AppScreen
    @Serializable
    object BloodDonationRequestList : AppScreen
    @Serializable
    object BloodDonationRequestCreate : AppScreen
    @Serializable
    data class BloodDonationRequestEdit(val donationId: Int) : AppScreen
    @Serializable
    data class Profile(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class MyBloodDonationList(val userId: String, val userType: UserType) : AppScreen
    @Serializable
    data class MyCheckInList(val userId: String, val userType: UserType) : AppScreen
    @Serializable
    data class MyDeviceList(val userId: String, val userType: UserType) : AppScreen
    @Serializable
    data class StudentProfileEdit(val userId: Int) : AppScreen
    @Serializable
    data class TeacherProfileEdit(
        val userId: String,
        val action: ProfileEditMode,
    ) : AppScreen
    @Serializable
    data class EmployeeProfileEdit(
        val userId: String,
        val action: ProfileEditMode,
    ) : AppScreen
    @Serializable
    data object DeleteAccount : AppScreen
    @Serializable
    object LocationPicker : AppScreen
    @Serializable
    object Donate : AppScreen
    @Serializable
    data class ImageUpload(val userId: String, val userType: UserType) : AppScreen
    @Serializable
    data class ImagePreview(val encodedImageUrl: String) : AppScreen
    @Serializable
    data class DownloadCv(
        val userId: String,
        val userType: UserType,
        val url: String,
    ) : AppScreen
    @Serializable
    data class UploadCv(val userId: String, val userType: UserType) : AppScreen
    @Serializable
    object Settings : AppScreen
    @Serializable
    data class Faculty(val facultyId: Int) : AppScreen
    @Serializable
    object Donors : AppScreen
    @Serializable
    object Notification : AppScreen
}
