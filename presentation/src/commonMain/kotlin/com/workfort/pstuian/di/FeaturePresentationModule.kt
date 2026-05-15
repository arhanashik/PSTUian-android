package com.workfort.pstuian.di

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.model.AppLaunchDeepLinkController
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.AppViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.BloodDonationHistoryUiStateMachine
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.BloodDonationHistoryViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.BloodDonationInputUiStateMachine
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.BloodDonationInputViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.BloodDonationRequestCreateUiStateMachine
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.BloodDonationRequestDisplayDataMapper
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.BloodDonationRequestListUiStateMachine
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.ui.changepassword.ChangePasswordUiStateMachine
import com.workfort.pstuian.ui.changepassword.ChangePasswordViewModel
import com.workfort.pstuian.ui.checkin.CheckInDisplayDataMapper
import com.workfort.pstuian.ui.checkin.CheckInUiStateMachine
import com.workfort.pstuian.ui.checkin.CheckInViewModel
import com.workfort.pstuian.ui.checkinhistory.CheckInHistoryUiStateMachine
import com.workfort.pstuian.ui.checkinhistory.CheckInHistoryViewModel
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.DeepLinkNavigator
import com.workfort.pstuian.ui.cvdownload.CvDownloadUiStateMachine
import com.workfort.pstuian.ui.cvdownload.CvDownloadViewModel
import com.workfort.pstuian.ui.cvupload.CvUploadUiStateMachine
import com.workfort.pstuian.ui.cvupload.CvUploadViewModel
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountUiStateMachine
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountViewModel
import com.workfort.pstuian.ui.donation.donate.DonateUiStateMachine
import com.workfort.pstuian.ui.donation.donate.DonateViewModel
import com.workfort.pstuian.ui.donation.donationhistory.DonationHistoryUiStateMachine
import com.workfort.pstuian.ui.donation.donationhistory.DonationHistoryViewModel
import com.workfort.pstuian.ui.faculty.faculty.FacultyUiStateMachine
import com.workfort.pstuian.ui.faculty.faculty.FacultyViewModel
import com.workfort.pstuian.ui.faculty.batch.BatchUiStateMachine
import com.workfort.pstuian.ui.faculty.batch.BatchViewModel
import com.workfort.pstuian.ui.faculty.course.CourseUiStateMachine
import com.workfort.pstuian.ui.faculty.course.CourseViewModel
import com.workfort.pstuian.ui.faculty.employee.EmployeeUiStateMachine
import com.workfort.pstuian.ui.faculty.employee.EmployeeViewModel
import com.workfort.pstuian.ui.faculty.teacher.TeacherUiStateMachine
import com.workfort.pstuian.ui.faculty.teacher.TeacherViewModel
import com.workfort.pstuian.ui.home.HomeUiStateMachine
import com.workfort.pstuian.ui.home.HomeViewModel
import com.workfort.pstuian.ui.notification.common.NotificationDisplayDataMapper
import com.workfort.pstuian.ui.notification.customnotification.CustomNotificationUiStateMachine
import com.workfort.pstuian.ui.notification.customnotification.CustomNotificationViewModel
import com.workfort.pstuian.ui.notification.notification.NotificationUiStateMachine
import com.workfort.pstuian.ui.notification.notification.NotificationViewModel
import com.workfort.pstuian.ui.notification.systemnotification.SystemNotificationUiStateMachine
import com.workfort.pstuian.ui.notification.systemnotification.SystemNotificationViewModel
import com.workfort.pstuian.ui.imageupload.ImageUploadUiStateMachine
import com.workfort.pstuian.ui.imageupload.ImageUploadViewModel
import com.workfort.pstuian.ui.profile.common.UserPresenceDisplayDataMapper
import com.workfort.pstuian.ui.profile.common.state.ProfileScreenUiStateMachine
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileDisplayDataMapper
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileViewModel
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileDisplayDataMapper
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditUiStateMachine
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileDisplayDataMapper
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditUiStateMachine
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditViewModel
import com.workfort.pstuian.ui.settings.SettingsUiStateMachine
import com.workfort.pstuian.ui.settings.SettingsViewModel
import com.workfort.pstuian.ui.signin.SignInUiStateMachine
import com.workfort.pstuian.ui.signin.SignInViewModel
import com.workfort.pstuian.ui.splash.SplashUiStateMachine
import com.workfort.pstuian.ui.splash.SplashViewModel
import com.workfort.pstuian.ui.students.StudentsUiStateMachine
import com.workfort.pstuian.ui.students.StudentsViewModel
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val appCommonModule = module {
    singleOf(::AppViewModel)
    singleOf(::SharedScreenData)
    singleOf(::AppNavigator)
    singleOf(::DeepLinkNavigator)
    singleOf(::AppLaunchDeepLinkController)
}

private val profileScreenStateModule = module {
    factoryOf(::ProfileScreenUiStateMachine)
}

private val bloodDonationInputModule = module {
    factoryOf(::BloodDonationInputUiStateMachine)
    factory { (donationId: Int?, requestId: Int?, userId: Int, userType: UserType) ->
        BloodDonationInputViewModel(
            donationId = donationId,
            requestId = requestId,
            userId = userId,
            userType = userType,
            bloodDonationRepository = get(),
            dateTimeUtil = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val bloodDonationRequestCreateModule = module {
    factoryOf(::BloodDonationRequestCreateUiStateMachine)
    factoryOf(::BloodDonationRequestCreateViewModel)
}

private val bloodDonationRequestListModule = module {
    factoryOf(::BloodDonationRequestDisplayDataMapper)
    factoryOf(::BloodDonationRequestListUiStateMachine)
    factoryOf(::BloodDonationRequestListViewModel)
}

private val changePasswordModule = module {
    factoryOf(::ChangePasswordUiStateMachine)
    factory { (resetPasswordParams: ResetPasswordParams?) ->
        ChangePasswordViewModel(
            authRepository = get(),
            settingsRepository = get(),
            screenData = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
            resetPasswordParams = resetPasswordParams,
        )
    }
}

private val checkInModule = module {
    factoryOf(::CheckInDisplayDataMapper)
    factoryOf(::CheckInUiStateMachine)
    factoryOf(::CheckInViewModel)
}

private val imageUploadModule = module {
    factoryOf(::ImageUploadUiStateMachine)
    factory { (userId: Int, userType: UserType) ->
        ImageUploadViewModel(
            userId = userId,
            userType = userType,
            fileHandlerRepository = get(),
            studentRepository = get(),
            teacherRepository = get(),
            fileUtil = get(),
            imageUtil = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val cvDownloadModule = module {
    factoryOf(::CvDownloadUiStateMachine)
    factory { (userId: Int, userType: UserType, urlToDownload: String) ->
        CvDownloadViewModel(
            userId = userId,
            userType = userType,
            urlToDownload = urlToDownload,
            fileUtil = get(),
            fileRemoteFetcher = get(),
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
            studentRepository = get(),
            fileHandlerRepository = get(),
            fileUtil = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val donationHistoryModule = module {
    factoryOf(::DonationHistoryUiStateMachine)
    factoryOf(::DonationHistoryViewModel)
}

private val donateModule = module {
    factoryOf(::DonateUiStateMachine)
    factoryOf(::DonateViewModel)
}

private val facultyModule = module {
    factoryOf(::FacultyUiStateMachine)
    factoryOf(::BatchUiStateMachine)
    factoryOf(::TeacherUiStateMachine)
    factoryOf(::CourseUiStateMachine)
    factoryOf(::EmployeeUiStateMachine)

    factory { (facultyId: Int) ->
        FacultyViewModel(
            facultyId = facultyId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
    factory { (facultyId: Int) ->
        BatchViewModel(
            facultyId = facultyId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
    factory { (facultyId: Int) ->
        TeacherViewModel(
            facultyId = facultyId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
    factory { (facultyId: Int) ->
        CourseViewModel(
            facultyId = facultyId,
            facultyRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
    factory { (facultyId: Int) ->
        EmployeeViewModel(
            facultyId = facultyId,
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

private val notificationModule = module {
    factoryOf(::NotificationUiStateMachine)
    factoryOf(::SystemNotificationUiStateMachine)
    factoryOf(::CustomNotificationUiStateMachine)
    factoryOf(::NotificationDisplayDataMapper)
    factoryOf(::NotificationViewModel)
    factoryOf(::SystemNotificationViewModel)
    factoryOf(::CustomNotificationViewModel)
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
            getSignedInUserUseCase = get(),
            authRepository = get(),
            settingsRepository = get(),
            sharedScreenData = get(),
            platformInfo = get(),
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

private val profileModule = module {
    factoryOf(::UserPresenceDisplayDataMapper)
    factoryOf(::StudentProfileDisplayDataMapper)
    factoryOf(::TeacherProfileDisplayDataMapper)
    factoryOf(::EmployeeProfileDisplayDataMapper)

    factory { (userId: Int) ->
        StudentProfileViewModel(
            userId = userId,
            studentRepo = get(),
            authRepo = get(),
            userPresenceRepository = get(),
            getStudentProfileUserUseCase = get(),
            studentProfileDisplayDataMapper = get(),
            userPresenceDisplayDataMapper = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }

    factory { (userId: Int) ->
        TeacherProfileViewModel(
            userId = userId,
            teacherRepo = get(),
            authRepo = get(),
            userPresenceRepository = get(),
            getTeacherProfileUserUseCase = get(),
            teacherProfileDisplayDataMapper = get(),
            userPresenceDisplayDataMapper = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }

    factory { (userId: Int) ->
        EmployeeProfileViewModel(
            userId = userId,
            authRepo = get(),
            getEmployeeProfileUserUseCase = get(),
            employeeProfileDisplayDataMapper = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val deleteAccountModule = module {
    factoryOf(::DeleteAccountUiStateMachine)
    factoryOf(::DeleteAccountViewModel)
}

private val bloodDonationHistoryModule = module {
    factoryOf(::BloodDonationHistoryUiStateMachine)
    factory { (userId: Int, userType: UserType) ->
        BloodDonationHistoryViewModel(
            userId = userId,
            userType = userType,
            donationRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val checkInHistoryModule = module {
    factoryOf(::CheckInHistoryUiStateMachine)
    factory { (userId: Int, userType: UserType) ->
        CheckInHistoryViewModel(
            userId = userId,
            userType = userType,
            checkInRepo = get(),
            uiStateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val settingsModule = module {
    factoryOf(::SettingsUiStateMachine)
    factoryOf(::SettingsViewModel)
}

private val studentProfileEditModule = module {
    factoryOf(::StudentProfileEditUiStateMachine)
    factory { (userId: Int) ->
        StudentProfileEditViewModel(
            userId = userId,
            studentRepo = get(),
            facultyRepository = get(),
            getStudentProfileUserUseCase = get(),
            stateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

private val teacherProfileEditModule = module {
    factoryOf(::TeacherProfileEditUiStateMachine)
    factory { (userId: Int) ->
        TeacherProfileEditViewModel(
            userId = userId,
            teacherRepository = get(),
            facultyRepository = get(),
            getTeacherProfileUserUseCase = get(),
            stateMachine = get(),
            coroutineDispatcherProvider = get(),
        )
    }
}

val featurePresentationModule = listOf(
    appCommonModule,
    profileScreenStateModule,
    bloodDonationInputModule,
    bloodDonationRequestCreateModule,
    bloodDonationRequestListModule,
    changePasswordModule,
    checkInModule,
    imageUploadModule,
    cvDownloadModule,
    cvUploadModule,
    donationHistoryModule,
    donateModule,
    facultyModule,
    homeModule,
    notificationModule,
    signInModule,
    splashModule,
    studentsModule,
    profileModule,
    deleteAccountModule,
    bloodDonationHistoryModule,
    checkInHistoryModule,
    settingsModule,
    studentProfileEditModule,
    teacherProfileEditModule,
)
