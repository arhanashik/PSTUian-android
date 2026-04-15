package com.workfort.pstuian.ui.facultypicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.featuredomain.appconstant.Const
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppBarIconButton
import com.workfort.pstuian.common.composable.FacultyView
import com.workfort.pstuian.ui.faculty.composable.BatchListItemSimpleView
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerNavigationState
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerUiEvent
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_change
import pstuian.feature_presentation.generated.resources.txt_change_faculty
import pstuian.feature_presentation.generated.resources.txt_select_batch
import pstuian.feature_presentation.generated.resources.txt_select_faculty

@Composable
fun FacultyPickerScreen(
    modifier: Modifier = Modifier,
    viewModel: FacultyPickerViewModel,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    FacultyPickerScreenContent(
        modifier = modifier,
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is FacultyPickerUiEvent.OnLoadData -> viewModel.loadInitialData()
                is FacultyPickerUiEvent.OnClickBack -> viewModel.onClickBack()
                is FacultyPickerUiEvent.OnClickFaculty -> viewModel.onClickFaculty(event.faculty)
                is FacultyPickerUiEvent.OnClickBatch -> viewModel.onClickBatch(event.batch)
                is FacultyPickerUiEvent.OnClickChangeFaculty -> viewModel.onClickChangeFaculty()
                is FacultyPickerUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        },
    )

    uiState.navigationState?.let { navigationState ->
        when (navigationState) {
            is FacultyPickerNavigationState.GoBack -> {
                navigationState.selectedFacultyId?.let { facultyId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(Const.Key.FACULTY, facultyId)
                }
                navigationState.selectedBatchId?.let { batchId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(Const.Key.BATCH, batchId)
                }
                navController.popBackStack()
            }
        }
        viewModel.navigationConsumed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FacultyPickerScreenContent(
    modifier: Modifier,
    uiState: FacultyPickerUiState,
    onUiEvent: (FacultyPickerUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (uiState.panelState) {
        is FacultyPickerUiState.PanelState.SelectFaculty ->
            stringResource(Res.string.txt_select_faculty)
        is FacultyPickerUiState.PanelState.SelectBatch ->
            stringResource(Res.string.txt_select_batch)
        else -> ""
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = title,
                navigation = { onUiEvent(FacultyPickerUiEvent.OnClickBack) },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = { onUiEvent(FacultyPickerUiEvent.OnLoadData) },
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        uiState.panelState.Handle(
            modifier = modifier.padding(innerPadding),
            onUiEvent = onUiEvent,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FacultyPickerUiState.PanelState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyPickerUiEvent) -> Unit,
) {
    when (this) {
        is FacultyPickerUiState.PanelState.None -> Unit
        is FacultyPickerUiState.PanelState.SelectFaculty -> {
            if (faculties.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                }
            } else {
                FlowRow(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    faculties.forEach {
                        FacultyView(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onUiEvent(FacultyPickerUiEvent.OnClickFaculty(it))
                                },
                            faculty = it,
                        )
                    }
                }
            }
        }
        is FacultyPickerUiState.PanelState.SelectBatch -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            modifier = Modifier.weight(0.75f),
                            text = selectedFaculty.title
                        )
                        TextButton(
                            onClick = {
                                onUiEvent(FacultyPickerUiEvent.OnClickChangeFaculty)
                            },
                        ) {
                            Text(text = stringResource(Res.string.txt_change))
                        }
                    }
                }
                items(batches) { batch ->
                    BatchListItemSimpleView(batch = batch) {
                        onUiEvent(FacultyPickerUiEvent.OnClickBatch(batch))
                    }
                }
                if (batches.isEmpty()) {
                    item { AnimatedEmptyView(modifier = Modifier.height(240.dp)) }
                }
            }
        }
        is FacultyPickerUiState.PanelState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.height(240.dp))
                TextButton(
                    onClick = {
                        onUiEvent(FacultyPickerUiEvent.OnClickChangeFaculty)
                    },
                ) {
                    Text(text = stringResource(Res.string.txt_change_faculty))
                }
            }
        }
    }
}
