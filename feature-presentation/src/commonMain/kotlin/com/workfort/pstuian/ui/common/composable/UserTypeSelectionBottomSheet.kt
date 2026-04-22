package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_save_and_continue
import pstuian.feature_presentation.generated.resources.helper_app_usage_role_sheet
import pstuian.feature_presentation.generated.resources.title_select_app_usage_role
import pstuian.feature_presentation.generated.resources.txt_visitor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserTypeSelectionBottomSheet(
    selectedUserType: UserType?,
    onSaveAndContinue: (UserType?) -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden || onDismiss != null },
    )
    ModalBottomSheet(
        onDismissRequest = { onDismiss?.invoke() },
        sheetState = sheetState,
        content = { BottomSheetContent(selectedUserType, onSaveAndContinue) },
    )
}

@Composable
private fun BottomSheetContent(
    selectedUserType: UserType?,
    onSaveAndContinue: (UserType?) -> Unit,
) {
    var currentSelection by remember { mutableStateOf(selectedUserType) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Title
            Text(
                text = stringResource(Res.string.title_select_app_usage_role),
                style = TextStyle.title3.copy(color = AppColors.textPrimary),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(Res.string.helper_app_usage_role_sheet),
                style = TextStyle.body3.copy(color = AppColors.textSecondary),
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Visitor option
            UserTypeOption(
                userTypeLabel = stringResource(Res.string.txt_visitor),
                isSelected = currentSelection == null,
                onSelect = { currentSelection = null },
            )
            // User types
            UserType.entries.forEach { userType ->
                UserTypeOption(
                    userTypeLabel = userType.localizedLabel(),
                    isSelected = currentSelection == userType,
                    onSelect = { currentSelection = userType },
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Action button
            ActionButton(
                label = stringResource(Res.string.btn_save_and_continue),
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                onClick = { onSaveAndContinue(currentSelection) },
            )
        }
    }
}

@Composable
private fun UserTypeOption(
    userTypeLabel: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Text(
        text = userTypeLabel,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                },
            )
            .clickable { onSelect() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
    )
}
