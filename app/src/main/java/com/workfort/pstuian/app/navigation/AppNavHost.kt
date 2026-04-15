package com.workfort.pstuian.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.common.navigation.NavEvent
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.blooddonationcreate.BloodDonationCreateScreen
import com.workfort.pstuian.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreen
import com.workfort.pstuian.ui.blooddonationrequestlist.BloodDonationRequestListScreen
import com.workfort.pstuian.ui.changepassword.ChangePasswordScreen
import com.workfort.pstuian.ui.checkinlist.CheckInListScreen
import com.workfort.pstuian.ui.contactus.ContactUsScreen
import com.workfort.pstuian.ui.cvdownload.CvDownloadScreen
import com.workfort.pstuian.ui.cvupload.CvUploadScreen
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountScreen
import com.workfort.pstuian.ui.donate.DonateScreen
import com.workfort.pstuian.ui.donors.DonorsScreen
import com.workfort.pstuian.ui.emailverification.EmailVerificationScreen
import com.workfort.pstuian.ui.employeeprofile.EmployeeProfileScreen
import com.workfort.pstuian.ui.faculty.FacultyScreen
import com.workfort.pstuian.ui.facultypicker.FacultyPickerScreen
import com.workfort.pstuian.ui.forgotpassword.ForgotPasswordScreen
import com.workfort.pstuian.ui.home.HomeScreen
import com.workfort.pstuian.ui.imagepreview.ImagePreviewScreen
import com.workfort.pstuian.ui.imageupload.ImageUploadScreen
import com.workfort.pstuian.ui.locationpicker.LocationPickerScreen
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListScreen
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListScreen
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListScreen
import com.workfort.pstuian.ui.settings.SettingsScreen
import com.workfort.pstuian.ui.signin.SignInScreen
import com.workfort.pstuian.ui.signup.SignUpScreen
import com.workfort.pstuian.ui.splash.SplashScreen
import com.workfort.pstuian.ui.studentprofile.StudentProfileScreen
import com.workfort.pstuian.ui.studentprofileedit.StudentProfileEditScreen
import com.workfort.pstuian.ui.students.StudentsScreen
import com.workfort.pstuian.ui.teacherprofile.TeacherProfileScreen
import com.workfort.pstuian.ui.teacherprofileedit.TeacherProfileEditScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigator: AppNavigator = koinInject(),
) {
    LaunchedEffect(Unit) {
        navigator.events.collect { event ->
            when (event) {
                is NavEvent.Navigate -> {
                    navController.navigate(event.screen)
                }
                is NavEvent.Back -> {
                    navController.popBackStack()
                }
                is NavEvent.PopToRoot -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppScreen.Splash,
        modifier = modifier,
    ) {
        composable<AppScreen.Splash> {
            SplashScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.SignIn> {
            SignInScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.SignUp> {
            SignUpScreen(
                viewModel = koinViewModel(),
                facultyId = null,
                batchId = null,
            )
        }
        composable<AppScreen.ChangePassword> {
            ChangePasswordScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.ForgotPassword> {
            ForgotPasswordScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.EmailVerification> {
            EmailVerificationScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.ContactUs> {
            ContactUsScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.Home> {
            HomeScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.Students> { backStackEntry ->
            val screen: AppScreen.Students = backStackEntry.toRoute()
            StudentsScreen(viewModel = koinViewModel { parametersOf(screen.batchId) })
        }
        composable<AppScreen.Teachers> { backStackEntry ->
            val screen: AppScreen.Teachers = backStackEntry.toRoute()
            TeacherProfileScreen(viewModel = koinViewModel { parametersOf(screen.userId) })
        }
        composable<AppScreen.Employees> { backStackEntry ->
            val screen: AppScreen.Employees = backStackEntry.toRoute()
            EmployeeProfileScreen(viewModel = koinViewModel { parametersOf(screen.userId) })
        }
        composable<AppScreen.BloodDonationRequestList> {
            BloodDonationRequestListScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.BloodDonationRequestCreate> {
            BloodDonationRequestCreateScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.BloodDonationRequestEdit> {
             // TODO: Need Screen for BloodDonationRequestEdit
        }
        composable<AppScreen.Profile> { backStackEntry ->
            val screen: AppScreen.Profile = backStackEntry.toRoute()
            when (screen.userType) {
                UserType.STUDENT -> StudentProfileScreen(
                    viewModel = koinViewModel { parametersOf(screen.userId) }
                )
                UserType.TEACHER -> TeacherProfileScreen(
                    viewModel = koinViewModel { parametersOf(screen.userId) }
                )
                UserType.EMPLOYEE -> EmployeeProfileScreen(
                    viewModel = koinViewModel { parametersOf(screen.userId) }
                )
            }
        }
        composable<AppScreen.MyBloodDonationList> { backStackEntry ->
            val screen: AppScreen.MyBloodDonationList = backStackEntry.toRoute()
            MyBloodDonationListScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
            )
        }
        composable<AppScreen.MyCheckInList> { backStackEntry ->
            val screen: AppScreen.MyCheckInList = backStackEntry.toRoute()
            MyCheckInListScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
            )
        }
        composable<AppScreen.MyDeviceList> {
            MyDeviceListScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.StudentProfileEdit> { backStackEntry ->
            val screen: AppScreen.StudentProfileEdit = backStackEntry.toRoute()
            StudentProfileEditScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.action) }
            )
        }
        composable<AppScreen.TeacherProfileEdit> { backStackEntry ->
            val screen: AppScreen.TeacherProfileEdit = backStackEntry.toRoute()
            TeacherProfileEditScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.action) }
            )
        }
        composable<AppScreen.EmployeeProfileEdit> {
            // TODO: Need Screen for EmployeeProfileEdit
        }
        composable<AppScreen.DeleteAccount> { backStackEntry ->
            val screen: AppScreen.DeleteAccount = backStackEntry.toRoute()
            DeleteAccountScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
            )
        }
        composable<AppScreen.LocationPicker> {
             LocationPickerScreen(
                 viewModel = koinViewModel(),
                 navController = navController,
             )
        }
        composable<AppScreen.Donate> {
            DonateScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.FacultyPicker> { backStackEntry ->
            val screen: AppScreen.FacultyPicker = backStackEntry.toRoute()
            FacultyPickerScreen(
                viewModel = koinViewModel {
                    parametersOf(screen.mode, screen.facultyId ?: -1, screen.batchId ?: -1)
                },
                navController = navController,
            )
        }
        composable<AppScreen.ImageUpload> { backStackEntry ->
            val screen: AppScreen.ImageUpload = backStackEntry.toRoute()
            ImageUploadScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
            )
        }
        composable<AppScreen.ImagePreview> { backStackEntry ->
            val screen: AppScreen.ImagePreview = backStackEntry.toRoute()
            ImagePreviewScreen(
                imageUrl = screen.encodedImageUrl,
                onBack = { navController.popBackStack() }
            )
        }
        composable<AppScreen.DownloadCv> { backStackEntry ->
            val screen: AppScreen.DownloadCv = backStackEntry.toRoute()
            CvDownloadScreen(
                viewModel = koinViewModel {
                    parametersOf(screen.userId, screen.userType, screen.url)
                }
            )
        }
        composable<AppScreen.UploadCv> { backStackEntry ->
            val screen: AppScreen.UploadCv = backStackEntry.toRoute()
            CvUploadScreen(
                viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
            )
        }
        composable<AppScreen.Settings> {
            SettingsScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.Faculty> { backStackEntry ->
            val screen: AppScreen.Faculty = backStackEntry.toRoute()
            FacultyScreen(viewModel = koinViewModel { parametersOf(screen.facultyId) })
        }
        composable<AppScreen.Donors> {
            DonorsScreen(viewModel = koinViewModel())
        }
        composable<AppScreen.Notification> {
            // TODO: Need Screen for Notification
        }
    }
}
