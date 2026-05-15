package com.workfort.pstuian.ui.common.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.BloodDonationHistoryScreen
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.BloodDonationInputScreen
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.BloodDonationRequestCreateScreen
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.BloodDonationRequestListScreen
import com.workfort.pstuian.ui.changepassword.ChangePasswordScreen
import com.workfort.pstuian.ui.checkin.CheckInScreen
import com.workfort.pstuian.ui.checkinhistory.CheckInHistoryScreen
import com.workfort.pstuian.ui.common.composable.ProvideCoilImageLoader
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.deleteaccount.DeleteAccountScreen
import com.workfort.pstuian.ui.donation.donate.DonateScreen
import com.workfort.pstuian.ui.donation.donationhistory.DonationHistoryScreen
import com.workfort.pstuian.ui.faculty.faculty.FacultyScreen
import com.workfort.pstuian.ui.home.HomeScreen
import com.workfort.pstuian.ui.notification.notification.NotificationScreen
import com.workfort.pstuian.ui.imagepreview.ImagePreviewScreen
import com.workfort.pstuian.ui.imageupload.ImageUploadScreen
import com.workfort.pstuian.ui.locationpicker.LocationPickerScreen
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileScreen
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileScreen
import com.workfort.pstuian.ui.profile.studentprofileedit.StudentProfileEditScreen
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileScreen
import com.workfort.pstuian.ui.profile.teacherprofileedit.TeacherProfileEditScreen
import com.workfort.pstuian.ui.settings.SettingsScreen
import com.workfort.pstuian.ui.signin.SignInScreen
import com.workfort.pstuian.ui.splash.SplashScreen
import com.workfort.pstuian.ui.students.StudentsScreen
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    themeMode: ThemeMode = ThemeMode.System,
    navController: NavHostController = rememberNavController(),
    navigator: AppNavigator = koinInject(),
    overlayContent: @Composable () -> Unit = {},
) {
    ProvideCoilImageLoader()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Splash is [AppNavHost]'s start destination. `toRoute<AppScreen>()` can fail for the root
    // typed route on some frames, so rely on the graph start id (and null entry before the host
    // attaches) instead of only parsing the route.
    val onSplashDestination =
        navBackStackEntry == null ||
            navController.currentDestination?.id == navController.graph.startDestinationId
    val currentAppScreen = navBackStackEntry?.let { entry ->
        runCatching { entry.toRoute<AppScreen>() }.getOrNull()
    }
    val useSchemeBackgroundForStatusBar =
        onSplashDestination || currentAppScreen is AppScreen.Profile
    AppTheme(
        themeMode = themeMode,
        systemBarSyncKey = navBackStackEntry?.id,
        useSchemeBackgroundForStatusBar = useSchemeBackgroundForStatusBar,
    ) {
        Surface {
            LaunchedEffect(Unit) {
                navigator.events.collect { event ->
                    when (event) {
                        is NavEvent.Navigate -> {
                            safeNavigate(navController) {
                                when (val screen = event.screen) {
                                    is AppScreen.ChangePassword -> {
                                        while (popBackStack<AppScreen.ChangePassword>(inclusive = true)) {
                                        }
                                        navigate(screen)
                                    }
                                    else -> navigate(screen)
                                }
                            }
                        }

                        is NavEvent.ResetTo -> {
                            safeNavigate(navController) {
                                navigate(event.screen) {
                                    popUpTo(event.screen) { inclusive = true }
                                }
                            }
                        }

                        is NavEvent.ReplaceWith -> {
                            safeNavigate(navController) {
                                val currentDestination = navController.currentDestination
                                navigate(event.screen) {
                                    if (currentDestination != null) {
                                        popUpTo(currentDestination.id) { inclusive = true }
                                    }
                                }
                            }
                        }

                        is NavEvent.ReplaceAll -> {
                            safeNavigate(navController) {
                                navigate(event.screen) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }

                        is NavEvent.Back -> {
                            navController.popBackStack()
                        }

                        is NavEvent.PopToRoot -> {
                            // Splash is removed from the stack when leaving it (resetAll in SplashScreen),
                            // so popBackStack to Splash would not find a destination. Clear the graph and
                            // land on splash like a fresh launch.
                            safeNavigate(navController) {
                                navigate(AppScreen.Splash) {
                                    popUpTo(0) { inclusive = true }
                                }
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
                composable<AppScreen.ChangePassword>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.ChangePassword = backStackEntry.toRoute()
                    ChangePasswordScreen(
                        viewModel = koinViewModel { parametersOf(screen.resetPasswordParams) },
                    )
                }
                composable<AppScreen.Home> {
                    HomeScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.Students> { backStackEntry ->
                    val screen: AppScreen.Students = backStackEntry.toRoute()
                    StudentsScreen(viewModel = koinViewModel { parametersOf(screen.batchId) })
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
                composable<AppScreen.BloodDonationInput>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.BloodDonationInput = backStackEntry.toRoute()
                    BloodDonationInputScreen(
                        viewModel = koinViewModel {
                            parametersOf(screen.donationId, screen.requestId, screen.userId, screen.userType)
                        }
                    )
                }
                composable<AppScreen.Profile>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.Profile = backStackEntry.toRoute()
                    when (screen.userType) {
                        UserType.STUDENT -> StudentProfileScreen(
                            viewModel = koinViewModel { parametersOf(screen.userId) },
                        )

                        UserType.TEACHER -> TeacherProfileScreen(
                            viewModel = koinViewModel { parametersOf(screen.userId) },
                        )

                        UserType.EMPLOYEE -> EmployeeProfileScreen(
                            viewModel = koinViewModel { parametersOf(screen.userId) },
                        )
                    }
                }
                composable<AppScreen.BloodDonationHistory>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.BloodDonationHistory = backStackEntry.toRoute()
                    BloodDonationHistoryScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) },
                    )
                }
                composable<AppScreen.CheckInHistory>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.CheckInHistory = backStackEntry.toRoute()
                    CheckInHistoryScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) },
                    )
                }
                composable<AppScreen.StudentProfileEdit>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.StudentProfileEdit = backStackEntry.toRoute()
                    StudentProfileEditScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId) },
                    )
                }
                composable<AppScreen.TeacherProfileEdit>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.TeacherProfileEdit = backStackEntry.toRoute()
                    TeacherProfileEditScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId) },
                    )
                }
                composable<AppScreen.EmployeeProfileEdit>(typeMap = navTypeMap) {
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
                composable<AppScreen.ImageUpload>(typeMap = navTypeMap) { backStackEntry ->
                    val screen: AppScreen.ImageUpload = backStackEntry.toRoute()
                    ImageUploadScreen(
                        viewModel = koinViewModel { parametersOf(screen.userId, screen.userType) },
                    )
                }
                composable<AppScreen.ImagePreview> { backStackEntry ->
                    val screen: AppScreen.ImagePreview = backStackEntry.toRoute()
                    ImagePreviewScreen(
                        imageUrl = screen.encodedImageUrl,
                        onBack = { navController.popBackStack() },
                    )
                }
                composable<AppScreen.Settings> {
                    SettingsScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.Faculty> { backStackEntry ->
                    val screen: AppScreen.Faculty = backStackEntry.toRoute()
                    FacultyScreen(viewModel = koinViewModel { parametersOf(screen.facultyId) })
                }
                composable<AppScreen.CheckIn> {
                    CheckInScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.DonationHistory> {
                    DonationHistoryScreen(viewModel = koinViewModel())
                }
                composable<AppScreen.Notification> {
                    NotificationScreen(viewModel = koinViewModel())
                }
            }
            overlayContent()
        }
    }
}

private fun safeNavigate(
    navController: NavHostController,
    block: NavHostController.() -> Unit,
) {
    try {
        navController.block()
    } catch (e: Exception) {
        Napier.w("Navigation skipped: ${e.message}", e)
    }
}
