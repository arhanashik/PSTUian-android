package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.faculty.batch.BatchScreen
import com.workfort.pstuian.ui.faculty.course.CourseScreen
import com.workfort.pstuian.ui.faculty.employee.EmployeeScreen
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import com.workfort.pstuian.ui.faculty.teacher.TeacherScreen
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
                0 -> BatchScreen(facultyId = uiState.facultyId)
                1 -> TeacherScreen(facultyId = uiState.facultyId)
                2 -> CourseScreen(facultyId = uiState.facultyId)
                3 -> EmployeeScreen(facultyId = uiState.facultyId)
            }
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
    }
}
