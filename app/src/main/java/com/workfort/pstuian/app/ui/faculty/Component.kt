package com.workfort.pstuian.app.ui.faculty

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.LabelText
import com.workfort.pstuian.app.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity


@Composable
fun BatchListItemView(
    modifier: Modifier,
    batch: BatchEntity,
    onClickBatch: (batch: BatchEntity) -> Unit,
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickBatch(batch) },
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
                    .weight(0.75f)
                    .padding(end = 16.dp)
            ) {
                TitleTextSmall(text = batch.title ?: batch.name)
                Text(text = batch.name, fontSize = 16.sp)
                Row {
                    Column(modifier = Modifier.weight(0.6f)) {
                        LabelText(text = context.getString(R.string.txt_total_registered_students))
                        Text(text = batch.registeredStudent.toString(), fontSize = 14.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.4f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        LabelText(text = context.getString(R.string.txt_total_students))
                        Text(text = batch.totalStudent.toString(), fontSize = 12.sp)
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
            Column(
                modifier = Modifier.weight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Session", fontSize = 10.sp, color = Color.LightGray)
                Text(text = batch.session, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BatchListItemSimpleView(
    modifier: Modifier = Modifier,
    batch: BatchEntity,
    onClickBatch: (batch: BatchEntity) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickBatch(batch) },
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
                    .weight(0.75f)
                    .padding(end = 16.dp)
            ) {
                TitleTextSmall(text = batch.name)
            }
            VerticalDivider(
                modifier = Modifier
                    .weight(0.05f)
                    .height(24.dp),
                thickness = 1.dp,
                color = Color.LightGray,
            )
            Text(modifier = Modifier.weight(0.2f), text = batch.session, fontSize = 14.sp)
        }
    }
}

@Composable
fun TeacherListItemView(
    modifier: Modifier,
    teacher: TeacherEntity,
    onClickTeacher: () -> Unit,
    onClickCall: () -> Unit,
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickTeacher() },
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
                    LoadAsyncUserImage(url = teacher.imageUrl, size = 42.dp)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    TitleTextSmall(text = teacher.name)
                }
                Row {
                    Column(modifier = Modifier.weight(0.5f)) {
                        LabelText(text = context.getString(R.string.txt_designation))
                        Text(text = teacher.designation, fontSize = 14.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        LabelText(text = context.getString(R.string.txt_department))
                        Text(text = teacher.department, fontSize = 12.sp)
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
                enabled = teacher.phone.isNullOrEmpty().not(),
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
fun CourseListItemView(
    modifier: Modifier,
    course: CourseEntity,
    onClickCourse: (course: CourseEntity) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickCourse(course) },
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
                TitleTextSmall(text = course.courseCode)
                Text(text = course.courseTitle, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier
                    .weight(0.05f)
                    .height(64.dp),
                thickness = 1.dp,
                color = Color.LightGray,
            )
            Column(
                modifier = Modifier.weight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = course.creditHour, fontSize = 18.sp)
                LabelText(text = LocalContext.current.getString(R.string.txt_credit))
            }
        }
    }
}

@Composable
fun EmployeeListItemView(
    modifier: Modifier,
    employee: EmployeeEntity,
    onClickEmployee: () -> Unit,
    onClickCall: () -> Unit,
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickEmployee() },
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
                    LoadAsyncUserImage(url = employee.imageUrl, size = 42.dp)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    TitleTextSmall(text = employee.name)
                }
                Row {
                    Column(modifier = Modifier.weight(0.5f)) {
                        LabelText(text = context.getString(R.string.txt_designation))
                        Text(text = employee.designation, fontSize = 14.sp)
                    }
                    Column(
                        modifier = Modifier.weight(0.5f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        LabelText(text = context.getString(R.string.txt_department))
                        Text(text = employee.department ?: "-", fontSize = 14.sp)
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
                enabled = employee.phone.isNullOrEmpty().not(),
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Icon",
                )
            }
        }
    }
}

