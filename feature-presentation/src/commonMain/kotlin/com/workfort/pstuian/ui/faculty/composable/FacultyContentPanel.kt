package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FacultyContentPanel(
    uiState: FacultyUiState.Content,
    onUiEvent: (FacultyUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { uiState.tabs.size })

    LaunchedEffect(uiState.selectedTab) {
        if (pagerState.currentPage != uiState.selectedTab) {
            pagerState.scrollToPage(uiState.selectedTab)
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        if (uiState.selectedTab != pagerState.settledPage) {
            onUiEvent(FacultyUiEvent.SelectTab(pagerState.settledPage))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ToggleSwitch(
            options = uiState.tabs,
            selectedIndex = uiState.selectedTab,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            onSelectedIndexChange = { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
                onUiEvent(FacultyUiEvent.SelectTab(index))
            }
        )
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
    onClickBatch: (Batch) -> Unit,
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
                BatchListShimmer()
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
                TeacherListShimmer()
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
    onClickCourse: (course: Course) -> Unit,
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
                CourseListShimmer()
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
                EmployeeListShimmer()
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
private fun List<Batch>.BatchListView(
    isLoading: Boolean,
    onClickBatch: (batch: Batch) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
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
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
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
private fun List<Course>.CourseListView(
    isLoading: Boolean,
    onClickCourse: (course: Course) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
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
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
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

@Composable
internal fun FacultyScreenShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerAnimation(),
        )
        BatchListShimmer()
    }
}

@Composable
private fun BatchListShimmer() = FacultyListShimmer { FacultyBatchCardShimmer() }

@Composable
private fun TeacherListShimmer() = FacultyListShimmer { FacultyTeacherEmployeeCardShimmer() }

@Composable
private fun CourseListShimmer() = FacultyListShimmer { FacultyCourseCardShimmer() }

@Composable
private fun EmployeeListShimmer() = FacultyListShimmer { FacultyTeacherEmployeeCardShimmer() }

@Composable
private fun FacultyListShimmer(
    item: @Composable () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(5) { item() }
    }
}

@Composable
private fun FacultyBatchCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.56f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .shimmerAnimation(),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
        }
    }
}

@Composable
private fun FacultyTeacherEmployeeCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(15.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmerAnimation(),
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .shimmerAnimation(),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerAnimation(),
            )
        }
    }
}

@Composable
private fun FacultyCourseCardShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
                Box(
                    modifier = Modifier
                        .width(170.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerAnimation(),
                )
            }
            Box(
                modifier = Modifier
                    .width(78.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .shimmerAnimation(),
            )
        }
    }
}
