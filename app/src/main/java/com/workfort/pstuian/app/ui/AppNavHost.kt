package com.workfort.pstuian.app.ui

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.workfort.pstuian.view.ui.changepassword.ChangePasswordScreen
import com.workfort.pstuian.view.ui.changepassword.ChangePasswordUiEvent
import com.workfort.pstuian.view.ui.common.facultypicker.FacultyPickerScreen
import com.workfort.pstuian.view.ui.common.locationpicker.LocationPickerScreen
import com.workfort.pstuian.view.ui.contactus.ContactUsScreen
import com.workfort.pstuian.view.ui.contactus.ContactUsScreenUiEvent
import com.workfort.pstuian.view.ui.checkinlist.CheckInListScreen
import com.workfort.pstuian.view.ui.checkinlist.CheckInListScreenUiEvent
import com.workfort.pstuian.app.ui.notification.NotificationScreen
import com.workfort.pstuian.app.ui.notification.NotificationViewModel
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenUiEvent
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenUiEvent
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenUiEvent
import com.workfort.pstuian.reducer.ui.mydevicelist.MyDeviceListScreenUiEvent
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.view.ui.blooddonationcreate.BloodDonationCreateScreen
import com.workfort.pstuian.view.ui.blooddonationcreate.BloodDonationCreateScreenUiEvent
import com.workfort.pstuian.view.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreen
import com.workfort.pstuian.view.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreenUiEvent
import com.workfort.pstuian.view.ui.blooddonationrequestlist.BloodDonationRequestListScreen
import com.workfort.pstuian.view.ui.blooddonationrequestlist.BloodDonationRequestListScreenUiEvent
import com.workfort.pstuian.view.ui.cvdownload.CvDownloadScreen
import com.workfort.pstuian.view.ui.cvupload.CvUploadScreen
import com.workfort.pstuian.view.ui.deleteaccount.DeleteAccountScreen
import com.workfort.pstuian.view.ui.deleteaccount.DeleteAccountUiEvent
import com.workfort.pstuian.view.ui.donate.DonateScreen
import com.workfort.pstuian.view.ui.donate.DonateUiEvent
import com.workfort.pstuian.view.ui.donors.DonorsScreen
import com.workfort.pstuian.view.ui.donors.DonorsScreenUiEvent
import com.workfort.pstuian.view.ui.emailverification.EmailVerificationScreen
import com.workfort.pstuian.view.ui.emailverification.EmailVerificationScreenUiEvent
import com.workfort.pstuian.view.ui.employeeprofile.EmployeeProfileScreen
import com.workfort.pstuian.view.ui.faculty.FacultyScreen
import com.workfort.pstuian.view.ui.forgotpassword.ForgotPasswordScreen
import com.workfort.pstuian.view.ui.forgotpassword.ForgotPasswordScreenUiEvent
import com.workfort.pstuian.view.ui.home.HomeScreen
import com.workfort.pstuian.view.ui.imagepreview.ImagePreviewScreen
import com.workfort.pstuian.view.ui.imageupload.ImageUploadScreen
import com.workfort.pstuian.view.ui.myblooddonationlist.MyBloodDonationListScreen
import com.workfort.pstuian.view.ui.myblooddonationlist.MyBloodDonationListScreenUiEvent
import com.workfort.pstuian.view.ui.mycheckinlist.MyCheckInListScreen
import com.workfort.pstuian.view.ui.mycheckinlist.MyCheckInListScreenUiEvent
import com.workfort.pstuian.view.ui.mydevicelist.MyDeviceListScreen
import com.workfort.pstuian.view.ui.settings.SettingsScreen
import com.workfort.pstuian.view.ui.settings.SettingsScreenUiEvent
import com.workfort.pstuian.view.ui.signin.SignInScreen
import com.workfort.pstuian.view.ui.signup.SignUpScreen
import com.workfort.pstuian.view.ui.splash.SplashScreen
import com.workfort.pstuian.view.ui.studentprofile.StudentProfileScreen
import com.workfort.pstuian.view.ui.studentprofileedit.StudentProfileEditScreen
import com.workfort.pstuian.view.ui.studentprofileedit.StudentProfileEditScreenUiEvent
import com.workfort.pstuian.view.ui.students.StudentsScreen
import com.workfort.pstuian.view.ui.teacherprofile.TeacherProfileScreen
import com.workfort.pstuian.view.ui.teacherprofileedit.TeacherProfileEditScreen
import com.workfort.pstuian.viewmodel.FacultyPickerViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavItem.Splash.route,
) {
    val linkUtil = LinkUtil()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {

        composable(NavItem.Splash.route) {
            SplashScreen(
                modifier = modifier,
                viewModel = koinViewModel(),
                onNavigateToHome = {
                    navController.navigate(NavItem.Home.route) {
                        popUpTo(NavItem.Splash.route) { inclusive = true }
                    }
                },
                onUpdateApp = {
                    linkUtil.openBrowser("https://play.google.com/store/apps/details?id=com.workfort.pstuian")
                }
            )
        }

        composable(NavItem.Home.route) {
            HomeScreen(
                modifier = modifier,
                viewModel = koinViewModel(),
                navigateToSignIn = { navController.navigate(NavItem.SignIn.route) },
                navigateToProfile = { userType, userId ->
                    val route = if (userType == UserType.STUDENT) {
                        NavItem.StudentProfile.route
                    } else {
                        NavItem.TeacherProfile.route
                    }
                    navController.navigate(route.plus("/$userId"))
                },
                navigateToNotification = { navController.navigate(NavItem.Notification.route) },
                navigateToFaculty = { faculty ->
                    navController.navigate(NavItem.Faculty.route.plus("/${faculty.id}"))
                },
                navigateToImagePreview = { url ->
                    navController.navigate(NavItem.ImagePreview.route.plus("/$url"))
                },
                navigateToContactUs = { navController.navigate(NavItem.ContactUs.route) },
                navigateToDonors = { navController.navigate(NavItem.Donors.route) },
                navigateToBloodDonationRequest = {
                    navController.navigate(NavItem.BloodDonationRequestList.route)
                },
                navigateToCheckIn = { navController.navigate(NavItem.CheckInList.route) },
                navigateToDonate = { navController.navigate(NavItem.Donate.route) },
                navigateToSettings = { navController.navigate(NavItem.Settings.route) },
                openBrowser = { url -> linkUtil.openBrowser(url) },
                openStore = {
                    linkUtil.openBrowser("https://play.google.com/store/apps/details?id=com.workfort.pstuian")
                },
                requestNotificationPermission = {}
            )
        }

        composable(NavItem.SignIn.route) {
            SignInScreen(
                modifier = modifier,
                viewModel = koinViewModel(),
                onNavigateToSignUp = { navController.navigate(NavItem.SignUp.route) },
                onNavigateToForgotPassword = { navController.navigate(NavItem.ForgotPassword.route) },
                onNavigateBack = { success ->
                    if (success) {
                        navController.navigate(NavItem.Home.route) {
                            popUpTo(NavItem.SignIn.route) { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onNavigateToEmailVerification = {
                    navController.navigate(NavItem.EmailVerification.route)
                },
                showToast = { /* Implementation of showToast */ }
            )
        }

        composable(NavItem.SignUp.route) {
            val facultyId = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Int>(NavParam.FACULTY_ID)
            val batchId = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Int>(NavParam.BATCH_ID)

            SignUpScreen(
                modifier = modifier,
                viewModel = koinViewModel(),
                facultyId = facultyId,
                batchId = batchId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFacultyPicker = { mode, _, _ ->
                    navController.navigate(
                        NavItem.FacultyPicker.route.plus("/${mode.name}")
                    )
                },
                onOpenUrl = { url -> linkUtil.openBrowser(url) }
            )
        }

        composable(
            NavItem.EmailVerification.route.plus("/{${NavParam.EMAIL}}"),
            arguments = listOf(
                navArgument(NavParam.EMAIL) { type = NavType.StringType },
            ),
        ) {
            val email = it.arguments?.getString(NavParam.EMAIL) ?: ""
            val viewModel: com.workfort.pstuian.viewmodel.emailverification.EmailVerificationViewModel =
                koinViewModel(parameters = { parametersOf(email) })
            val screenState by viewModel.screenState.collectAsState()
            EmailVerificationScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is EmailVerificationScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is EmailVerificationScreenUiEvent.OnClickUserTypeBtn ->
                            viewModel.onClickUserTypeBtn(uiEvent.userType)
                        is EmailVerificationScreenUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
                        is EmailVerificationScreenUiEvent.OnClickSendEmail ->
                            viewModel.sendVerificationEmail(uiEvent.email)
                        is EmailVerificationScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.ForgotPassword.route) {
            val viewModel: com.workfort.pstuian.viewmodel.forgotpassword.ForgotPasswordViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            ForgotPasswordScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is ForgotPasswordScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is ForgotPasswordScreenUiEvent.OnClickUserTypeBtn ->
                            viewModel.onClickUserTypeBtn(uiEvent.userType)
                        is ForgotPasswordScreenUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
                        is ForgotPasswordScreenUiEvent.OnClickSendResetLink ->
                            viewModel.sendPasswordResetLink(uiEvent.email)
                        is ForgotPasswordScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(
            NavItem.Faculty.route.plus("/{${NavParam.FACULTY_ID}}"),
            arguments = listOf(
                navArgument(NavParam.FACULTY_ID) { type = NavType.IntType },
            ),
        ) {
            val facultyId = it.arguments?.getInt(NavParam.FACULTY_ID) ?: 0
            FacultyScreen(
                modifier = modifier,
                viewModel = koinViewModel(parameters = { parametersOf(facultyId) }),
                navigateToTeacher = { teacher ->
                    navController.navigate(NavItem.TeacherProfile.route.plus("/${teacher.id}"))
                },
                navigateToEmployee = { employee ->
                    navController.navigate(NavItem.EmployeeProfile.route.plus("/${employee.id}"))
                },
                navigateToStudents = { batch ->
                    navController.navigate(NavItem.Students.route.plus("/${batch.facultyId}/${batch.id}"))
                },
                onBack = { navController.popBackStack() },
                onCall = { phoneNumber -> linkUtil.callTo(phoneNumber) }
            )
        }

        composable(
            NavItem.TeacherProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID) ?: 0
            TeacherProfileScreen(
                modifier = modifier,
                viewModel = koinViewModel(parameters = { parametersOf(userId) }),
                onEdit = { _, _ ->
                    navController.navigate(NavItem.TeacherProfileEdit.route)
                },
                onBack = { navController.popBackStack() },
                onCall = { phoneNumber -> linkUtil.callTo(phoneNumber) },
                onEmail = { email -> linkUtil.sendEmail(email) },
                onBrowser = { url -> linkUtil.openBrowser(url) },
                onImagePreview = { url ->
                    navController.navigate(NavItem.ImagePreview.route.plus("/$url"))
                },
                onChangeImage = { _, _ ->
                    navController.navigate(NavItem.ImageUpload.route)
                },
                onChangePassword = {
                    navController.navigate(NavItem.ChangePassword.route)
                },
                onMyDeviceList = {
                    navController.navigate(NavItem.MyDeviceList.route)
                },
                onDeleteAccount = {
                    navController.navigate(NavItem.DeleteAccount.route)
                }
            )
        }

        composable(
            NavItem.StudentProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID) ?: 0
            StudentProfileScreen(
                modifier = modifier,
                viewModel = koinViewModel(parameters = { parametersOf(userId) }),
                navigateToStudentProfileEdit = { _, _ ->
                    navController.navigate(NavItem.StudentProfileEdit.route)
                },
                navigateToDownloadCv = { _, _, url ->
                    navController.navigate(NavItem.CvDownload.route.plus("?${NavParam.URL}=$url"))
                },
                navigateBack = { navController.popBackStack() },
                callTo = { phoneNumber -> linkUtil.callTo(phoneNumber) },
                sendEmail = { email -> linkUtil.sendEmail(email) },
                openBrowser = { url -> linkUtil.openBrowser(url) },
                navigateToImagePreview = { url ->
                    navController.navigate(NavItem.ImagePreview.route.plus("/$url"))
                },
                navigateToImageUpload = { _, _ ->
                    navController.navigate(NavItem.ImageUpload.route)
                },
                navigateToChangePassword = {
                    navController.navigate(NavItem.ChangePassword.route)
                },
                navigateToUploadCv = { _, _ ->
                    navController.navigate(NavItem.CvUpload.route)
                },
                navigateToMyBloodDonationList = { _, _ ->
                    navController.navigate(NavItem.MyBloodDonationList.route)
                },
                navigateToMyCheckInList = { _, _ ->
                    navController.navigate(NavItem.MyCheckInList.route)
                },
                navigateToMyDeviceList = {
                    navController.navigate(NavItem.MyDeviceList.route)
                },
                navigateToDeleteAccount = {
                    navController.navigate(NavItem.DeleteAccount.route)
                }
            )
        }

        composable(
            NavItem.EmployeeProfile.route.plus("/{${NavParam.USER_ID}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) { type = NavType.IntType },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID) ?: 0
            EmployeeProfileScreen(
                modifier = modifier,
                viewModel = koinViewModel(parameters = { parametersOf(userId) }),
                onBack = { navController.popBackStack() },
                onCall = { phoneNumber -> linkUtil.callTo(phoneNumber) },
                onImagePreview = { url ->
                    navController.navigate(NavItem.ImagePreview.route.plus("/$url"))
                }
            )
        }

        composable(
            NavItem.Students.route.plus("/{${NavParam.FACULTY_ID}}/{${NavParam.BATCH_ID}}"),
            arguments = listOf(
                navArgument(NavParam.FACULTY_ID) { type = NavType.IntType },
                navArgument(NavParam.BATCH_ID) { type = NavType.IntType },
            ),
        ) {
            val facultyId = it.arguments?.getInt(NavParam.FACULTY_ID) ?: 0
            val batchId = it.arguments?.getInt(NavParam.BATCH_ID) ?: 0
            StudentsScreen(
                modifier = modifier,
                viewModel = koinViewModel(parameters = { parametersOf(facultyId, batchId) }),
                navigateToStudentProfile = { userId ->
                    navController.navigate(NavItem.StudentProfile.route.plus("/$userId"))
                },
                navigateBack = { navController.popBackStack() },
                callTo = { phoneNumber -> linkUtil.callTo(phoneNumber) }
            )
        }

        composable(NavItem.StudentProfileEdit.route) {
            val viewModel: com.workfort.pstuian.viewmodel.studentprofileedit.StudentProfileEditViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            StudentProfileEditScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is StudentProfileEditScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
                        is StudentProfileEditScreenUiEvent.OnChangeProfile ->
                            viewModel.onChangeProfile(uiEvent.profile)
                        is StudentProfileEditScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is StudentProfileEditScreenUiEvent.OnClickSave -> viewModel.onClickSave()
                        is StudentProfileEditScreenUiEvent.OnClickFaculty -> viewModel.onClickFaculty()
                        is StudentProfileEditScreenUiEvent.OnClickBatch -> viewModel.onClickBatch()
                        is StudentProfileEditScreenUiEvent.OnChangeFaculty ->
                            viewModel.onChangeFaculty(uiEvent.facultyId)
                        is StudentProfileEditScreenUiEvent.OnChangeBatch ->
                            viewModel.onChangeBatch(uiEvent.batchId)
                        is StudentProfileEditScreenUiEvent.OnSave -> viewModel.updateProfile()
                        is StudentProfileEditScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is StudentProfileEditScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.TeacherProfileEdit.route) {
            val viewModel: com.workfort.pstuian.viewmodel.teacherprofileedit.TeacherProfileEditViewModel = koinViewModel()
            TeacherProfileEditScreen(
                modifier = modifier,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFacultyPicker = { mode, _ ->
                    navController.navigate(
                        NavItem.FacultyPicker.route.plus("/${mode}")
                    )
                }
            )
        }

        composable(NavItem.ContactUs.route) {
            val viewModel: com.workfort.pstuian.viewmodel.contactus.ContactUsViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            ContactUsScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is ContactUsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is ContactUsScreenUiEvent.OnClickSend -> viewModel.sendInquiry()
                        is ContactUsScreenUiEvent.OnChangeContactUsInput -> viewModel.onChangeContactUsInput(uiEvent.input)
                        is ContactUsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.Donors.route) {
            val viewModel: com.workfort.pstuian.viewmodel.donors.DonorsViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            DonorsScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is DonorsScreenUiEvent.OnLoadData -> viewModel.loadDonors()
                        is DonorsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is DonorsScreenUiEvent.OnClickItem -> viewModel.onClickItem(uiEvent.item)
                        is DonorsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is DonorsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.CheckInList.route) {
            val viewModel: com.workfort.pstuian.viewmodel.checkinlist.CheckInListViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            CheckInListScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is CheckInListScreenUiEvent.OnLoadCheckInListList -> viewModel.loadCheckInList(uiEvent.isRefresh)
                        is CheckInListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is CheckInListScreenUiEvent.OnClickItem -> viewModel.onClickItem(uiEvent.item)
                        is CheckInListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                },
            )
        }

        composable(
            NavItem.MyCheckInList.route
                .plus("?${NavParam.USER_ID}={${NavParam.USER_ID}}")
                .plus("&${NavParam.USER_TYPE}={${NavParam.USER_TYPE}}"),
            arguments = listOf(
                navArgument(NavParam.USER_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(NavParam.USER_TYPE) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            val userId = it.arguments?.getInt(NavParam.USER_ID) ?: 0
            val userType = it.arguments?.getString(NavParam.USER_TYPE) ?: ""
            val viewModel: com.workfort.pstuian.viewmodel.mycheckinlist.MyCheckInListViewModel =
                koinViewModel(parameters = { parametersOf(userId, userType) })
            val screenState by viewModel.screenState.collectAsState()
            MyCheckInListScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is MyCheckInListScreenUiEvent.OnLoadMoreData -> viewModel.loadCheckInList(uiEvent.refresh)
                        is MyCheckInListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is MyCheckInListScreenUiEvent.OnClickItem -> viewModel.onClickItem(uiEvent.item)
                        is MyCheckInListScreenUiEvent.OnClickChangePrivacy ->
                            viewModel.onClickChangePrivacy(uiEvent.item, uiEvent.privacy)
                        is MyCheckInListScreenUiEvent.OnClickDelete -> viewModel.onClickDelete(uiEvent.item)
                        is MyCheckInListScreenUiEvent.OnChangePrivacy -> viewModel.changePrivacy(uiEvent.item, uiEvent.privacy)
                        is MyCheckInListScreenUiEvent.OnDelete -> viewModel.delete(uiEvent.item)
                        is MyCheckInListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is MyCheckInListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                },
            )
        }

        composable(NavItem.BloodDonationCreate.route) {
            val viewModel: com.workfort.pstuian.viewmodel.blooddonationcreate.BloodDonationCreateViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            BloodDonationCreateScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is BloodDonationCreateScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is BloodDonationCreateScreenUiEvent.OnClickSelectDate -> viewModel.onClickSelectDate()
                        is BloodDonationCreateScreenUiEvent.OnClickSend -> viewModel.sendRequest()
                        is BloodDonationCreateScreenUiEvent.OnChangeInput -> viewModel.onChangeInput(uiEvent.input)
                        is BloodDonationCreateScreenUiEvent.OnSelectDate -> viewModel.onSelectDate(uiEvent.dateMills)
                        is BloodDonationCreateScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.BloodDonationRequestCreate.route) {
            val viewModel: com.workfort.pstuian.viewmodel.blooddonationrequestcreate.BloodDonationRequestCreateViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            BloodDonationRequestCreateScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is BloodDonationRequestCreateScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is BloodDonationRequestCreateScreenUiEvent.OnClickSelectDate -> viewModel.onClickSelectDate()
                        is BloodDonationRequestCreateScreenUiEvent.OnClickSend -> viewModel.sendRequest()
                        is BloodDonationRequestCreateScreenUiEvent.OnChangeInput -> viewModel.onChangeInput(uiEvent.input)
                        is BloodDonationRequestCreateScreenUiEvent.OnSelectDate -> viewModel.onSelectDate(uiEvent.dateMills)
                        is BloodDonationRequestCreateScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.BloodDonationRequestList.route) {
            val viewModel: com.workfort.pstuian.viewmodel.blooddonationrequestlist.BloodDonationRequestListViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            BloodDonationRequestListScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is BloodDonationRequestListScreenUiEvent.OnLoadMoreData -> viewModel.loadDonationRequests(uiEvent.refresh)
                        is BloodDonationRequestListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is BloodDonationRequestListScreenUiEvent.OnClickItem -> viewModel.onClickItem(uiEvent.item)
                        is BloodDonationRequestListScreenUiEvent.OnClickCall -> linkUtil.callTo(uiEvent.phoneNumber)
                        is BloodDonationRequestListScreenUiEvent.OnClickCreateRequest ->
                            navController.navigate(NavItem.BloodDonationRequestCreate.route)
                        is BloodDonationRequestListScreenUiEvent.OnCall -> linkUtil.callTo(uiEvent.phoneNumber)
                        is BloodDonationRequestListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.MyBloodDonationList.route) {
            val viewModel: com.workfort.pstuian.viewmodel.myblooddonationlist.MyBloodDonationListViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            MyBloodDonationListScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is MyBloodDonationListScreenUiEvent.OnLoadMoreData -> viewModel.loadDonationList(uiEvent.refresh)
                        is MyBloodDonationListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is MyBloodDonationListScreenUiEvent.OnClickEdit -> viewModel.onClickEdit(uiEvent.item)
                        is MyBloodDonationListScreenUiEvent.OnClickDelete -> viewModel.onClickDelete(uiEvent.item)
                        is MyBloodDonationListScreenUiEvent.OnDelete -> viewModel.deleteDonation(uiEvent.item)
                        is MyBloodDonationListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.Settings.route) {
            val viewModel: com.workfort.pstuian.viewmodel.settings.SettingsViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            SettingsScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is SettingsScreenUiEvent.LoadInitialData -> viewModel.loadInitial()
                        is SettingsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is SettingsScreenUiEvent.OnClickContactUs ->
                            navController.navigate(NavItem.ContactUs.route)
                        is SettingsScreenUiEvent.OnChangeShowNotification -> viewModel.setShowNotification(uiEvent.show)
                        is SettingsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is SettingsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.Donate.route) {
            val viewModel: com.workfort.pstuian.viewmodel.donate.DonateViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            DonateScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is DonateUiEvent.OnClickBack -> viewModel.onClickBack()
                        is DonateUiEvent.OnClickSend -> viewModel.sendDonationInfo()
                        is DonateUiEvent.OnChangeInput -> viewModel.onChangeInput(uiEvent.input)
                        is DonateUiEvent.MessageConsumed -> viewModel.messageConsumed()
                    }
                }
            )
        }

        composable(
            NavItem.ImagePreview.route.plus("/{${NavParam.URL}}"),
            arguments = listOf(
                navArgument(NavParam.URL) { type = NavType.StringType },
            ),
        ) {
            val url = it.arguments?.getString(NavParam.URL) ?: ""
            ImagePreviewScreen(
                modifier = modifier,
                imageUrl = url,
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavItem.ImageUpload.route) {
            val viewModel: com.workfort.pstuian.viewmodel.imageupload.ImageUploadViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current
            ImageUploadScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is ImageUploadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is ImageUploadScreenUiEvent.OnSelectImage -> viewModel.onSelectImage(uiEvent.uri)
                        is ImageUploadScreenUiEvent.OnClickUpload -> {
                            val uri = android.net.Uri.parse(screenState.displayState.selectedFileUri ?: "")
                            val request = com.workfort.pstuian.util.helper.ToWorkManagerRequest.uploadImage(context, uri)
                            androidx.work.WorkManager.getInstance(context).enqueue(request)
                        }
                        is ImageUploadScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is ImageUploadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.CvUpload.route) {
            val viewModel: com.workfort.pstuian.viewmodel.cvupload.CvUploadViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current
            CvUploadScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is CvUploadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is CvUploadScreenUiEvent.OnSelectCv -> viewModel.onSelectCv(uiEvent.uri, "")
                        is CvUploadScreenUiEvent.OnClickUpload -> {
                            val selectedFileUri = screenState.displayState.selectedFileUri
                            if (selectedFileUri != null) {
                                val uri = android.net.Uri.parse(selectedFileUri)
                                val request = com.workfort.pstuian.util.helper.ToWorkManagerRequest.uploadPdf(context, uri)
                                androidx.work.WorkManager.getInstance(context).enqueue(request)
                            }
                        }
                        is CvUploadScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is CvUploadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(
            NavItem.CvDownload.route.plus("?${NavParam.URL}={${NavParam.URL}}"),
            arguments = listOf(
                navArgument(NavParam.URL) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            val url = it.arguments?.getString(NavParam.URL) ?: ""
            val viewModel: com.workfort.pstuian.viewmodel.cvdownload.CvDownloadViewModel =
                koinViewModel(parameters = { parametersOf(url) })
            val screenState by viewModel.screenState.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current
            CvDownloadScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is CvDownloadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is CvDownloadScreenUiEvent.OnDownload -> {
                            val selectedFileUri = uiEvent.uri
                            val uri = android.net.Uri.parse(selectedFileUri)
                            val request = com.workfort.pstuian.util.helper.ToWorkManagerRequest.downloadPdf(context, url, uri)
                            androidx.work.WorkManager.getInstance(context).enqueue(request)
                        }
                        is CvDownloadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.MyDeviceList.route) {
            val viewModel: com.workfort.pstuian.viewmodel.mydevicelist.MyDeviceListViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            MyDeviceListScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is MyDeviceListScreenUiEvent.OnLoadMoreData -> viewModel.loadDeviceList(uiEvent.refresh)
                        is MyDeviceListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
                        is MyDeviceListScreenUiEvent.OnClickItem -> viewModel.onClickItem(uiEvent.item)
                        is MyDeviceListScreenUiEvent.OnClickDelete -> viewModel.onClickDelete(uiEvent.item)
                        is MyDeviceListScreenUiEvent.OnClickSignOutFromAll -> viewModel.onClickSignOutFromAllDevice()
                        is MyDeviceListScreenUiEvent.OnSignOutFromAll -> viewModel.signOutFromAllDevices()
                        is MyDeviceListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        is MyDeviceListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                        else -> Unit
                    }
                }
            )
        }

        composable(NavItem.Notification.route) {
            val viewModel: NotificationViewModel = koinViewModel()
            NotificationScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            NavItem.FacultyPicker.route.plus("/{${NavParam.MODE}}"),
            arguments = listOf(
                navArgument(NavParam.MODE) { type = NavType.StringType },
            ),
        ) {
            val modeString = it.arguments?.getString(NavParam.MODE)
            val mode = FacultySelectionMode.valueOf(modeString ?: FacultySelectionMode.NONE.name)
            val viewModel: FacultyPickerViewModel = koinViewModel(parameters = { parametersOf(mode) })
            FacultyPickerScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
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
            val viewModel: com.workfort.pstuian.viewmodel.LocationPickerViewModel = koinViewModel(parameters = { parametersOf(isCheckInMode) })
            LocationPickerScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
            )
        }

        composable(NavItem.ChangePassword.route) {
            val viewModel: com.workfort.pstuian.viewmodel.changepassword.ChangePasswordViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            ChangePasswordScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is ChangePasswordUiEvent.OnClickBack -> viewModel.onClickBack()
                        is ChangePasswordUiEvent.OnClickSaveBtn -> viewModel.changePassword()
                        is ChangePasswordUiEvent.OnChangeInput -> viewModel.onChangeInput(uiEvent.input)
                        is ChangePasswordUiEvent.MessageConsumed -> viewModel.messageConsumed()
                    }
                }
            )
        }

        composable(NavItem.DeleteAccount.route) {
            val viewModel: com.workfort.pstuian.viewmodel.deleteaccount.DeleteAccountViewModel = koinViewModel()
            val screenState by viewModel.screenState.collectAsState()
            DeleteAccountScreen(
                modifier = modifier,
                screenState = screenState,
                onUiEvent = { uiEvent ->
                    when (uiEvent) {
                        is DeleteAccountUiEvent.OnClickBack -> viewModel.onClickBack()
                        is DeleteAccountUiEvent.OnClickDeleteAccountBtn -> viewModel.deleteAccount()
                        is DeleteAccountUiEvent.OnChangeInput -> viewModel.onChangeInput(uiEvent.input)
                        is DeleteAccountUiEvent.MessageConsumed -> viewModel.messageConsumed()
                        else -> Unit
                    }
                }
            )
        }
    }
}
