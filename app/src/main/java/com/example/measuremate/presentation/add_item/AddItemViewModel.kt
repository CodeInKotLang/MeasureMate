package com.example.measuremate.presentation.add_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measuremate.domain.model.BodyPart
import com.example.measuremate.domain.model.MeasuringUnit
import com.example.measuremate.domain.repository.DatabaseRepository
import com.example.measuremate.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(AddItemState())
    val state = combine(
        _state,
        databaseRepository.getAllBodyParts()
    ) { state, bodyParts ->
        state.copy(bodyParts = bodyParts)
    }.catch { e ->
        _uiEvent.send(UiEvent.Snackbar(message = "Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = AddItemState()
    )

    fun onEvent(event: AddItemEvent) {
        when(event) {
            AddItemEvent.OnAddItemDialogDismiss -> {
                _state.update {
                    it.copy(
                        textFieldValue = "",
                        selectedBodyPart = null
                    )
                }
            }
            is AddItemEvent.OnItemClick -> {
                _state.update {
                    it.copy(
                        textFieldValue = event.bodyPart.name,
                        selectedBodyPart = event.bodyPart
                    )
                }
            }
            is AddItemEvent.OnItemIsActiveChange -> {
                val bodyPart = event.bodyPart
                upsertBodyPart(
                    bodyPart = bodyPart.copy(isActive = !bodyPart.isActive)
                )
            }
            is AddItemEvent.OnTextFieldValueChange -> {
                _state.update { it.copy(textFieldValue = event.value) }
            }
            AddItemEvent.UpsertItem -> {
                val selectedBodyPart = state.value.selectedBodyPart
                val bodyPart = BodyPart(
                    name = state.value.textFieldValue.trim(),
                    isActive = selectedBodyPart?.isActive ?: true,
                    measuringUnit = selectedBodyPart?.measuringUnit ?: MeasuringUnit.CM.code,
                    bodyPartId = selectedBodyPart?.bodyPartId
                )
                upsertBodyPart(bodyPart = bodyPart)
                _state.update { it.copy(textFieldValue = "") }
            }
        }
    }

    private fun upsertBodyPart(bodyPart: BodyPart) {
        viewModelScope.launch {
            databaseRepository.upsertBodyPart(bodyPart)
                .onSuccess {
                    _uiEvent.send(UiEvent.Snackbar(message = "Body Part saved successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't saved. ${e.message}"))
                }
        }
    }

}