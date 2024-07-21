package com.example.measuremate.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MeasureMateDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String,
    confirmButtonText: String = "Yes",
    dismissButtonText: String = "Cancel",
    body:  @Composable (() -> Unit)? = null,
    onDialogDismiss: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            modifier = modifier,
            title = { Text(text = title) },
            text = body,
            onDismissRequest = { onDialogDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirmButtonClick() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onConfirmButtonClick() }) {
                    Text(text = dismissButtonText)
                }
            }
        )
    }
}

@Preview
@Composable
private fun MeasureMateDialogPreview() {
    MeasureMateDialog(
        isOpen = true,
        title = "Login anonymously?",
        body = {
            Text(
                text = "By logging in anonymously, you will not be able to synchronize the data " +
                        "across devices or after uninstalling the app. \nAre you sure you want to proceed?"
            )
        },
        onDialogDismiss = {},
        onConfirmButtonClick = {}
    )
}