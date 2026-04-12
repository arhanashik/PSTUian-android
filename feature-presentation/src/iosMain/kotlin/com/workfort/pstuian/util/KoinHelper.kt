package com.workfort.pstuian.util

import com.workfort.pstuian.di.initKoin
import com.workfort.pstuian.app.ui.commonmodel.faculty.FacultyViewModel
import com.workfort.pstuian.app.ui.commonmodel.home.HomeViewModel
import com.workfort.pstuian.app.ui.commonmodel.notification.NotificationViewModel
import com.workfort.pstuian.app.ui.commonmodel.settings.SettingsViewModel
import com.workfort.pstuian.app.ui.commonmodel.signin.SignInViewModel
import com.workfort.pstuian.app.ui.commonmodel.signup.SignUpViewModel
import com.workfort.pstuian.app.ui.commonmodel.splash.SplashViewModel
import com.workfort.pstuian.app.ui.commonmodel.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.app.ui.commonmodel.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.app.ui.commonmodel.students.StudentsViewModel
import com.workfort.pstuian.app.ui.commonmodel.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.app.ui.commonmodel.teacherprofileedit.TeacherProfileEditViewModel
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.app.ui.commonmodel.blooddonationcreate.BloodDonationCreateViewModel
import com.workfort.pstuian.app.ui.commonmodel.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.app.ui.commonmodel.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.app.ui.commonmodel.checkinlist.CheckInListViewModel
import com.workfort.pstuian.app.ui.commonmodel.donate.DonateViewModel
import com.workfort.pstuian.app.ui.commonmodel.donors.DonorsViewModel
import com.workfort.pstuian.app.ui.commonmodel.emailverification.EmailVerificationViewModel
import com.workfort.pstuian.app.ui.commonmodel.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.app.ui.commonmodel.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.app.ui.commonmodel.FacultyPickerViewModel
import com.workfort.pstuian.app.ui.commonmodel.LocationPickerViewModel
import com.workfort.pstuian.util.flow.asCommonFlow
import com.workfort.pstuian.util.flow.ViewModelWrapper
import com.workfort.pstuian.model.FacultySelectionMode
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

class KoinHelper : KoinComponent {
    fun getSplashViewModel() = ViewModelWrapper(
        instance = get<SplashViewModel>(),
        state = get<SplashViewModel>().splashScreenState.asCommonFlow()
    )
    fun getHomeViewModel() = get<HomeViewModel>()
    fun getSignInViewModel() = get<SignInViewModel>()
    fun getSignUpViewModel() = get<SignUpViewModel>()
    fun getFacultyViewModel() = get<FacultyViewModel>()
    fun getStudentsViewModel(facultyId: Int, batchId: Int) = get<StudentsViewModel> {
        parametersOf(facultyId, batchId)
    }
    fun getSettingsViewModel() = get<SettingsViewModel>()
    fun getNotificationViewModel() = get<NotificationViewModel>()
    fun getStudentProfileViewModel(userId: Int) = get<StudentProfileViewModel> {
        parametersOf(userId)
    }
    fun getStudentProfileEditViewModel(userId: Int) = get<StudentProfileEditViewModel> {
        parametersOf(userId)
    }
    fun getTeacherProfileViewModel(userId: Int) = get<TeacherProfileViewModel> {
        parametersOf(userId)
    }
    fun getTeacherProfileEditViewModel(userId: Int) = get<TeacherProfileEditViewModel> {
        parametersOf(userId)
    }
    fun getCheckInListViewModel() = get<CheckInListViewModel>()
    fun getMyCheckInListViewModel(userId: Int, userType: UserType) = get<MyCheckInListViewModel> {
        parametersOf(userId, userType)
    }
    fun getEmailVerificationViewModel() = get<EmailVerificationViewModel>()
    fun getDonateViewModel() = get<DonateViewModel>()
    fun getDonorsViewModel() = get<DonorsViewModel>()
    fun getMyBloodDonationListViewModel(userId: Int, userType: UserType) = get<MyBloodDonationListViewModel> {
        parametersOf(userId, userType)
    }
    fun getBloodDonationRequestListViewModel() = get<BloodDonationRequestListViewModel>()
    fun getBloodDonationRequestCreateViewModel() = get<BloodDonationRequestCreateViewModel>()
    fun getBloodDonationCreateViewModel() = get<BloodDonationCreateViewModel>()
    fun getFacultyPickerViewModel(selectionMode: FacultySelectionMode, selectedFacultyId: Int, selectedBatchId: Int) =
        get<FacultyPickerViewModel> {
            parametersOf(selectionMode, selectedFacultyId, selectedBatchId)
        }
    fun getLocationPickerViewModel(isCheckInMode: Boolean) = get<LocationPickerViewModel> {
        parametersOf(isCheckInMode)
    }
}

