package com.workfort.pstuian.ui.students.composable

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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.common.composable.AnimatedEmptyView
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.LabelText
import com.workfort.pstuian.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.common.composable.NavigationButton
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.ui.students.state.MessageState
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_registration_number
import pstuian.feature_presentation.generated.resources.txt_title_call

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsContentPanel(
    uiState: StudentsUiState,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = uiState.title,
                navigation = {
                    NavigationButton {
                        onUiEvent(StudentsUiEvent.ClickBack)
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (uiState.error != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedErrorView(modifier = Modifier.fillMaxWidth())
                }
            } else if (uiState.items.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                StudentListView(
                    students = uiState.items,
                    isLoading = uiState.isLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }

    uiState.messageState?.let {
        HandleMessageState(it, onUiEvent)
    }
}

@Composable
private fun StudentListView(
    students: List<StudentEntity>,
    isLoading: Boolean,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(students) { student ->
            StudentListItemView(
                student = student,
                onClickStudent = {
                    onUiEvent(StudentsUiEvent.ClickStudent(student))
                },
                onClickCall = {
                    student.phone?.let { onUiEvent(StudentsUiEvent.ClickCall(it)) }
                },
            )
        }
        if (isLoading) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
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
private fun HandleMessageState(
    messageState: MessageState,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    when (messageState) {
        is MessageState.Call -> {
            ShowSuccessDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" ${messageState.phoneNumber}"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(StudentsUiEvent.Call(messageState.phoneNumber))
                },
                onDismiss = {
                    onUiEvent(StudentsUiEvent.MessageConsumed)
                }
            )
        }
    }
}
