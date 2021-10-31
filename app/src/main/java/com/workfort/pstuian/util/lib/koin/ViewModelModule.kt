package com.workfort.pstuian.util.lib.koin

import com.workfort.pstuian.app.ui.common.viewmodel.AuthViewModel
import com.workfort.pstuian.app.ui.common.viewmodel.FileHandlerViewModel
import com.workfort.pstuian.app.ui.donors.viewmodel.DonorsViewModel
import com.workfort.pstuian.app.ui.faculty.viewmodel.FacultyViewModel
import com.workfort.pstuian.app.ui.home.viewmodel.HomeViewModel
import com.workfort.pstuian.app.ui.notification.viewmodel.NotificationViewModel
import com.workfort.pstuian.app.ui.studentprofile.viewmodel.StudentProfileViewModel
import com.workfort.pstuian.app.ui.students.viewmodel.StudentsViewModel
import com.workfort.pstuian.app.ui.support.viewmodel.SupportViewModel
import com.workfort.pstuian.app.ui.teacherprofile.viewmodel.TeacherProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:08 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { DonorsViewModel(get()) }
    viewModel { FacultyViewModel(get()) }
    viewModel { StudentsViewModel(get()) }
    viewModel { StudentProfileViewModel(get()) }
    viewModel { TeacherProfileViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { FileHandlerViewModel() }
    viewModel { SupportViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
}