package com.workfort.pstuian.ui.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreen
import com.workfort.pstuian.ui.blooddonationrequestlist.BloodDonationRequestListScreen
import com.workfort.pstuian.ui.changepassword.ChangePasswordScreen
import com.workfort.pstuian.ui.common.composable.ProvideCoilImageLoader
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.contactus.ContactUsScreen
import com.workfort.pstuian.ui.cvdownload.CvDownloadScreen
import com.workfort.pstuian.ui.cvupload.CvUploadScreen
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountScreen
import com.workfort.pstuian.ui.donate.DonateScreen
import com.workfort.pstuian.ui.donors.DonorsScreen
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileScreen
import com.workfort.pstuian.ui.faculty.FacultyScreen
import com.workfort.pstuian.ui.home.HomeScreen
import com.workfort.pstuian.ui.imagepreview.ImagePreviewScreen
import com.workfort.pstuian.ui.imageupload.ImageUploadScreen
import com.workfort.pstuian.ui.locationpicker.LocationPickerScreen
import com.workfort.pstuian.ui.myblooddonationlist.MyBloodDonationListScreen
import com.workfort.pstuian.ui.mycheckinlist.MyCheckInListScreen
import com.workfort.pstuian.ui.mydevicelist.MyDeviceListScreen
import com.workfort.pstuian.ui.settings.SettingsScreen
import com.workfort.pstuian.ui.signin.SignInScreen
import com.workfort.pstuian.ui.splash.SplashScreen
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileScreen
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditScreen
import com.workfort.pstuian.ui.students.StudentsScreen
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileScreen
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    theme: ThemeMode = ThemeMode.System,
    navController: NavHostController = rememberNavController(),
    navigator: AppNavigator = koinInject(),
) {
    ProvideCoilImageLoader()

    AppTheme(theme = theme) {
        Surface {
            LaunchedEffect(Unit) {
                navigator.events.collect { event ->
                    when (event) {
                        is NavEvent.Navigate -> {
                            navController.navigate(event.screen)
                        }

                        is NavEvent.ResetTo -> {
                            navController.navigate(event.screen) {
                                popUpTo(event.screen) { inclusive = false }
                            }
                        }

                        is NavEvent.ReplaceWith -> {
                            val currentDestination = navController.currentDestination
                            navController.navigate(event.screen) {
                                if (currentDestination != null) {
                                    popUpTo(currentDestination.id) { inclusive = true }
                                }
                            }
                        }

                        is NavEvent.ResetAll -> {
                            navController.navigate(event.screen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }

                        is NavEvent.Back -> {
                            navController.popBackStack()
                        }

                        is NavEvent.PopToRoot -> {
                            // Splash is removed from the stack when leaving it (resetAll in SplashScreen),
                            // so popBackStack to Splash would not find a destination. Clear the graph and
                            // land on splash like a fresh launch.
                            navController.navigate(AppScreen.Splash) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = AppScreen.Splash,
                modifier = modifier,
                typeMap = navTypeMap,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                composable<AppScreen.Splash> {
                    SplashScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.SignIn> {
                    SignInScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.ChangePassword> {
                    ChangePasswordScreen(viewModel = koinViewModel())
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
                composable<AppScreen.Profile>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
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
                composable<AppScreen.MyBloodDonationList>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.MyBloodDonationList = backStackEntry.toRoute()
                    MyBloodDonationListScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
                    )
                }
                composable<AppScreen.MyCheckInList>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.MyCheckInList = backStackEntry.toRoute()
                    MyCheckInListScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) }
                    )
                }
                composable<AppScreen.MyDeviceList>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.MyDeviceList = backStackEntry.toRoute()
                    MyDeviceListScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.StudentProfileEdit>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.StudentProfileEdit = backStackEntry.toRoute()
                    StudentProfileEditScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.action) }
                    )
                }
                composable<AppScreen.TeacherProfileEdit>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.TeacherProfileEdit = backStackEntry.toRoute()
                    TeacherProfileEditScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.action) }
                    )
                }
                composable<AppScreen.EmployeeProfileEdit>(
                    typeMap = navTypeMap
                ) {
                    // TODO: Need Screen for EmployeeProfileEdit
                }
                composable<AppScreen.DeleteAccount> {
                    DeleteAccountScreen(viewModel = koinViewModel())
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
                composable<AppScreen.ImageUpload>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
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
                composable<AppScreen.DownloadCv>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
                    val screen: AppScreen.DownloadCv = backStackEntry.toRoute()
                    CvDownloadScreen(
                        viewModel = koinViewModel {
                            parametersOf(screen.userId, screen.userType, screen.url)
                        }
                    )
                }
                composable<AppScreen.UploadCv>(
                    typeMap = navTypeMap
                ) { backStackEntry ->
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
    }
}
