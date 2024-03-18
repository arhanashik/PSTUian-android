package com.workfort.pstuian.util.di

import com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel.BloodDonationViewModel
import com.workfort.pstuian.app.ui.checkin.viewmodel.CheckInViewModel
import com.workfort.pstuian.app.ui.common.locationpicker.viewmodel.CheckInLocationViewModel
import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.notification.viewmodel.NotificationViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.support.viewmodel.SupportViewModel
import com.workfort.pstuian.app.ui.teacherprofile.viewmodel.TeacherProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { DonorsViewModel(get()) }
    viewModel { FacultyViewModel(get()) }
    viewModel { StudentProfileViewModel(get()) }
    viewModel { TeacherProfileViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { SupportViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { BloodDonationViewModel(get(), get()) }
    viewModel { CheckInViewModel(get()) }
    viewModel { CheckInLocationViewModel(get()) }
}