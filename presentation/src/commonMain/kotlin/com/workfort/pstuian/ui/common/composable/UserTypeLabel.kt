package com.workfort.pstuian.ui.common.composable

import androidx.compose.runtime.Composable
import com.workfort.pstuian.featuredomain.model.UserType
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.label_employee
import pstuian.presentation.generated.resources.txt_student
import pstuian.presentation.generated.resources.txt_teacher

@Composable
fun UserType.localizedLabel(): String = when (this) {
    UserType.STUDENT -> stringResource(Res.string.txt_student)
    UserType.TEACHER -> stringResource(Res.string.txt_teacher)
    UserType.EMPLOYEE -> stringResource(Res.string.label_employee)
}
