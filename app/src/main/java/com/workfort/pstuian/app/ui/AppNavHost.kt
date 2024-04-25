package com.workfort.pstuian.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.workfort.pstuian.app.ui.blooddonationcreate.BloodDonationCreateScreen
import com.workfort.pstuian.app.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreen
import com.workfort.pstuian.app.ui.blooddonationrequestlist.BloodDonationRequestListScreen
import com.workfort.pstuian.app.ui.changepassword.ChangePasswordScreen
import com.workfort.pstuian.app.ui.checkinlist.CheckInListScreen
import com.workfort.pstuian.app.ui.common.facultypicker.FacultyPickerScreen
import com.workfort.pstuian.app.ui.common.facultypicker.FacultyPickerViewModel
import com.workfort.pstuian.app.ui.common.locationpicker.LocationPickerScreen
import com.workfort.pstuian.app.ui.contactus.ContactUsScreen
import com.workfort.pstuian.app.ui.cvdownload.CvDownloadScreen
import com.workfort.pstuian.app.ui.cvupload.CvUploadScreen
import com.workfort.pstuian.app.ui.deleteaccount.DeleteAccountScreen
import com.workfort.pstuian.app.ui.donate.DonateScreen
import com.workfort.pstuian.app.ui.donors.DonorsScreen
import com.workfort.pstuian.app.ui.emailverification.EmailVerificationScreen
import com.workfort.pstuian.app.ui.employeeprofile.EmployeeProfileScreen
import com.workfort.pstuian.app.ui.faculty.FacultyScreen
import com.workfort.pstuian.app.ui.forgotpassword.ForgotPasswordScreen
import com.workfort.pstuian.app.ui.home.HomeScreen
import com.workfort.pstuian.app.ui.imagepreview.ImagePreviewScreen
import com.workfort.pstuian.app.ui.imageupload.ImageUploadScreen
import com.workfort.pstuian.app.ui.myblooddonationlist.MyBloodDonationListScreen
import com.workfort.pstuian.app.ui.mycheckinlist.MyCheckInListScreen
import com.workfort.pstuian.app.ui.mydevicelist.MyDeviceListScreen
import com.workfort.pstuian.app.ui.notification.NotificationScreen
import com.workfort.pstuian.app.ui.settings.SettingsScreen
import com.workfort.pstuian.app.ui.signin.SignInScreen
import com.workfort.pstuian.app.ui.signup.SignUpScreen
import com.workfort.pstuian.app.ui.splash.SplashScreen
import com.workfort.pstuian.app.ui.studentprofile.StudentProfileScreen
import com.workfort.pstuian.app.ui.studentprofileedit.StudentProfileEditScreen
import com.workfort.pstuian.app.ui.students.StudentsScreen
import com.workfort.pstuian.app.ui.teacherprofile.TeacherProfileScreen
import com.workfort.pstuian.app.ui.teacherprofileedit.TeacherProfileEditScreen
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.ProfileEditMode
import com.workfort.pstuian.model.UserType
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavItem.Splash.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavItem.Splash.route) {
            SplashScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.Home.route) {
            HomeScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.SignIn.route) {
            SignInScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.SignUp.route) {
            SignUpScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.Notification.route) {
            NotificationScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.Faculty.route.plus("/{${NavParam.FACULTY_ID}}"),
            arguments = listOf(
                navArgument(NavParam.FACULTY_ID) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.FACULTY_ID)?.let { id ->
                FacultyScreen(
                    modifier = modifier,
                    viewModel = getViewModel(parameters = { parametersOf(id) }),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.StudentList.route.plus("/{${NavParam.BATCH_ID}}"),
            arguments = listOf(
                navArgument(NavParam.BATCH_ID) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.BATCH_ID)?.let { id ->
                StudentsScreen(
                    modifier = modifier,
                    viewModel = getViewModel(parameters = { parametersOf(id) }),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.StudentProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.USER_ID)?.let { userId ->
                StudentProfileScreen(
                    modifier = modifier,
                    viewModel = getViewModel(parameters = { parametersOf(userId) }),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.StudentProfileEdit.route.plus(
                "/{${NavParam.USER_ID}}/{${NavParam.MODE}}",
            ),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
                navArgument(NavParam.MODE) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.USER_ID)?.let { userId ->
                it.arguments?.getInt(NavParam.MODE)?.let { actionInt ->
                    ProfileEditMode.create(actionInt)?.let { action ->
                        StudentProfileEditScreen(
                            modifier = modifier,
                            viewModel = getViewModel(parameters = { parametersOf(userId, action) }),
                            navController = navController,
                        )
                    }
                }
            }
        }

        composable(
            NavItem.TeacherProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.USER_ID)?.let { userId ->
                TeacherProfileScreen(
                    modifier = modifier,
                    viewModel = getViewModel(parameters = { parametersOf(userId) }),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.TeacherProfileEdit.route.plus(
                "/{${NavParam.USER_ID}}/{${NavParam.MODE}}",
            ),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
                navArgument(NavParam.MODE) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.USER_ID )?.let { userId ->
                it.arguments?.getInt(NavParam.MODE)?.let { actionInt ->
                    ProfileEditMode.create(actionInt)?.let { action ->
                        TeacherProfileEditScreen(
                            modifier = modifier,
                            viewModel = getViewModel(parameters = { parametersOf(userId, action) }),
                            navController = navController,
                        )
                    }
                }
            }
        }

        composable(
            NavItem.EmployeeProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            it.arguments?.getInt(NavParam.USER_ID)?.let { userId ->
                EmployeeProfileScreen(
                    modifier = modifier,
                    viewModel = getViewModel(parameters = { parametersOf(userId) }),
                    navController = navController,
                )
            }
        }

        composable(NavItem.EmailVerification.route) {
            EmailVerificationScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.ForgotPassword.route) {
            ForgotPasswordScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.CheckInList.route) {
            CheckInListScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.MyCheckInList.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID)
            val userTypeStr = it.arguments?.getString(NavParam.USER_TYPE)
            if (userId != null && userId != -1 && userTypeStr != null) {
                val userType = UserType.create(userTypeStr)
                MyCheckInListScreen(
                    modifier = modifier,
                    viewModel = getViewModel(
                        parameters = { parametersOf(userId, userType) },
                    ),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.ImagePreview.route.plus("/{${NavParam.URL}}")) {
            it.arguments?.getString(NavParam.URL)?.let { imageUrl ->
                ImagePreviewScreen(
                    modifier = modifier,
                    navController = navController,
                    imageUrl = imageUrl,
                )
            }
        }

        composable(NavItem.ContactUs.route) {
            ContactUsScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.Donors.route) {
            DonorsScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.Donate.route) {
            DonateScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.Settings.route) {
            SettingsScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.BloodDonationCreate.route) {
            BloodDonationCreateScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.MyBloodDonationList.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID)
            val userTypeStr = it.arguments?.getString(NavParam.USER_TYPE)
            if (userId != null && userId != -1 && userTypeStr != null) {
                val userType = UserType.create(userTypeStr)
                MyBloodDonationListScreen(
                    modifier = modifier,
                    viewModel = getViewModel(
                        parameters = { parametersOf(userId, userType) },
                    ),
                    navController = navController,
                )
            }
        }

        composable(NavItem.BloodDonationRequestCreate.route) {
            BloodDonationRequestCreateScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.BloodDonationRequestList.route) {
            BloodDonationRequestListScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.FacultyPicker.route
                .plus("?${NavParam.MODE}={${NavParam.MODE}}")
                .plus("&${NavParam.FACULTY_ID}={${NavParam.FACULTY_ID}}")
                .plus("&${NavParam.BATCH_ID}={${NavParam.BATCH_ID}}"),
            arguments = listOf(
                navArgument(NavParam.MODE) { type = NavType.IntType },
                navArgument(NavParam.FACULTY_ID) {
                    defaultValue = FacultyPickerViewModel.INVALID_ID
                    type = NavType.IntType
                },
                navArgument(NavParam.BATCH_ID) {
                    defaultValue = FacultyPickerViewModel.INVALID_ID
                    type = NavType.IntType
                },
            ),
        ) {
            val mode = it.arguments?.getInt(NavParam.MODE)?.let { modeInt ->
                FacultySelectionMode.create(modeInt)
            } ?: FacultySelectionMode.BOTH
            val facultyId = it.arguments?.getInt(NavParam.FACULTY_ID)
                ?: FacultyPickerViewModel.INVALID_ID
            val batchId = it.arguments?.getInt(NavParam.BATCH_ID)
                ?: FacultyPickerViewModel.INVALID_ID
            FacultyPickerScreen(
                modifier = modifier,
                viewModel = getViewModel(
                    parameters = { parametersOf(mode, facultyId, batchId) },
                ),
                navController = navController,
            )
        }

        composable(NavItem.MyDeviceList.route) {
            MyDeviceListScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.LocationPicker.route
                .plus("?${NavParam.IS_CHECK_IN_MODE}={${NavParam.IS_CHECK_IN_MODE}}"),
            arguments = listOf(
                navArgument(NavParam.IS_CHECK_IN_MODE) {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) {
            val isCheckInMode = it.arguments?.getBoolean(NavParam.IS_CHECK_IN_MODE)?.or(false)
            LocationPickerScreen(
                modifier = modifier,
                viewModel = getViewModel(parameters = { parametersOf(isCheckInMode) }),
                navController = navController,
            )
        }

        composable(NavItem.ChangePassword.route) {
            ChangePasswordScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(NavItem.DeleteAccount.route) {
            DeleteAccountScreen(
                modifier = modifier,
                viewModel = getViewModel(),
                navController = navController,
            )
        }

        composable(
            NavItem.CvUpload.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID)
            val userTypeStr = it.arguments?.getString(NavParam.USER_TYPE)
            if (userId != null && userId != -1 && userTypeStr != null) {
                val userType = UserType.create(userTypeStr)
                CvUploadScreen(
                    modifier = modifier,
                    viewModel = getViewModel(
                        parameters = { parametersOf(userId, userType) },
                    ),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.CvDownload.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}")
                .plus("&${NavParam.URL}={${NavParam.URL}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(NavParam.URL) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID)
            val userTypeStr = it.arguments?.getString(NavParam.USER_TYPE)
            val url = it.arguments?.getString(NavParam.URL)
            if (userId != null && userId != -1 && userTypeStr != null && url != null) {
                val userType = UserType.create(userTypeStr)
                CvDownloadScreen(
                    modifier = modifier,
                    viewModel = getViewModel(
                        parameters = { parametersOf(userId, userType, url) },
                    ),
                    navController = navController,
                )
            }
        }

        composable(
            NavItem.ImageUpload.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID)
            val userTypeStr = it.arguments?.getString(NavParam.USER_TYPE)
            if (userId != null && userId != -1 && userTypeStr != null) {
                val userType = UserType.create(userTypeStr)
                ImageUploadScreen(
                    modifier = modifier,
                    viewModel = getViewModel(
                        parameters = { parametersOf(userId, userType) },
                    ),
                    navController = navController,
                )
            }
        }
    }
}