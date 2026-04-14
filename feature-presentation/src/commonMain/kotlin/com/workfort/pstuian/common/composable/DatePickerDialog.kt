package com.workfort.pstuian.common.composable

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DatePickerDialog(
    selectableDates: SelectableDates? = null,
    onDismissRequest: () -> Unit,
    onSelect: (Long?) -> Unit,
) {
    val state = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = selectableDates ?: DatePickerDefaults.AllDates,
    )
    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onSelect(state.selectedDateMillis) }) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Dismiss")
            }
        },
    ) {
        DatePicker(state = state)
    }
}