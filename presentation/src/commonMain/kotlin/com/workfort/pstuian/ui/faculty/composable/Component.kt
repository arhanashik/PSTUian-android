package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.LabelText
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.theme.bgCircle
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_credit
import pstuian.presentation.generated.resources.txt_department
import pstuian.presentation.generated.resources.txt_designation
import pstuian.presentation.generated.resources.txt_total_registered_students
import pstuian.presentation.generated.resources.txt_total_students

@Composable
fun BatchListItemView(
    batch: Batch,
    onClickBatch: (batch: Batch) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickBatch(batch) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    TitleTextSmall(text = batch.title ?: batch.name)
                    Text(
                        text = batch.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                InfoBadge(text = "Session ${batch.session}")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_total_registered_students),
                    value = batch.registeredStudent.toString(),
                )
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_total_students),
                    value = batch.totalStudent.toString(),
                )
            }
        }
    }
}

@Composable
private fun InfoBadge(text: String) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 28.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(8.dp)
                .width(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BatchMetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        LabelText(text = label)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun TeacherListItemView(
    teacher: User.Teacher,
    onClickTeacher: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickTeacher() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LoadAsyncUserImage(
                        url = teacher.imageUrl,
                        size = 42.dp,
                        modifier = Modifier.bgCircle(),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        TitleTextSmall(text = teacher.name)
                        Text(
                            text = teacher.department,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                IconButton(onClick = { onClickCall() }, enabled = teacher.phone.isNullOrEmpty().not()) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call teacher")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_designation),
                    value = teacher.designation,
                )
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_department),
                    value = teacher.department,
                )
            }
        }
    }
}

@Composable
fun CourseListItemView(
    course: Course,
    onClickCourse: (course: Course) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickCourse(course) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    TitleTextSmall(text = course.courseCode)
                    Text(
                        text = course.courseTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                InfoBadge(text = "${course.creditHour} ${stringResource(Res.string.txt_credit)}")
            }
        }
    }
}

@Composable
fun EmployeeListItemView(
    employee: User.Employee,
    onClickEmployee: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClickEmployee() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LoadAsyncUserImage(
                        url = employee.imageUrl,
                        size = 42.dp,
                        modifier = Modifier.bgCircle(),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        TitleTextSmall(text = employee.name)
                        Text(
                            text = employee.department ?: "-",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                IconButton(onClick = { onClickCall() }, enabled = employee.phone.isNullOrEmpty().not()) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call employee")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_designation),
                    value = employee.designation,
                )
                BatchMetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.txt_department),
                    value = employee.department ?: "-",
                )
            }
        }
    }
}
