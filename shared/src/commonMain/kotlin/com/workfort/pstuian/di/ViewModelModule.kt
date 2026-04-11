package com.workfort.pstuian.di

import com.workfort.pstuian.viewmodel.faculty.FacultyViewModel
import com.workfort.pstuian.viewmodel.home.HomeViewModel
import com.workfort.pstuian.viewmodel.notification.NotificationViewModel
import com.workfort.pstuian.viewmodel.settings.SettingsViewModel
import com.workfort.pstuian.viewmodel.signup.SignUpViewModel
import com.workfort.pstuian.viewmodel.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.viewmodel.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.viewmodel.students.StudentsViewModel
import com.workfort.pstuian.viewmodel.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.viewmodel.teacherprofileedit.TeacherProfileEditViewModel
import com.workfort.pstuian.viewmodel.signin.SignInViewModel
import com.workfort.pstuian.viewmodel.splash.SplashViewModel
import com.workfort.pstuian.viewmodel.blooddonationcreate.BloodDonationCreateViewModel
import com.workfort.pstuian.viewmodel.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.viewmodel.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.viewmodel.checkinlist.CheckInListViewModel
import com.workfort.pstuian.viewmodel.donate.DonateViewModel
import com.workfort.pstuian.viewmodel.donors.DonorsViewModel
import com.workfort.pstuian.viewmodel.emailverification.EmailVerificationViewModel
import com.workfort.pstuian.viewmodel.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.viewmodel.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.viewmodel.FacultyPickerViewModel
import com.workfort.pstuian.viewmodel.LocationPickerViewModel
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.FacultySelectionMode
import org.koin.dsl.module

val viewModelModule = module {
    factory { SplashViewModel(get(), get(), get(), get()) }
    factory { HomeViewModel(get(), get(), get(), get(), get()) }
    factory { SignInViewModel(get(), get()) }
    factory { SignUpViewModel(get(), get(), get()) }
    factory { (facultyId: Int) -> FacultyViewModel(facultyId, get(), get()) }
    factory { SettingsViewModel(get(), get()) }
    factory { NotificationViewModel(get(), get()) }
    factory { (userId: Int) -> StudentProfileViewModel(userId, get(), get(), get()) }
    factory { (userId: Int) -> TeacherProfileViewModel(userId, get(), get(), get()) }
    factory { (userId: Int, mode: ProfileEditMode) ->
        StudentProfileEditViewModel(userId, mode, get(), get(), get())
    }
    factory { (batchId: Int) -> StudentsViewModel(batchId, get(), get()) }
    factory { (userId: Int, mode: ProfileEditMode) ->
        TeacherProfileEditViewModel(userId, mode, get(), get(), get())
    }
    factory { CheckInListViewModel(get(), get(), get(), get()) }
    factory { (userId: Int, userType: UserType) ->
        MyCheckInListViewModel(userId, userType, get(), get())
    }
    factory { EmailVerificationViewModel(get(), get()) }
    factory { DonateViewModel(get(), get(), get()) }
    factory { DonorsViewModel(get(), get()) }
    factory { BloodDonationRequestListViewModel(get(), get()) }
    factory { BloodDonationRequestCreateViewModel(get(), get()) }
    factory { BloodDonationCreateViewModel(get(), get()) }
    factory { (userId: Int, userType: UserType) ->
        MyBloodDonationListViewModel(userId, userType, get(), get())
    }
    factory { (params: org.koin.core.parameter.ParametersHolder) ->
        FacultyPickerViewModel(
            selectionMode = params.get(),
            selectedFacultyId = params.get(),
            selectedBatchId = params.get(),
            facultyRepo = get(),
            stateReducer = get(),
        )
    }
    factory { (isCheckInMode: Boolean) ->
        LocationPickerViewModel(isCheckInMode, get(), get())
    }
}
