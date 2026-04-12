package com.workfort.pstuian.util.di

import com.workfort.pstuian.app.ui.blooddonationcreate.BloodDonationCreateViewModel
import com.workfort.pstuian.app.ui.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.app.ui.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.app.ui.changepassword.ChangePasswordViewModel
import com.workfort.pstuian.app.ui.checkinlist.CheckInListViewModel
import com.workfort.pstuian.app.ui.common.domain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.app.ui.common.domain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.app.ui.common.infrastructure.usecase.ClearAllDataUseCaseImpl
import com.workfort.pstuian.app.ui.common.infrastructure.usecase.RegisterDeviceUseCaseImpl
import com.workfort.pstuian.app.ui.contactus.ContactUsViewModel
import com.workfort.pstuian.app.ui.deleteaccount.DeleteAccountViewModel
import com.workfort.pstuian.app.ui.donate.DonateViewModel
import com.workfort.pstuian.app.ui.donors.DonorsViewModel
import com.workfort.pstuian.app.ui.emailverification.EmailVerificationViewModel
import com.workfort.pstuian.app.ui.forgotpassword.ForgotPasswordViewModel
import com.workfort.pstuian.app.ui.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.app.ui.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.app.ui.notification.NotificationViewModel
import com.workfort.pstuian.app.ui.settings.SettingsViewModel
import com.workfort.pstuian.app.ui.signin.SignInViewModel
import com.workfort.pstuian.app.ui.signup.SignUpViewModel
import com.workfort.pstuian.app.ui.splash.SplashViewModel
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.blooddonationcreate.BloodDonationCreateScreenStateReducer
import com.workfort.pstuian.reducer.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreenStateReducer
import com.workfort.pstuian.reducer.ui.blooddonationrequestlist.BloodDonationRequestListScreenStateReducer
import com.workfort.pstuian.reducer.ui.changepassword.ChangePasswordScreenStateReducer
import com.workfort.pstuian.reducer.ui.checkinlist.CheckInListScreenStateReducer
import com.workfort.pstuian.reducer.ui.common.facultypicker.FacultyPickerScreenStateReducer
import com.workfort.pstuian.reducer.ui.common.locationpicker.LocationPickerScreenStateReducer
import com.workfort.pstuian.reducer.ui.contactus.ContactUsScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.deleteaccount.DeleteAccountScreenStateReducer
import com.workfort.pstuian.reducer.ui.donate.DonateScreenStateReducer
import com.workfort.pstuian.reducer.ui.donors.DonorsScreenStateReducer
import com.workfort.pstuian.reducer.ui.emailverification.EmailVerificationScreenStateReducer
import com.workfort.pstuian.reducer.ui.employeeprofile.EmployeeProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.faculty.FacultyScreenStateReducer
import com.workfort.pstuian.reducer.ui.forgotpassword.ForgotPasswordScreenStateReducer
import com.workfort.pstuian.reducer.ui.home.HomeScreenStateReducer
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.myblooddonationlist.MyBloodDonationListScreenStateReducer
import com.workfort.pstuian.reducer.ui.mycheckinlist.MyCheckInListScreenStateReducer
import com.workfort.pstuian.reducer.ui.mydevicelist.MyDeviceListScreenStateReducer
import com.workfort.pstuian.reducer.ui.notification.NotificationScreenStateReducer
import com.workfort.pstuian.reducer.ui.settings.SettingsScreenStateReducer
import com.workfort.pstuian.reducer.ui.signin.SignInScreenStateReducer
import com.workfort.pstuian.reducer.ui.signup.SignUpScreenStateReducer
import com.workfort.pstuian.reducer.ui.splash.SplashScreenStateReducer
import com.workfort.pstuian.reducer.ui.studentprofile.StudentProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.studentprofileedit.StudentProfileEditScreenStateReducer
import com.workfort.pstuian.reducer.ui.students.StudentsScreenStateReducer
import com.workfort.pstuian.reducer.ui.teacherprofile.TeacherProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.teacherprofileedit.TeacherProfileEditScreenStateReducer
import com.workfort.pstuian.viewmodel.FacultyPickerViewModel
import com.workfort.pstuian.viewmodel.LocationPickerViewModel
import com.workfort.pstuian.viewmodel.home.HomeViewModel
import com.workfort.pstuian.viewmodel.faculty.FacultyViewModel
import com.workfort.pstuian.viewmodel.students.StudentsViewModel
import com.workfort.pstuian.viewmodel.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.viewmodel.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.viewmodel.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.viewmodel.teacherprofileedit.TeacherProfileEditViewModel
import com.workfort.pstuian.viewmodel.employeeprofile.EmployeeProfileViewModel
import com.workfort.pstuian.viewmodel.mydevicelist.MyDeviceListViewModel
import com.workfort.pstuian.viewmodel.cvdownload.CvDownloadViewModel
import com.workfort.pstuian.viewmodel.cvupload.CvUploadViewModel
import com.workfort.pstuian.viewmodel.imageupload.ImageUploadViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val useCaseModule = module {
    factory<ClearAllDataUseCase> { ClearAllDataUseCaseImpl(get(), get(), get(), get()) }
    factory<RegisterDeviceUseCase> { RegisterDeviceUseCaseImpl(get()) }
}

val stateReducerModule = module {
    factory { BloodDonationCreateScreenStateReducer() }
    factory { BloodDonationRequestCreateScreenStateReducer() }
    factory { BloodDonationRequestListScreenStateReducer() }
    factory { ChangePasswordScreenStateReducer() }
    factory { CheckInListScreenStateReducer() }
    factory { ContactUsScreenStateReducer() }
    factory { CvDownloadScreenStateReducer() }
    factory { CvUploadScreenStateReducer() }
    factory { DeleteAccountScreenStateReducer() }
    factory { DonateScreenStateReducer() }
    factory { DonorsScreenStateReducer() }
    factory { FacultyPickerScreenStateReducer() }
    factory { FacultyScreenStateReducer() }
    factory { ForgotPasswordScreenStateReducer() }
    factory { EmailVerificationScreenStateReducer() }
    factory { EmployeeProfileScreenStateReducer() }
    factory { HomeScreenStateReducer() }
    factory { ImageUploadScreenStateReducer() }
    factory { LocationPickerScreenStateReducer() }
    factory { MyBloodDonationListScreenStateReducer() }
    factory { MyCheckInListScreenStateReducer() }
    factory { MyDeviceListScreenStateReducer() }
    factory { NotificationScreenStateReducer() }
    factory { SettingsScreenStateReducer() }
    factory { SignInScreenStateReducer() }
    factory { SignUpScreenStateReducer() }
    factory { SplashScreenStateReducer() }
    factory { StudentProfileScreenStateReducer() }
    factory { StudentProfileEditScreenStateReducer() }
    factory { StudentsScreenStateReducer() }
    factory { TeacherProfileEditScreenStateReducer() }
    factory { TeacherProfileScreenStateReducer() }
}

val viewModelModule = module {
    viewModel {
        SplashViewModel(
            authRepo = get(),
            clearAllDataUseCase = get(),
            registerDeviceUseCase = get(),
            reducer = get(),
        )
    }
    viewModel {
        HomeViewModel(
            authRepo = get(),
            sliderRepo = get(),
            facultyRepo = get(),
            clearAllDataUseCase = get(),
            stateReducer = get(),
        )
    }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get(), get()) }
    viewModel { EmailVerificationViewModel(get(), get()) }
    viewModel { ForgotPasswordViewModel(get(), get()) }
    viewModel { DonorsViewModel(get(), get()) }
    viewModel { (facultyId: Int) ->
        FacultyViewModel(facultyId, get(), get())
    }
    viewModel { (mode: FacultySelectionMode, facultyId: Int, batchId: Int) ->
        FacultyPickerViewModel(
            selectionMode = mode,
            selectedFacultyId = facultyId,
            selectedBatchId = batchId,
            facultyRepo = get(),
            stateReducer = get(),
        )
    }
    viewModel { (batchId: Int) ->
        StudentsViewModel(batchId, get(), get())
    }
    viewModel { (userId: Int) ->
        StudentProfileViewModel(userId, get(), get(), get())
    }
    viewModel { (userId: Int, mode: ProfileEditMode) ->
        StudentProfileEditViewModel(userId, mode, get(), get(), get())
    }
    viewModel { (userId: Int) ->
        TeacherProfileViewModel(userId, get(), get(), get())
    }
    viewModel { (userId: Int, mode: ProfileEditMode) ->
        TeacherProfileEditViewModel(userId, mode, get(), get(), get())
    }
    viewModel { (userId: Int) ->
        EmployeeProfileViewModel(userId, get(), get())
    }
    viewModel { ContactUsViewModel(get(), get()) }
    viewModel { DonateViewModel(get(), get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }
    viewModel { BloodDonationCreateViewModel(get(), get()) }
    viewModel { (userId: Int, userType: UserType) ->
        MyBloodDonationListViewModel(userId, userType, get(), get())
    }
    viewModel { BloodDonationRequestCreateViewModel(get(), get()) }
    viewModel { BloodDonationRequestListViewModel(get(), get()) }
    viewModelOf(::CheckInListViewModel)
    viewModel { (userId: Int, userType: UserType) ->
        MyCheckInListViewModel(
            userId = userId,
            userType = userType,
            checkInRepo = get(),
            reducer = get(),
        )
    }
    viewModel { (isCheckInMode: Boolean) ->
        LocationPickerViewModel(
            isCheckInMode = isCheckInMode,
            checkInLocationRepo = get(),
            reducer = get(),
        )
    }
    viewModel {
        MyDeviceListViewModel(
            authRepo = get(),
            reducer = get(),
        )
    }
    viewModel {
        ChangePasswordViewModel(
            authRepo = get(),
            reducer = get(),
        )
    }
    viewModel {
        DeleteAccountViewModel(
            authRepo = get(),
            reducer = get(),
        )
    }
    viewModel { (userId: Int, userType: UserType, url: String) ->
        CvDownloadViewModel(
            userId = userId,
            userType = userType,
            urlToDownload = url,
            reducer = get(),
        )
    }
    viewModel { (userId: Int, userType: UserType) ->
        CvUploadViewModel(
            userId = userId,
            userType = userType,
            reducer = get(),
        )
    }
    viewModel { (userId: Int, userType: UserType) ->
        ImageUploadViewModel(
            userId = userId,
            userType = userType,
            reducer = get(),
        )
    }
    viewModel {
        SettingsViewModel(
            repo = get(),
            reducer = get(),
        )
    }
}
