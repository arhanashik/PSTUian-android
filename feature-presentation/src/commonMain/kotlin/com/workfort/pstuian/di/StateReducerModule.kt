package com.workfort.pstuian.di

import com.workfort.pstuian.reducer.ui.faculty.FacultyScreenStateReducer
import com.workfort.pstuian.reducer.ui.home.HomeScreenStateReducer
import com.workfort.pstuian.reducer.ui.notification.NotificationScreenStateReducer
import com.workfort.pstuian.reducer.ui.settings.SettingsScreenStateReducer
import com.workfort.pstuian.reducer.ui.signup.SignUpScreenStateReducer
import com.workfort.pstuian.reducer.ui.studentprofile.StudentProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.studentprofileedit.StudentProfileEditScreenStateReducer
import com.workfort.pstuian.reducer.ui.students.StudentsScreenStateReducer
import com.workfort.pstuian.reducer.ui.teacherprofile.TeacherProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.teacherprofileedit.TeacherProfileEditScreenStateReducer
import com.workfort.pstuian.reducer.ui.signin.SignInScreenStateReducer
import com.workfort.pstuian.reducer.ui.splash.SplashScreenStateReducer
import com.workfort.pstuian.reducer.ui.blooddonationcreate.BloodDonationCreateScreenStateReducer
import com.workfort.pstuian.reducer.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreenStateReducer
import com.workfort.pstuian.reducer.ui.blooddonationrequestlist.BloodDonationRequestListScreenStateReducer
import com.workfort.pstuian.reducer.ui.checkinlist.CheckInListScreenStateReducer
import com.workfort.pstuian.reducer.ui.donate.DonateScreenStateReducer
import com.workfort.pstuian.reducer.ui.donors.DonorsScreenStateReducer
import com.workfort.pstuian.reducer.ui.emailverification.EmailVerificationScreenStateReducer
import com.workfort.pstuian.reducer.ui.mycheckinlist.MyCheckInListScreenStateReducer
import com.workfort.pstuian.reducer.ui.mydevicelist.MyDeviceListScreenStateReducer
import com.workfort.pstuian.reducer.ui.common.facultypicker.FacultyPickerScreenStateReducer
import com.workfort.pstuian.reducer.ui.common.locationpicker.LocationPickerScreenStateReducer
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenStateReducer
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenStateReducer
import org.koin.dsl.module

val stateReducerModule = module {
    single { SplashScreenStateReducer() }
    single { HomeScreenStateReducer() }
    single { SignInScreenStateReducer() }
    single { SignUpScreenStateReducer() }
    single { FacultyScreenStateReducer() }
    single { SettingsScreenStateReducer() }
    single { NotificationScreenStateReducer() }
    single { StudentProfileScreenStateReducer() }
    single { TeacherProfileScreenStateReducer() }
    single { StudentProfileEditScreenStateReducer() }
    single { StudentsScreenStateReducer() }
    single { TeacherProfileEditScreenStateReducer() }
    single { CheckInListScreenStateReducer() }
    single { MyCheckInListScreenStateReducer() }
    single { MyDeviceListScreenStateReducer() }
    single { EmailVerificationScreenStateReducer() }
    single { DonateScreenStateReducer() }
    single { DonorsScreenStateReducer() }
    single { BloodDonationRequestListScreenStateReducer() }
    single { BloodDonationRequestCreateScreenStateReducer() }
    single { BloodDonationCreateScreenStateReducer() }
    single { FacultyPickerScreenStateReducer() }
    single { LocationPickerScreenStateReducer() }
    single { ImageUploadScreenStateReducer() }
    single { CvUploadScreenStateReducer() }
    single { CvDownloadScreenStateReducer() }
}
