package com.workfort.pstuian.ui.faculty.batch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.faculty.batch.composable.BatchScreenContent
import com.workfort.pstuian.ui.faculty.batch.state.BatchNavigationState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BatchScreen(
    facultyId: Int,
    viewModel: BatchViewModel = koinViewModel { parametersOf(facultyId) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    BatchScreenContent(uiState, viewModel::onUiEvent)

    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleNavigationState(
    navigation: BatchNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is BatchNavigationState.GoToStudents -> {
                    navigator?.navigateTo(AppScreen.Students(it.batchId))
                }
            }
            onNavigationHandled()
        }
    }
}
