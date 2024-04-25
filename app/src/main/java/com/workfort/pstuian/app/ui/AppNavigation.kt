package com.workfort.pstuian.app.ui


enum class Screen {
    SPLASH,
    HOME,
    SIGN_IN,
    SIGN_UP,
    NOTIFICATION,
    FACULTY,
    STUDENT_LIST,
    STUDENT_PROFILE,
    STUDENT_PROFILE_EDIT,
    TEACHER_PROFILE,
    TEACHER_PROFILE_EDIT,
    EMPLOYEE_PROFILE,
    EMAIL_VERIFICATION,
    FORGOT_PASSWORD,
    CHECK_IN_LIST,
    MY_CHECK_IN_LIST,
    IMAGE_PREVIEW,
    CONTACT_US,
    DONORS,
    DONATE,
    SETTINGS,
    BLOOD_DONATION_CREATE,
    MY_BLOOD_DONATION_LIST,
    BLOOD_DONATION_REQUEST_CREATE,
    BLOOD_DONATION_REQUEST_LIST,
    MY_DEVICE_LIST,
    FACULTY_PICKER,
    LOCATION_PICKER,
    CHANGE_PASSWORD,
    DELETE_ACCOUNT,
    CV_UPLOAD,
    CV_DOWNLOAD,
    IMAGE_UPLOAD,
}

sealed class NavItem(val route: String) {
    data object Splash : NavItem(Screen.SPLASH.name)
    data object Home : NavItem(Screen.HOME.name)
    data object SignIn : NavItem(Screen.SIGN_IN.name)
    data object SignUp : NavItem(Screen.SIGN_UP.name)
    data object Notification : NavItem(Screen.NOTIFICATION.name)
    data object Faculty : NavItem(Screen.FACULTY.name)
    data object StudentList : NavItem(Screen.STUDENT_LIST.name)
    data object StudentProfile : NavItem(Screen.STUDENT_PROFILE.name)
    data object StudentProfileEdit : NavItem(Screen.STUDENT_PROFILE_EDIT.name)
    data object TeacherProfile : NavItem(Screen.TEACHER_PROFILE.name)
    data object TeacherProfileEdit : NavItem(Screen.TEACHER_PROFILE_EDIT.name)
    data object EmployeeProfile : NavItem(Screen.EMPLOYEE_PROFILE.name)
    data object EmailVerification : NavItem(Screen.EMAIL_VERIFICATION.name)
    data object ForgotPassword : NavItem(Screen.FORGOT_PASSWORD.name)
    data object CheckInList : NavItem(Screen.CHECK_IN_LIST.name)
    data object MyCheckInList : NavItem(Screen.MY_CHECK_IN_LIST.name)
    data object ImagePreview : NavItem(Screen.IMAGE_PREVIEW.name)
    data object ContactUs : NavItem(Screen.CONTACT_US.name)
    data object Donors : NavItem(Screen.DONORS.name)
    data object Donate : NavItem(Screen.DONATE.name)
    data object Settings : NavItem(Screen.SETTINGS.name)
    data object BloodDonationCreate : NavItem(Screen.BLOOD_DONATION_CREATE.name)
    data object MyBloodDonationList : NavItem(Screen.MY_BLOOD_DONATION_LIST.name)
    data object BloodDonationRequestCreate : NavItem(Screen.BLOOD_DONATION_REQUEST_CREATE.name)
    data object BloodDonationRequestList : NavItem(Screen.BLOOD_DONATION_REQUEST_LIST.name)
    data object MyDeviceList : NavItem(Screen.MY_DEVICE_LIST.name)
    data object FacultyPicker : NavItem(Screen.FACULTY_PICKER.name)
    data object LocationPicker : NavItem(Screen.LOCATION_PICKER.name)
    data object ChangePassword : NavItem(Screen.CHANGE_PASSWORD.name)
    data object DeleteAccount : NavItem(Screen.DELETE_ACCOUNT.name)
    data object CvUpload : NavItem(Screen.CV_UPLOAD.name)
    data object CvDownload : NavItem(Screen.CV_DOWNLOAD.name)
    data object ImageUpload : NavItem(Screen.IMAGE_UPLOAD.name)
}

object NavParam {
    const val USER_ID = "userId"
    const val USER_TYPE = "userType"
    const val FACULTY_ID = "facultyId"
    const val BATCH_ID = "batchId"
    const val IS_CHECK_IN_MODE = "isCheckInMode"
    const val URL = "url"
    const val MODE = "mode"
}