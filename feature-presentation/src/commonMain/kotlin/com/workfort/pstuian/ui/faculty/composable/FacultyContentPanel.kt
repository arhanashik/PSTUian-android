package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ScrollableTabView
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FacultyContentPanel(
    uiState: FacultyUiState,
    onUiEvent: (FacultyUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { uiState.tabs.size })

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabView(
            tabs = uiState.tabs,
            selectedTabIndex = uiState.selectedTab,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
            onUiEvent(FacultyUiEvent.SelectTab(index))
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> uiState.batchListState.Handle {
                    onUiEvent(FacultyUiEvent.BatchClicked(it))
                }
                1 -> uiState.teacherListState.Handle(
                    onClickTeacher = { onUiEvent(FacultyUiEvent.TeacherClicked(it)) },
                    onClickCall = { onUiEvent(FacultyUiEvent.CallClicked(it)) },
                )
                2 -> uiState.courseListState.Handle {
                    onUiEvent(FacultyUiEvent.CourseClicked(it))
                }
                3 -> uiState.employeeListState.Handle(
                    onClickEmployee = { onUiEvent(FacultyUiEvent.EmployeeClicked(it)) },
                    onClickCall = { onUiEvent(FacultyUiEvent.CallClicked(it)) },
                )
            }
        }
    }
}

@Composable
private fun FacultyUiState.BatchListState.Handle(
    onClickBatch: (BatchEntity) -> Unit,
) {
    if (error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    if (batches.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
        batches.BatchListView(isLoading, onClickBatch)
    }
}

@Composable
private fun FacultyUiState.TeacherListState.Handle(
    onClickTeacher: (User.Teacher) -> Unit,
    onClickCall: (String) -> Unit,
) {
    if (error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    if (teachers.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
            isLoading = isLoading,
            onClickTeacher = onClickTeacher,
            onClickCall = onClickCall,
        )
    }
}

@Composable
private fun FacultyUiState.CourseListState.Handle(
    onClickCourse: (course: CourseEntity) -> Unit,
) {
    if (error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    if (courses.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
        courses.CourseListView(isLoading, onClickCourse)
    }
}

@Composable
private fun FacultyUiState.EmployeeListState.Handle(
    onClickEmployee: (User.Employee) -> Unit,
    onClickCall: (String) -> Unit,
) {
    if (error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedErrorView()
        }
        return
    }

    if (employees.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
        employees.EmployeeListView(
            isLoading = isLoading,
            onClickEmployee = onClickEmployee,
            onClickCall = onClickCall,
        )
    }
}

@Composable
private fun List<BatchEntity>.BatchListView(
    isLoading: Boolean,
    onClickBatch: (batch: BatchEntity) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@BatchListView) { batch ->
            BatchListItemView(batch) { onClickBatch(batch) }
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun List<User.Teacher>.TeacherListView(
    isLoading: Boolean,
    onClickTeacher: (User.Teacher) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@TeacherListView) { teacher ->
            TeacherListItemView(
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
    isLoading: Boolean,
    onClickCourse: (course: CourseEntity) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@CourseListView) { course ->
            CourseListItemView(course) { onClickCourse(course) }
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun List<User.Employee>.EmployeeListView(
    isLoading: Boolean,
    onClickEmployee: (User.Employee) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@EmployeeListView) { employee ->
            EmployeeListItemView(
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
