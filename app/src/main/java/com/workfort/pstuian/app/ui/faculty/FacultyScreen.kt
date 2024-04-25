package com.workfort.pstuian.app.ui.faculty

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ScrollableTabView
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.launch

@Composable
fun FacultyScreen(
    modifier: Modifier = Modifier,
    viewModel: FacultyViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<FacultyScreenUiEvent>(FacultyScreenUiEvent.None)
    }

    val linkUtil = LinkUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        uiEvent = FacultyScreenUiEvent.LoadInitialData
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is FacultyScreenUiEvent.None -> Unit
            is FacultyScreenUiEvent.LoadInitialData -> viewModel.loadInitialData()
            is FacultyScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is FacultyScreenUiEvent.OnClickBatch -> viewModel.onClickBatch(batch)
            is FacultyScreenUiEvent.OnClickTeacher -> viewModel.onClickTeacher(teacher)
            is FacultyScreenUiEvent.OnClickCourse -> Unit
            is FacultyScreenUiEvent.OnClickEmployee -> viewModel.onClickEmployee(employee)
            is FacultyScreenUiEvent.OnClickCall -> viewModel.onClickCall(phoneNumber)
            is FacultyScreenUiEvent.OnCall -> linkUtil.callTo(phoneNumber)
            is FacultyScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is FacultyScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = FacultyScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FacultyScreenContent(
    modifier: Modifier,
    displayState: FacultyScreenState.DisplayState,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val tabs = listOf(
        context.getString(R.string.label_batch),
        context.getString(R.string.label_teacher),
        context.getString(R.string.label_course_schedule),
        context.getString(R.string.label_employee),
    )
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = displayState.title,
                elevation = 0.dp,
                onClickBack = {
                    onUiEvent(FacultyScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            ScrollableTabView(
                modifier = modifier,
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
            ) { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> displayState.batchListState.Handle(modifier, onUiEvent)
                    1 -> displayState.teacherListState.Handle(modifier, onUiEvent)
                    2 -> displayState.courseListState.Handle(modifier, onUiEvent)
                    3 -> displayState.employeeListState.Handle(modifier, onUiEvent)
                }
            }
        }
    }
}

@Composable
private fun List<BatchEntity>.BatchListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickBatch: (batch: BatchEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@BatchListView) { batch ->
            BatchListItemView(modifier, batch) {
                onClickBatch(batch)
            }
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun List<TeacherEntity>.TeacherListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickTeacher: (TeacherEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@TeacherListView) { teacher ->
            TeacherListItemView(
                modifier = modifier,
                teacher = teacher,
                onClickTeacher = { onClickTeacher(teacher) },
                onClickCall = { teacher.phone?.let(onClickCall) },
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun List<CourseEntity>.CourseListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickCourse: (course: CourseEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@CourseListView) { course ->
            CourseListItemView(modifier, course) {
                onClickCourse(course)
            }
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun List<EmployeeEntity>.EmployeeListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickEmployee: (EmployeeEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@EmployeeListView) { employee ->
            EmployeeListItemView(
                modifier = modifier,
                employee = employee,
                onClickEmployee = { onClickEmployee(employee) },
                onClickCall = { employee.phone?.let(onClickCall) },
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun FacultyScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    FacultyScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun FacultyScreenState.DisplayState.BatchListState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    when (this@Handle) {
        is FacultyScreenState.DisplayState.BatchListState.None -> Unit
        is FacultyScreenState.DisplayState.BatchListState.Available -> {
            if (batches.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                batches.BatchListView(modifier = modifier, isLoading = isLoading) {
                    onUiEvent(FacultyScreenUiEvent.OnClickBatch(it))
                }
            }
        }
        is FacultyScreenState.DisplayState.BatchListState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun FacultyScreenState.DisplayState.TeacherListState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    when (this@Handle) {
        is FacultyScreenState.DisplayState.TeacherListState.None -> Unit
        is FacultyScreenState.DisplayState.TeacherListState.Available -> {
            if (teachers.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                teachers.TeacherListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onClickTeacher = {
                        onUiEvent(FacultyScreenUiEvent.OnClickTeacher(it))
                    },
                    onClickCall = {
                        onUiEvent(FacultyScreenUiEvent.OnClickCall(it))
                    },
                )
            }
        }
        is FacultyScreenState.DisplayState.TeacherListState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun FacultyScreenState.DisplayState.CourseListState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    when (this@Handle) {
        is FacultyScreenState.DisplayState.CourseListState.None -> Unit
        is FacultyScreenState.DisplayState.CourseListState.Available -> {
            if (courses.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                courses.CourseListView(modifier = modifier, isLoading = isLoading) {
                    onUiEvent(FacultyScreenUiEvent.OnClickCourse(it))
                }
            }
        }
        is FacultyScreenState.DisplayState.CourseListState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun FacultyScreenState.DisplayState.EmployeeListState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    when (this@Handle) {
        is FacultyScreenState.DisplayState.EmployeeListState.None -> Unit
        is FacultyScreenState.DisplayState.EmployeeListState.Available -> {
            if (employees.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                }
            } else {
                employees.EmployeeListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onClickEmployee = {
                        onUiEvent(FacultyScreenUiEvent.OnClickEmployee(it))
                    },
                    onClickCall = {
                        onUiEvent(FacultyScreenUiEvent.OnClickCall(it))
                    },
                )
            }
        }
        is FacultyScreenState.DisplayState.EmployeeListState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun FacultyScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is FacultyScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
                onConfirm = {
                    onUiEvent(FacultyScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(FacultyScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun FacultyScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (FacultyScreenUiEvent) -> Unit,
) {
    when (this) {
        is FacultyScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is FacultyScreenState.NavigationState.GoToStudentsScreen -> {
            navController.navigate(
                NavItem.StudentList.route.plus("/${batch.id}"),
            )
        }
        is FacultyScreenState.NavigationState.GoToTeacherScreen -> {
            navController.navigate(
                NavItem.TeacherProfile.route.plus("/${teacher.id}"),
            )
        }
        is FacultyScreenState.NavigationState.GoToEmployeeScreen -> {
            navController.navigate(
                NavItem.EmployeeProfile.route.plus("/${employee.id}"),
            )
        }
    }
    onUiEvent(FacultyScreenUiEvent.NavigationConsumed)
}