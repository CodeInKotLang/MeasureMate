package com.example.measuremate.presentation.add_item

import com.example.measuremate.domain.model.BodyPart

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value: String): AddItemEvent()
    data class OnItemClick(val bodyPart: BodyPart): AddItemEvent()
    data class OnItemIsActiveChange(val bodyPart: BodyPart): AddItemEvent()
    data object OnAddItemDialogDismiss: AddItemEvent()
    data object UpsertItem: AddItemEvent()
}