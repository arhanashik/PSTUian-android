package com.workfort.pstuian.app.ui.common.facultypicker

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.FacultyView
import com.workfort.pstuian.app.ui.faculty.BatchListItemSimpleView
import com.workfort.pstuian.appconstant.Const


@Composable
fun FacultyPickerScreen(
    modifier: Modifier = Modifier,
    viewModel: FacultyPickerViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<FacultyPickerScreenUiEvent>(FacultyPickerScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = FacultyPickerScreenUiEvent.OnLoadData
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
            is FacultyPickerScreenUiEvent.None -> Unit
            is FacultyPickerScreenUiEvent.OnLoadData -> viewModel.loadInitialData()
            is FacultyPickerScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is FacultyPickerScreenUiEvent.OnClickFaculty -> viewModel.onClickFaculty(faculty)
            is FacultyPickerScreenUiEvent.OnClickBatch -> viewModel.onClickBatch(batch)
            is FacultyPickerScreenUiEvent.OnClickChangeFaculty -> viewModel.onClickChangeFaculty()
            is FacultyPickerScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = FacultyPickerScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FacultyPickerScreenContent(
    modifier: Modifier,
    displayState: FacultyPickerScreenState.DisplayState,
    onUiEvent: (FacultyPickerScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val title = when (displayState.panelState) {
        is FacultyPickerScreenState.DisplayState.PanelState.SelectFaculty -> "Select Faculty"
        is FacultyPickerScreenState.DisplayState.PanelState.SelectBatch -> "Select Batch"
        else -> ""
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = title,
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(FacultyPickerScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(FacultyPickerScreenUiEvent.OnLoadData)
                },
            )
        },
    ) { innerPadding ->
        displayState.panelState.Handle(
            modifier = modifier.padding(innerPadding),
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
private fun FacultyPickerScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyPickerScreenUiEvent) -> Unit,
) {
    FacultyPickerScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FacultyPickerScreenState.DisplayState.PanelState.Handle(
    modifier: Modifier,
    onUiEvent: (FacultyPickerScreenUiEvent) -> Unit,
) {
    when (this) {
        is FacultyPickerScreenState.DisplayState.PanelState.None -> Unit
        is FacultyPickerScreenState.DisplayState.PanelState.SelectFaculty -> {
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
                                    onUiEvent(FacultyPickerScreenUiEvent.OnClickFaculty(it))
                                },
                            faculty = it,
                        )
                    }
                }
            }
        }
        is FacultyPickerScreenState.DisplayState.PanelState.SelectBatch -> {
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
                                onUiEvent(FacultyPickerScreenUiEvent.OnClickChangeFaculty)
                            },
                        ) {
                            Text(text = "Change")
                        }
                    }
                }
                items(batches) { batch ->
                    BatchListItemSimpleView(batch = batch) {
                        onUiEvent(FacultyPickerScreenUiEvent.OnClickBatch(batch))
                    }
                }
                if (batches.isEmpty()) {
                    item { AnimatedEmptyView(modifier = Modifier.height(240.dp)) }
                }
            }
        }
        is FacultyPickerScreenState.DisplayState.PanelState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.height(240.dp))
                TextButton(
                    onClick = {
                        onUiEvent(FacultyPickerScreenUiEvent.OnClickChangeFaculty)
                    },
                ) {
                    Text(text = "Change Faculty")
                }
            }
        }
    }
}

@Composable
private fun FacultyPickerScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (FacultyPickerScreenUiEvent) -> Unit,
) {
    when (this) {
        is FacultyPickerScreenState.NavigationState.GoBack -> {
            selectedFacultyId?.let { facultyId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Const.Key.FACULTY, facultyId)
            }
            selectedBatchId?.let { batchId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Const.Key.BATCH, batchId)
            }
            navController.popBackStack()
        }
    }
    onUiEvent(FacultyPickerScreenUiEvent.NavigationConsumed)
}