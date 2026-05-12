package com.workfort.pstuian.ui.faculty.course.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.shimmerAnimation
import com.workfort.pstuian.ui.faculty.composable.CourseListItemView
import com.workfort.pstuian.ui.faculty.course.state.CourseUiEvent
import com.workfort.pstuian.ui.faculty.course.state.CourseUiState

@Composable
internal fun CourseContentPanel(
    uiState: CourseUiState.Content,
    onUiEvent: (CourseUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.courses.isEmpty() && uiState.isLoading) {
            CourseListShimmer()
            return
        }

        if (uiState.courses.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedEmptyView()
            }
        } else {
            CourseListView(
                courses = uiState.courses,
                isContentLoading = uiState.isLoading,
                onUiEvent = onUiEvent,
            )
        }
    }
}

@Composable
private fun CourseListView(
    courses: List<Course>,
    isContentLoading: Boolean,
    onUiEvent: (CourseUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()
    var lastLoadMoreRequestedAtSize by remember { mutableIntStateOf(-1) }
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - 1
        }
    }

    LaunchedEffect(shouldLoadMore, isContentLoading, courses.size) {
        val canRequestMore = shouldLoadMore && !isContentLoading && courses.isNotEmpty()
        if (canRequestMore && lastLoadMoreRequestedAtSize != courses.size) {
            lastLoadMoreRequestedAtSize = courses.size
            onUiEvent(CourseUiEvent.LoadMore)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(courses) { course ->
            CourseListItemView(course) { onUiEvent(CourseUiEvent.CourseClicked(course)) }
        }
        if (isContentLoading) {
            item { CourseListItemShimmer() }
        }
    }
}

@Composable
internal fun CourseListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            CourseListItemShimmer()
        }
    }
}

@Composable
private fun CourseListItemShimmer() {
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
