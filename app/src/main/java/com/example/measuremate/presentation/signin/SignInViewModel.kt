package com.example.measuremate.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measuremate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }

            is SignInEvent.SignInWithGoogle -> {

            }
        }
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            authRepository.signInAnonymously()
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}