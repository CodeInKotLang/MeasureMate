package com.example.measuremate.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measuremate.domain.repository.AuthRepository
import com.example.measuremate.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.AnonymousUserSignInWithGoogle -> {}
            DashboardEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _uiEvent.send(UiEvent.Snackbar(message = "Signed out successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't signed out. ${e.message}"))
                }
        }
    }
}