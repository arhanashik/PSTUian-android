package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_visitor

/**
 * One selectable row in [ListSelectionBottomSheet]. [value] may be null when the sheet should offer
 * an explicit “none” / default choice (e.g. visitor with no [com.workfort.pstuian.featuredomain.model.UserType]).
 */
internal data class ListSelectionOption<T>(
    val value: T?,
    val label: String,
)

/**
 * Modal bottom sheet: title, optional helper, scrollable option list, primary action.
 * Use for faculty picker, user type, or any similar list selection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> ListSelectionBottomSheet(
    title: String,
    helperText: String? = null,
    primaryButtonLabel: String,
    primaryButtonIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowForward,
    options: List<ListSelectionOption<T>>,
    initialSelection: T?,
    scrollable: Boolean = true,
    onDismiss: (() -> Unit)? = null,
    onConfirm: (T?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden || onDismiss != null },
    )
    ModalBottomSheet(
        onDismissRequest = { onDismiss?.invoke() },
        sheetState = sheetState,
        content = {
            ListSelectionBottomSheetContent(
                title = title,
                helperText = helperText,
                primaryButtonLabel = primaryButtonLabel,
                primaryButtonIcon = primaryButtonIcon,
                options = options,
                initialSelection = initialSelection,
                scrollable = scrollable,
                onConfirm = onConfirm,
            )
        },
    )
}

@Composable
private fun <T> ListSelectionBottomSheetContent(
    title: String,
    helperText: String?,
    primaryButtonLabel: String,
    primaryButtonIcon: ImageVector?,
    options: List<ListSelectionOption<T>>,
    initialSelection: T?,
    scrollable: Boolean,
    onConfirm: (T?) -> Unit,
) {
    var currentSelection by remember(initialSelection) {
        mutableStateOf(initialSelection)
    }

    val columnModifier = if (scrollable) {
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    } else {
        Modifier.fillMaxWidth()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(modifier = columnModifier) {
            Text(
                text = title,
                style = TextStyle.title3.copy(color = AppColors.textPrimary),
            )
            if (helperText != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = helperText,
                    style = TextStyle.body3.copy(color = AppColors.textSecondary),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            options.forEach { option ->
                ListSelectionOptionRow(
                    label = option.label,
                    isSelected = currentSelection == option.value,
                    onSelect = { currentSelection = option.value },
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ActionButton(
                label = primaryButtonLabel,
                icon = primaryButtonIcon,
                onClick = { onConfirm(currentSelection) },
            )
        }
    }
}

@Composable
private fun ListSelectionOptionRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Text(
        text = label,
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

internal fun facultiesToListSelectionOptions(
    faculties: List<Faculty>,
): List<ListSelectionOption<Faculty>> = faculties.map { faculty ->
    ListSelectionOption(
        value = faculty,
        label = faculty.title.ifBlank { faculty.shortTitle },
    )
}

internal fun batchesToListSelectionOptions(
    batches: List<Batch>,
): List<ListSelectionOption<Batch>> = batches.map { batch ->
    ListSelectionOption(batch, batchDisplayLabel(batch))
}

internal fun batchDisplayLabel(batch: Batch): String =
    batch.title?.takeIf { it.isNotBlank() } ?: batch.name

@Composable
internal fun userTypeListSelectionOptions(): List<ListSelectionOption<UserType>> {
    val visitorLabel = stringResource(Res.string.txt_visitor)
    return buildList {
        add(ListSelectionOption(value = null, label = visitorLabel))
        UserType.entries.forEach { userType ->
            add(ListSelectionOption(value = userType, label = userType.localizedLabel()))
        }
    }
}
