package com.workfort.pstuian.ui.common.navigation

import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType
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

    suspend fun resetAll(screen: AppScreen) {
        _events.emit(NavEvent.ResetAll(screen))
    }

    suspend fun goBack() {
        _events.emit(NavEvent.Back)
    }

    suspend fun popToRoot() {
        _events.emit(NavEvent.PopToRoot)
    }

    // Convenience methods for every screen
    suspend fun navigateToSignIn() = navigateTo(AppScreen.SignIn)
    suspend fun navigateToChangePassword() = navigateTo(AppScreen.ChangePassword)
    suspend fun navigateToContactUs() = navigateTo(AppScreen.ContactUs)
    suspend fun navigateToHome() = navigateTo(AppScreen.Home)
    suspend fun navigateToStudents(batchId: Int) = navigateTo(AppScreen.Students(batchId))
    suspend fun navigateToTeachers(userId: String) = navigateTo(AppScreen.Teachers(userId))
    suspend fun navigateToEmployees(userId: String) = navigateTo(AppScreen.Employees(userId))
    suspend fun navigateToBloodDonationRequestList() = navigateTo(AppScreen.BloodDonationRequestList)
    suspend fun navigateToBloodDonationRequestCreate() = navigateTo(AppScreen.BloodDonationRequestCreate)
    suspend fun navigateToBloodDonationRequestEdit(donationId: Int) = navigateTo(AppScreen.BloodDonationRequestEdit(donationId))
    suspend fun navigateToProfile(userId: String, userType: UserType) = navigateTo(AppScreen.Profile(userId, userType))
    suspend fun navigateToMyBloodDonationList(userId: String, userType: UserType) = navigateTo(AppScreen.MyBloodDonationList(userId, userType))
    suspend fun navigateToMyCheckInList(userId: String, userType: UserType) = navigateTo(AppScreen.MyCheckInList(userId, userType))
    suspend fun navigateToMyDeviceList(userId: String, userType: UserType) = navigateTo(AppScreen.MyDeviceList(userId, userType))
    suspend fun navigateToStudentProfileEdit(userId: String, action: ProfileEditMode) = navigateTo(AppScreen.StudentProfileEdit(userId, action))
    suspend fun navigateToTeacherProfileEdit(userId: String, action: ProfileEditMode) = navigateTo(AppScreen.TeacherProfileEdit(userId, action))
    suspend fun navigateToEmployeeProfileEdit(userId: String, action: ProfileEditMode) = navigateTo(AppScreen.EmployeeProfileEdit(userId, action))
    suspend fun navigateToDeleteAccount(userId: String, userType: UserType) = navigateTo(AppScreen.DeleteAccount(userId, userType))
    suspend fun navigateToLocationPicker() = navigateTo(AppScreen.LocationPicker)
    suspend fun navigateToDonate() = navigateTo(AppScreen.Donate)
    suspend fun navigateToFacultyPicker(mode: FacultySelectionMode, facultyId: Int? = null, batchId: Int? = null) = navigateTo(AppScreen.FacultyPicker(mode, facultyId, batchId))
    suspend fun navigateToImageUpload(userId: String, userType: UserType) = navigateTo(AppScreen.ImageUpload(userId, userType))
    suspend fun navigateToImagePreview(encodedImageUrl: String) = navigateTo(AppScreen.ImagePreview(encodedImageUrl))
    suspend fun navigateToDownloadCv(userId: String, userType: UserType, url: String) = navigateTo(AppScreen.DownloadCv(userId, userType, url))
    suspend fun navigateToUploadCv(userId: String, userType: UserType) = navigateTo(AppScreen.UploadCv(userId, userType))
    suspend fun navigateToSettings() = navigateTo(AppScreen.Settings)
    suspend fun navigateToFaculty(facultyId: Int) = navigateTo(AppScreen.Faculty(facultyId))
    suspend fun navigateToDonors() = navigateTo(AppScreen.Donors)
    suspend fun navigateToNotification() = navigateTo(AppScreen.Notification)
}
