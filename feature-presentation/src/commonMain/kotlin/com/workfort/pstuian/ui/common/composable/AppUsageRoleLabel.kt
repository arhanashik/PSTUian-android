package com.workfort.pstuian.ui.common.composable

import androidx.compose.runtime.Composable
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_employee
import pstuian.feature_presentation.generated.resources.txt_visitor
import pstuian.feature_presentation.generated.resources.txt_student
import pstuian.feature_presentation.generated.resources.txt_teacher

@Composable
fun AppUsageRole.localizedLabel(): String = when (this) {
    AppUsageRole.TEACHER -> stringResource(Res.string.txt_teacher)
    AppUsageRole.STUDENT -> stringResource(Res.string.txt_student)
    AppUsageRole.EMPLOYEE -> stringResource(Res.string.label_employee)
    AppUsageRole.VISITOR -> stringResource(Res.string.txt_visitor)
}
