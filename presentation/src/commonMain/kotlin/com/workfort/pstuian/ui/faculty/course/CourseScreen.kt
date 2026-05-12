package com.workfort.pstuian.ui.faculty.course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.faculty.course.composable.CourseScreenContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CourseScreen(
    facultyId: Int,
    viewModel: CourseViewModel = koinViewModel { parametersOf(facultyId) },
) {
    val uiState by viewModel.uiState.collectAsState()

    CourseScreenContent(uiState, viewModel::onUiEvent)
}
