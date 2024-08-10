package com.example.measuremate.presentation.util

sealed class UiEvent {
    data class Snackbar(
        val message: String,
        val actionLabel: String? = null
    ) : UiEvent()
    data object HideBottomSheet: UiEvent()
    data object Navigate: UiEvent()
}