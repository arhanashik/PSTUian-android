package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun OutlinedTextInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    inputType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    isError: Boolean = false,
    supportingText: String? = null,
    shape: Shape = RoundedCornerShape(32.dp),
    unfocusedBorderColor: Color = Color.LightGray,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (value: String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        readOnly = readOnly,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        trailingIcon = trailingIcon,
        prefix = prefix?.let { { Text(text = it) } },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        supportingText = supportingText?.let { { Text(text = it) } },
        shape = shape,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = unfocusedBorderColor,
        ),
    )
}

/**
 * @param anchorView : child view to anchor the drop down.
 *                     1. Child view should always use the modifier passed by the parent.
 *                     2. Boolean value can be used to check the expand state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuBox(
    anchorView: @Composable (Modifier, Boolean) -> Unit,
    items: Array<String>,
    onItemChange: (item: String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        anchorView(Modifier.menuAnchor(), expanded)
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { /* No Op */ }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}