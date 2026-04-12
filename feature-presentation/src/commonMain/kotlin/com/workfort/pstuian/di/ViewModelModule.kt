package com.workfort.pstuian.di

import com.workfort.pstuian.app.ui.commonmodel.faculty.FacultyViewModel
import com.workfort.pstuian.app.ui.commonmodel.home.HomeViewModel
import com.workfort.pstuian.app.ui.commonmodel.notification.NotificationViewModel
import com.workfort.pstuian.app.ui.commonmodel.settings.SettingsViewModel
import com.workfort.pstuian.app.ui.commonmodel.signup.SignUpViewModel
import com.workfort.pstuian.app.ui.commonmodel.studentprofile.StudentProfileViewModel
import com.workfort.pstuian.app.ui.commonmodel.studentprofileedit.StudentProfileEditViewModel
import com.workfort.pstuian.app.ui.commonmodel.students.StudentsViewModel
import com.workfort.pstuian.app.ui.commonmodel.teacherprofile.TeacherProfileViewModel
import com.workfort.pstuian.app.ui.commonmodel.teacherprofileedit.TeacherProfileEditViewModel
import com.workfort.pstuian.app.ui.commonmodel.signin.SignInViewModel
import com.workfort.pstuian.app.ui.commonmodel.splash.SplashViewModel
import com.workfort.pstuian.app.ui.commonmodel.blooddonationcreate.BloodDonationCreateViewModel
import com.workfort.pstuian.app.ui.commonmodel.blooddonationrequestcreate.BloodDonationRequestCreateViewModel
import com.workfort.pstuian.app.ui.commonmodel.blooddonationrequestlist.BloodDonationRequestListViewModel
import com.workfort.pstuian.app.ui.commonmodel.checkinlist.CheckInListViewModel
import com.workfort.pstuian.app.ui.commonmodel.donate.DonateViewModel
import com.workfort.pstuian.app.ui.commonmodel.donors.DonorsViewModel
import com.workfort.pstuian.app.ui.commonmodel.emailverification.EmailVerificationViewModel
import com.workfort.pstuian.app.ui.commonmodel.myblooddonationlist.MyBloodDonationListViewModel
import com.workfort.pstuian.app.ui.commonmodel.mycheckinlist.MyCheckInListViewModel
import com.workfort.pstuian.app.ui.commonmodel.mydevicelist.MyDeviceListViewModel
import com.workfort.pstuian.app.ui.commonmodel.FacultyPickerViewModel
import com.workfort.pstuian.app.ui.commonmodel.LocationPickerViewModel
import com.workfort.pstuian.app.ui.commonmodel.imageupload.ImageUploadViewModel
import com.workfort.pstuian.app.ui.commonmodel.cvupload.CvUploadViewModel
import com.workfort.pstuian.app.ui.commonmodel.cvdownload.CvDownloadViewModel
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
    factory { MyDeviceListViewModel(get(), get()) }
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
    factory { (userId: Int, userType: UserType) ->
        ImageUploadViewModel(userId, userType, get())
    }
    factory { (userId: Int, userType: UserType) ->
        CvUploadViewModel(userId, userType, get())
    }
    factory { (userId: Int, userType: UserType, urlToDownload: String) ->
        CvDownloadViewModel(userId, userType, urlToDownload, get())
    }
}
