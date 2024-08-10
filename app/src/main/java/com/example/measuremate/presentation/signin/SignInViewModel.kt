package com.example.measuremate.presentation.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measuremate.domain.model.AuthStatus
import com.example.measuremate.domain.model.predefinedBodyParts
import com.example.measuremate.domain.repository.AuthRepository
import com.example.measuremate.domain.repository.DatabaseRepository
import com.example.measuremate.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    val authStatus = authRepository.authStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = AuthStatus.LOADING
        )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }

            is SignInEvent.SignInWithGoogle -> {
                signInWithGoogle(event.context)
            }
        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            _state.update { it.copy(isAnonymousSignInButtonLoading = true) }
            authRepository.signInAnonymously()
                .onSuccess {
                    databaseRepository.addUser()
                        .onSuccess {
                            try {
                                insertPredefinedBodyParts()
                                _uiEvent.send(UiEvent.Snackbar(message = "Signed in successfully"))
                            } catch (e: Exception) {
                                _uiEvent.send(UiEvent.Snackbar(message = "Signed in, but failed to insert predefined body parts. ${e.message}"))
                            }
                        }
                        .onFailure { e ->
                            _uiEvent.send(UiEvent.Snackbar(message = "Couldn't add user. ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't sign in. ${e.message}"))
                }
            _state.update { it.copy(isAnonymousSignInButtonLoading = false) }
        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isGoogleSignInButtonLoading = true) }
            authRepository.signIn(context)
                .onSuccess { isNewUser ->
                    if (isNewUser) {
                        databaseRepository.addUser()
                            .onSuccess {
                                try {
                                    insertPredefinedBodyParts()
                                    _uiEvent.send(UiEvent.Snackbar(message = "Signed in successfully"))
                                } catch (e: Exception) {
                                    _uiEvent.send(UiEvent.Snackbar(message = "Signed in, but failed to insert predefined body parts. ${e.message}"))
                                }
                            }
                            .onFailure { e ->
                                _uiEvent.send(UiEvent.Snackbar(message = "Couldn't add user. ${e.message}"))
                            }
                    } else {
                        _uiEvent.send(UiEvent.Snackbar(message = "Signed in successfully"))
                    }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't sign in. ${e.message}"))
                }
            _state.update { it.copy(isGoogleSignInButtonLoading = false) }
        }
    }

    private suspend fun insertPredefinedBodyParts() {
        predefinedBodyParts.forEach { bodyPart ->
            databaseRepository.upsertBodyPart(bodyPart)
        }
    }
}