package com.example.measuremate.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.measuremate.domain.model.MeasuringUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasuringUnitBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isOpen: Boolean,
    onBottomSheetDismiss: () -> Unit,
    onItemClicked: (MeasuringUnit) -> Unit
) {
    if (isOpen) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = { onBottomSheetDismiss() },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Select Measuring Unit",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                }
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(MeasuringUnit.entries) { unit ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClicked(unit) }
                            .padding(vertical = 12.dp, horizontal = 20.dp)
                    ) {
                        Text(
                            text = "${unit.label} (${unit.code})",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}