package com.workfort.pstuian.di

import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.AppViewModel
import com.workfort.pstuian.ui.blooddonationcreate.BloodDonationCreateUiStateMachine
import com.workfort.pstuian.ui.blooddonationcreate.BloodDonationCreateViewModel
import com.workfort.pstuian.ui.blooddonationrequestcreate.BloodDonationRequestCreateUiStateMachine
import com.workfort.pstuian.ui.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.ui.blooddonationrequestlist.BloodDonationRequestListUiStateMachine
import com.workfort.pstuian.ui.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.ui.changepassword.ChangePasswordUiStateMachine
import com.workfort.pstuian.ui.changepassword.ChangePasswordViewModel
import com.workfort.pstuian.ui.checkinlist.CheckInListUiStateMachine
import com.workfort.pstuian.ui.checkinlist.CheckInListViewModel
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.contactus.ContactUsUiStateMachine
import com.workfort.pstuian.ui.contactus.ContactUsViewModel
import com.workfort.pstuian.ui.cvdownload.CvDownloadUiStateMachine
import com.workfort.pstuian.ui.cvdownload.CvDownloadViewModel
import com.workfort.pstuian.ui.cvupload.CvUploadUiStateMachine
import com.workfort.pstuian.ui.cvupload.CvUploadViewModel
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountUiStateMachine
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountViewModel
import com.workfort.pstuian.ui.donate.DonateUiStateMachine
import com.workfort.pstuian.ui.donate.DonateViewModel
import com.workfort.pstuian.ui.donors.DonorsUiStateMachine
import com.workfort.pstuian.ui.donors.DonorsViewModel
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileUiStateMachine
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileViewModel
import com.workfort.pstuian.ui.faculty.FacultyUiStateMachine
import com.workfort.pstuian.ui.faculty.FacultyViewModel
import com.workfort.pstuian.ui.home.HomeUiStateMachine
import com.workfort.pstuian.ui.home.HomeViewModel
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListUiStateMachine
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListUiStateMachine
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListUiStateMachine
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListViewModel
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileDisplayDataMapper
import com.workfort.pstuian.ui.settings.SettingsUiStateMachine
import com.workfort.pstuian.ui.settings.SettingsViewModel
import com.workfort.pstuian.ui.signin.SignInUiStateMachine
import com.workfort.pstuian.ui.signin.SignInViewModel
import com.workfort.pstuian.ui.splash.SplashUiStateMachine
import com.workfort.pstuian.ui.splash.SplashViewModel
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileUiStateMachine
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditUiStateMachine
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.ui.students.StudentsUiStateMachine
import com.workfort.pstuian.ui.students.StudentsViewModel
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileUiStateMachine
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditUiStateMachine
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val appCommonModule = module {
    singleOf(::AppViewModel)
    singleOf(::SharedScreenData)
    singleOf(::AppNavigator)
}

private val bloodDonationCreateModule = module {
    factoryOf(::BloodDonationCreateUiStateMachine)
    factoryOf(::BloodDonationCreateViewModel)
}

private val bloodDonationRequestCreateModule = module {
    factoryOf(::BloodDonationRequestCreateUiStateMachine)
    factoryOf(::BloodDonationRequestCreateViewModel)
}

private val bloodDonationRequestListModule = module {
    factoryOf(::BloodDonationRequestListUiStateMachine)
    factoryOf(::BloodDonationRequestListViewModel)
}

private val changePasswordModule = module {
    factoryOf(::ChangePasswordUiStateMachine)
    factoryOf(::ChangePasswordViewModel)
}

private val checkInListModule = module {
    factoryOf(::CheckInListUiStateMachine)
    factoryOf(::CheckInListViewModel)
}

private val contactUsModule = module {
    factoryOf(::ContactUsUiStateMachine)
    factoryOf(::ContactUsViewModel)
}

private val cvDownloadModule = module {
    factoryOf(::CvDownloadUiStateMachine)
    factory { (userId: Int, userType: UserType, urlToDownload: String) ->
        CvDownloadViewModel(
            userId = userId,
            userType = userType,
            urlToDownload = urlToDownload,
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val cvUploadModule = module {
    factoryOf(::CvUploadUiStateMachine)
    factory { (userId: Int, userType: UserType) ->
        CvUploadViewModel(
            userId = userId,
            userType = userType,
            authRepository = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val donorsModule = module {
    factoryOf(::DonorsUiStateMachine)
    factoryOf(::DonorsViewModel)
}

private val donateModule = module {
    factoryOf(::DonateUiStateMachine)
    factoryOf(::DonateViewModel)
}

private val facultyModule = module {
    factoryOf(::FacultyUiStateMachine)
    factory { (facultyId: Int) ->
        FacultyViewModel(
            currentFacultyId = facultyId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val homeModule = module {
    factoryOf(::HomeUiStateMachine)
    factoryOf(::HomeViewModel)
}

private val signInModule = module {
    factoryOf(::SignInUiStateMachine)
    factoryOf(::SignInViewModel)
}

private val splashModule = module {
    factoryOf(::SplashUiStateMachine)
    factory {
        SplashViewModel(
            appConfigRepository = get(),
            registerDeviceUseCase = get(),
            getInitialScreenUseCase = get(),
            settingsRepository = get(),
            stateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val studentsModule = module {
    factoryOf(::StudentsUiStateMachine)
    factory { (batchId: Int) ->
        StudentsViewModel(
            batchId = batchId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val studentProfileModule = module {
    factoryOf(::StudentProfileDisplayDataMapper)
    factoryOf(::StudentProfileUiStateMachine)
    factory { (userId: Int) ->
        StudentProfileViewModel(
            userId = userId,
            studentRepo = get(),
            authRepo = get(),
            settingsRepository = get(),
            getStudentProfileUserUseCase = get(),
            displayDataMapper = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val teacherProfileModule = module {
    factoryOf(::TeacherProfileUiStateMachine)
    factory { (userId: Int) ->
        TeacherProfileViewModel(
            userId = userId,
            teacherRepo = get(),
            authRepo = get(),
            settingsRepository = get(),
            getTeacherProfileUserUseCase = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val employeeProfileModule = module {
    factoryOf(::EmployeeProfileUiStateMachine)
    factory { (userId: Int) ->
        EmployeeProfileViewModel(
            userId = userId,
            facultyRepo = get(),
            authRepo = get(),
            settingsRepository = get(),
            getEmployeeProfileUserUseCase = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val deleteAccountModule = module {
    factoryOf(::DeleteAccountUiStateMachine)
    factoryOf(::DeleteAccountViewModel)
}

private val myBloodDonationListModule = module {
    factoryOf(::MyBloodDonationListUiStateMachine)
    factory { (userId: String, userType: UserType) ->
        MyBloodDonationListViewModel(
            userId = userId,
            userType = userType,
            donationRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val myCheckInListModule = module {
    factoryOf(::MyCheckInListUiStateMachine)
    factory { (userId: String, userType: UserType) ->
        MyCheckInListViewModel(
            userId = userId,
            userType = userType,
            checkInRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val myDeviceListModule = module {
    factoryOf(::MyDeviceListUiStateMachine)
    factoryOf(::MyDeviceListViewModel)
}

private val settingsModule = module {
    factoryOf(::SettingsUiStateMachine)
    factoryOf(::SettingsViewModel)
}

private val studentProfileEditModule = module {
    factoryOf(::StudentProfileEditUiStateMachine)
    factory { (userId: Int, mode: ProfileEditMode) ->
        StudentProfileEditViewModel(
            userId = userId,
            mode = mode,
            studentRepo = get(),
            facultyRepo = get(),
            getStudentProfileUserUseCase = get(),
            stateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val teacherProfileEditModule = module {
    factoryOf(::TeacherProfileEditUiStateMachine)
    factory { (userId: Int, mode: ProfileEditMode) ->
        TeacherProfileEditViewModel(
            userId = userId,
            mode = mode,
            teacherRepo = get(),
            facultyRepo = get(),
            getTeacherProfileUserUseCase = get(),
            stateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

val featurePresentationModule = listOf(
    appCommonModule,
    bloodDonationCreateModule,
    bloodDonationRequestCreateModule,
    bloodDonationRequestListModule,
    changePasswordModule,
    checkInListModule,
    contactUsModule,
    cvDownloadModule,
    cvUploadModule,
    donorsModule,
    donateModule,
    facultyModule,
    homeModule,
    signInModule,
    splashModule,
    studentsModule,
    studentProfileModule,
    teacherProfileModule,
    employeeProfileModule,
    deleteAccountModule,
    myBloodDonationListModule,
    myCheckInListModule,
    myDeviceListModule,
    settingsModule,
    studentProfileEditModule,
    teacherProfileEditModule,
)
