package com.workfort.pstuian.view.ui.students

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.reducer.ui.students.StudentsScreenState
import com.workfort.pstuian.view.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.view.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.LabelText
import com.workfort.pstuian.view.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.view.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.view.ui.common.component.TitleTextSmall
import com.workfort.pstuian.viewmodel.students.StudentsViewModel
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.txt_id
import pstuian.shared.generated.resources.txt_registration_number
import pstuian.shared.generated.resources.txt_blood_group
import pstuian.shared.generated.resources.txt_title_call
import pstuian.shared.generated.resources.txt_msg_call
import pstuian.shared.generated.resources.txt_call

@Composable
fun StudentsScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentsViewModel,
    navigateBack: () -> Unit,
    navigateToStudentProfile: (studentId: Int) -> Unit,
    callTo: (phoneNumber: String) -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<StudentsScreenUiEvent>(StudentsScreenUiEvent.None)
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(
            navigateBack = navigateBack,
            navigateToStudentProfile = navigateToStudentProfile,
        ) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is StudentsScreenUiEvent.None -> Unit
            is StudentsScreenUiEvent.LoadStudentList -> viewModel.loadStudentList()
            is StudentsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is StudentsScreenUiEvent.OnClickStudent -> viewModel.onClickStudent(student)
            is StudentsScreenUiEvent.OnClickCall -> viewModel.onClickCall(phoneNumber)
            is StudentsScreenUiEvent.OnCall -> callTo(phoneNumber)
            is StudentsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is StudentsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = StudentsScreenUiEvent.None
    }
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentsScreenContent(
    modifier: Modifier,
    displayState: StudentsScreenState.DisplayState,
    onUiEvent: (StudentsScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(StudentsScreenUiEvent.LoadStudentList)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = displayState.title,
                onClickBack = {
                    onUiEvent(StudentsScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        displayState.studentListState.Handle(modifier.padding(innerPadding), onUiEvent)
    }
}

@Composable
private fun List<StudentEntity>.StudentListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickStudent: (StudentEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@StudentListView) { student ->
            StudentListItemView(
                student = student,
                onClickStudent = { onClickStudent(student) },
                onClickCall = { student.phone?.let(onClickCall) },
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun StudentListItemView(
    student: StudentEntity,
    onClickStudent: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickStudent() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LoadAsyncUserImage(url = student.imageUrl, size = 42.dp)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    TitleTextSmall(text = student.name)
                }
                Row {
                    Column(modifier = Modifier.weight(0.3f)) {
                        LabelText(text = stringResource(Res.string.txt_id))
                        Text(text = student.id.toString(), fontSize = 14.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        LabelText(text = stringResource(Res.string.txt_registration_number))
                        Text(text = student.reg, fontSize = 14.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        LabelText(text = stringResource(Res.string.txt_blood_group))
                        Text(text = student.blood ?: "-", fontSize = 14.sp)
                    }
                }
            }
            VerticalDivider(
                modifier = Modifier
                    .weight(0.05f)
                    .height(84.dp),
                thickness = 1.dp,
                color = Color.LightGray,
            )
            IconButton(
                modifier = Modifier.weight(0.15f),
                onClick = { onClickCall() },
                enabled = student.phone.isNullOrEmpty().not(),
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Icon",
                )
            }
        }
    }
}

@Composable
private fun StudentsScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentsScreenUiEvent) -> Unit,
) {
    StudentsScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun StudentsScreenState.DisplayState.StudentListState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentsScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentsScreenState.DisplayState.StudentListState.None -> Unit
        is StudentsScreenState.DisplayState.StudentListState.Available -> {
            if (students.isEmpty()) {
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
                students.StudentListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onClickStudent = {
                        onUiEvent(StudentsScreenUiEvent.OnClickStudent(it))
                    },
                    onClickCall = {
                        onUiEvent(StudentsScreenUiEvent.OnClickCall(it))
                    },
                )
            }
        }
        is StudentsScreenState.DisplayState.StudentListState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun StudentsScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (StudentsScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentsScreenState.DisplayState.MessageState.Call -> {
            ShowSuccessDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(StudentsScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(StudentsScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun StudentsScreenState.NavigationState.Handle(
    navigateBack: () -> Unit,
    navigateToStudentProfile: (studentId: Int) -> Unit,
    onUiEvent: (StudentsScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentsScreenState.NavigationState.GoBack -> {
            navigateBack()
        }
        is StudentsScreenState.NavigationState.GoToStudentProfile -> {
            navigateToStudentProfile(student.id)
        }
    }
    onUiEvent(StudentsScreenUiEvent.NavigationConsumed)
}