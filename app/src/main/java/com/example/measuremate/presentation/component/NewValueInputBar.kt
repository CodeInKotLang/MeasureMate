package com.example.measuremate.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NewValueInputBar(
    modifier: Modifier = Modifier,
    date: String,
    value: String,
    isInputValueCardVisible: Boolean,
    onValueChange: (String) -> Unit,
    onDoneIconClick: () -> Unit,
    onDoneImeActionClick: () -> Unit,
    onCalendarIconClick: () -> Unit,
) {
    var inputError by rememberSaveable { mutableStateOf<String?>(null) }
    inputError = when {
        value.isBlank() -> "Please enter new value here."
        value.toFloatOrNull() == null -> "Invalid number."
        value.toFloat() < 1f -> "Please set at least 1."
        value.toFloat() > 1000f -> "Please set a maximum of 1000."
        else -> null
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isInputValueCardVisible,
        enter = slideInVertically(tween(durationMillis = 600)) { h -> h},
        exit = slideOutVertically(tween(durationMillis = 600)) { h -> h}
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(7.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                isError = inputError != null && value.isNotBlank(),
                supportingText = { Text(text = inputError.orEmpty()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = { onDoneImeActionClick() }),
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = date)
                        IconButton(onClick = { onCalendarIconClick() }) {
                            Icon(imageVector = Icons.Rounded.DateRange, contentDescription = "")
                        }
                    }
                }
            )
            FilledIconButton(
                modifier = Modifier.size(50.dp),
                onClick = { onDoneIconClick() },
                enabled = inputError == null
            ) {
                Icon(imageVector = Icons.Rounded.Done, contentDescription = "Done")
            }
        }
    }
}

@Preview
@Composable
private fun NewValueInputBarPreview() {
    NewValueInputBar(
        date = "12 May 2024",
        isInputValueCardVisible = true,
        value = "",
        onValueChange = {},
        onDoneIconClick = {},
        onDoneImeActionClick = {},
        onCalendarIconClick = {}
    )
}