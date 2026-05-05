package com.workfort.pstuian.ui.common.navigation

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigator {
    private val _events = MutableSharedFlow<NavEvent>()
    val events = _events.asSharedFlow()

    suspend fun navigateTo(screen: AppScreen) {
        _events.emit(NavEvent.Navigate(screen))
    }

    suspend fun resetTo(screen: AppScreen) {
        _events.emit(NavEvent.ResetTo(screen))
    }

    suspend fun replaceWith(screen: AppScreen) {
        _events.emit(NavEvent.ReplaceWith(screen))
    }

    suspend fun replaceAll(screen: AppScreen) {
        _events.emit(NavEvent.ReplaceAll(screen))
    }

    suspend fun goBack() {
        _events.emit(NavEvent.Back)
    }

    suspend fun popToRoot() {
        _events.emit(NavEvent.PopToRoot)
    }

    // Convenience methods for every screen
    suspend fun navigateToSignIn() = navigateTo(AppScreen.SignIn)
    suspend fun navigateToChangePassword(resetPasswordParams: ResetPasswordParams? = null) = navigateTo(AppScreen.ChangePassword(resetPasswordParams))
    suspend fun navigateToHome() = navigateTo(AppScreen.Home)
    suspend fun navigateToStudents(batchId: Int) = navigateTo(AppScreen.Students(batchId))
    suspend fun navigateToBloodDonationRequestList() = navigateTo(AppScreen.BloodDonationRequestList)
    suspend fun navigateToBloodDonationRequestCreate() = navigateTo(AppScreen.BloodDonationRequestCreate)
    suspend fun navigateToBloodDonationInput(donationId: Int?, requestId: Int?, userId: Int, userType: UserType) =
        navigateTo(AppScreen.BloodDonationInput(donationId, requestId, userId, userType))
    suspend fun navigateToProfile(userId: Int, userType: UserType) = navigateTo(AppScreen.Profile(userId, userType))
    suspend fun navigateToBloodDonationHistory(userId: Int, userType: UserType) = navigateTo(AppScreen.BloodDonationHistory(userId, userType))
    suspend fun navigateToCheckInHistory(userId: Int, userType: UserType) = navigateTo(AppScreen.CheckInHistory(userId, userType))
    suspend fun navigateToStudentProfileEdit(userId: Int) = navigateTo(AppScreen.StudentProfileEdit(userId))
    suspend fun navigateToTeacherProfileEdit(userId: Int) = navigateTo(AppScreen.TeacherProfileEdit(userId))
    suspend fun navigateToEmployeeProfileEdit(userId: Int) = navigateTo(AppScreen.EmployeeProfileEdit(userId))
    suspend fun navigateToDeleteAccount() = navigateTo(AppScreen.DeleteAccount)
    suspend fun navigateToLocationPicker() = navigateTo(AppScreen.LocationPicker)
    suspend fun navigateToDonate() = navigateTo(AppScreen.Donate)
    suspend fun navigateToImageUpload(userId: Int, userType: UserType) = navigateTo(AppScreen.ImageUpload(userId, userType))
    suspend fun navigateToImagePreview(encodedImageUrl: String) = navigateTo(AppScreen.ImagePreview(encodedImageUrl))
    suspend fun navigateToSettings() = navigateTo(AppScreen.Settings)
    suspend fun navigateToFaculty(facultyId: Int) = navigateTo(AppScreen.Faculty(facultyId))
    suspend fun navigateToCheckIn() = navigateTo(AppScreen.CheckIn)
    suspend fun navigateToDonors() = navigateTo(AppScreen.Donors)
    suspend fun navigateToNotification() = navigateTo(AppScreen.Notification)
}
