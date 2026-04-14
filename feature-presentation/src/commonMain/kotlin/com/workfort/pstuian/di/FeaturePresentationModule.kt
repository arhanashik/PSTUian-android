package com.workfort.pstuian.di

import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType
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
import com.workfort.pstuian.ui.emailverification.EmailVerificationUiStateMachine
import com.workfort.pstuian.ui.emailverification.EmailVerificationViewModel
import com.workfort.pstuian.ui.employeeprofile.EmployeeProfileUiStateMachine
import com.workfort.pstuian.ui.employeeprofile.EmployeeProfileViewModel
import com.workfort.pstuian.ui.faculty.FacultyUiStateMachine
import com.workfort.pstuian.ui.faculty.FacultyViewModel
import com.workfort.pstuian.ui.forgotpassword.ForgotPasswordUiStateMachine
import com.workfort.pstuian.ui.forgotpassword.ForgotPasswordViewModel
import com.workfort.pstuian.ui.home.HomeUiStateMachine
import com.workfort.pstuian.ui.home.HomeViewModel
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListUiStateMachine
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListUiStateMachine
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListUiStateMachine
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListViewModel
import com.workfort.pstuian.ui.settings.SettingsUiStateMachine
import com.workfort.pstuian.ui.settings.SettingsViewModel
import com.workfort.pstuian.ui.signin.SignInUiStateMachine
import com.workfort.pstuian.ui.signin.SignInViewModel
import com.workfort.pstuian.ui.signup.SignUpUiStateMachine
import com.workfort.pstuian.ui.signup.SignUpViewModel
import com.workfort.pstuian.ui.splash.SplashUiStateMachine
import com.workfort.pstuian.ui.splash.SplashViewModel
import com.workfort.pstuian.ui.studentprofile.StudentProfileUiStateMachine
import com.workfort.pstuian.ui.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.ui.studentprofileedit.StudentProfileEditUiStateMachine
import com.workfort.pstuian.ui.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.ui.students.StudentsUiStateMachine
import com.workfort.pstuian.ui.students.StudentsViewModel
import com.workfort.pstuian.ui.teacherprofile.TeacherProfileUiStateMachine
import com.workfort.pstuian.ui.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.ui.teacherprofileedit.TeacherProfileEditUiStateMachine
import com.workfort.pstuian.ui.teacherprofileedit.TeacherProfileEditViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private val navigationModule = module {
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
        CvUploadViewModel(userId, userType, get())
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

private val forgotPasswordModule = module {
    factoryOf(::ForgotPasswordUiStateMachine)
    factoryOf(::ForgotPasswordViewModel)
}

private val homeModule = module {
    factoryOf(::HomeUiStateMachine)
    factoryOf(::HomeViewModel)
}

private val myCheckInListModule = module {
    factoryOf(::MyCheckInListUiStateMachine)
    factory { (userId: Int, userType: UserType) ->
        MyCheckInListViewModel(
            userId = userId,
            userType = userType,
            checkInRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val signInModule = module {
    factoryOf(::SignInUiStateMachine)
    factoryOf(::SignInViewModel)
}

private val signUpModule = module {
    factoryOf(::SignUpUiStateMachine)
    factoryOf(::SignUpViewModel)
}

private val splashModule = module {
    factoryOf(::SplashUiStateMachine)
    factoryOf(::SplashViewModel)
}

private val studentsModule = module {
    factoryOf(::StudentsUiStateMachine)
    factory { (batchId: Int) ->
        StudentsViewModel(batchId, get(), get())
    }
}

private val studentProfileModule = module {
    factoryOf(::StudentProfileUiStateMachine)
    factory { (userId: Int) ->
        StudentProfileViewModel(userId, get(), get(), get())
    }
}

private val teacherProfileModule = module {
    factoryOf(::TeacherProfileUiStateMachine)
    factory { (userId: Int) ->
        TeacherProfileViewModel(userId, get(), get(), get())
    }
}

private val employeeProfileModule = module {
    factoryOf(::EmployeeProfileUiStateMachine)
    factory { (userId: Int) ->
        EmployeeProfileViewModel(userId, get(), get())
    }
}

private val deleteAccountModule = module {
    factoryOf(::DeleteAccountUiStateMachine)
    factoryOf(::DeleteAccountViewModel)
}

private val emailVerificationModule = module {
    factoryOf(::EmailVerificationUiStateMachine)
    factoryOf(::EmailVerificationViewModel)
}

private val myBloodDonationListModule = module {
    factory { (userId: Int, userType: UserType) ->
        MyBloodDonationListUiStateMachine(userId, userType, get())
    }
    factoryOf(::MyBloodDonationListViewModel)
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
    factory { (userId: Int, mode: ProfileEditMode) ->
        StudentProfileEditUiStateMachine(userId, mode, get(), get())
    }
    factory { (userId: Int, mode: ProfileEditMode) ->
        StudentProfileEditViewModel(get { parametersOf(userId, mode) })
    }
}

private val teacherProfileEditModule = module {
    factory { (userId: Int, mode: ProfileEditMode) ->
        TeacherProfileEditUiStateMachine(userId, mode, get(), get())
    }
    factory { (userId: Int, mode: ProfileEditMode) ->
        TeacherProfileEditViewModel(get { parametersOf(userId, mode) })
    }
}

val featurePresentationModule = listOf(
    navigationModule,
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
    forgotPasswordModule,
    homeModule,
    myCheckInListModule,
    signInModule,
    signUpModule,
    splashModule,
    studentsModule,
    studentProfileModule,
    teacherProfileModule,
    employeeProfileModule,
    deleteAccountModule,
    emailVerificationModule,
    myBloodDonationListModule,
    myDeviceListModule,
    settingsModule,
    studentProfileEditModule,
    teacherProfileEditModule,
)
