package com.example.measuremate.presentation.util

sealed class UiEvent {
    data class Snackbar(val message: String) : UiEvent()
}