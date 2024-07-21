package com.example.measuremate.presentation.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureMateDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    if (isOpen) {
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(onClick = { onConfirmButtonClicked() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(state = state)
            }
        )
    }
}