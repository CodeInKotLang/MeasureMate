package com.example.measuremate.presentation.dashboard

import android.content.Context

sealed class DashboardEvent {
    data class AnonymousUserSignInWithGoogle(val context: Context): DashboardEvent()
    data object SignOut: DashboardEvent()
}