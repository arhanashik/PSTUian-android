package com.workfort.pstuian.ui.common.navigation

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen {
    @Serializable
    object Splash : AppScreen
    @Serializable
    object SignIn : AppScreen
    /** Change password, send reset email, or complete OOB reset; [resetPasswordParams] from deep link. */
    @Serializable
    data class ChangePassword(
        val resetPasswordParams: ResetPasswordParams? = null,
    ) : AppScreen
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
    data class MyBloodDonationList(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class MyCheckInList(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class StudentProfileEdit(val userId: Int) : AppScreen
    @Serializable
    data class TeacherProfileEdit(val userId: Int) : AppScreen
    @Serializable
    data class EmployeeProfileEdit(val userId: Int) : AppScreen
    @Serializable
    data object DeleteAccount : AppScreen
    @Serializable
    object LocationPicker : AppScreen
    @Serializable
    object Donate : AppScreen
    @Serializable
    data class ImageUpload(val userId: Int, val userType: UserType) : AppScreen
    @Serializable
    data class ImagePreview(val encodedImageUrl: String) : AppScreen
    @Serializable
    object Settings : AppScreen

    @Serializable
    data class Faculty(val facultyId: Int) : AppScreen
    @Serializable
    data object CheckInList : AppScreen
    @Serializable
    object Donors : AppScreen
    @Serializable
    object Notification : AppScreen
}
