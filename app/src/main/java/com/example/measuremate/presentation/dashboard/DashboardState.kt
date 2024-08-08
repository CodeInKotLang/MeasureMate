package com.example.measuremate.presentation.dashboard

import com.example.measuremate.domain.model.BodyPart
import com.example.measuremate.domain.model.User

data class DashboardState(
    val user: User? = null,
    val bodyParts: List<BodyPart> = emptyList(),
    val isSignOutButtonLoading: Boolean = false,
    val isSignInButtonLoading: Boolean = false,
)
