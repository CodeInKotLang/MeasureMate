package com.example.measuremate.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.measuremate.data.util.Constants
import com.example.measuremate.domain.model.BodyPart
import com.example.measuremate.domain.model.BodyPartValue
import com.example.measuremate.domain.model.TimeRange
import com.example.measuremate.domain.repository.DatabaseRepository
import com.example.measuremate.presentation.navigation.Routes
import com.example.measuremate.presentation.util.UiEvent
import com.example.measuremate.presentation.util.changeMillisToLocalDate
import com.example.measuremate.presentation.util.roundToDecimal
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bodyPartId = savedStateHandle.toRoute<Routes.DetailsScreen>().bodyPartId

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(DetailsState())
    val state = combine(
        _state,
        databaseRepository.getBodyPart(bodyPartId),
        databaseRepository.getAllBodyPartValues(bodyPartId)
    ) { state, bodyPart, bodyPartValues ->
        val currentDate = LocalDate.now()
        val last7DaysValues = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(7))
        }
        val last30DaysValues = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(30))
        }
        state.copy(
            bodyPart = bodyPart,
            allBodyPartValues = bodyPartValues,
            graphBodyPartValues = when (state.timeRange) {
                TimeRange.LAST7DAYS -> last7DaysValues
                TimeRange.LAST30DAYS -> last30DaysValues
                TimeRange.ALL_TIME -> bodyPartValues
            }
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.Snackbar(message = "Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DetailsState()
    )

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.AddNewValue -> {
                val state = state.value
                val id = state.allBodyPartValues.find { it.date == state.date }?.bodyPartValueId
                val bodyPartValue = BodyPartValue(
                    value = state.textFieldValue.roundToDecimal(decimalPlaces = 2),
                    date = state.date,
                    bodyPartId = bodyPartId,
                    bodyPartValueId = id
                )
                upsertBodyPartValue(bodyPartValue)
                _state.update { it.copy(textFieldValue = "") }
            }

            is DetailsEvent.ChangeMeasuringUnit -> {
                val bodyPart = state.value.bodyPart?.copy(
                    measuringUnit = event.measuringUnit.code
                )
                upsertBodyPart(bodyPart = bodyPart)
            }

            DetailsEvent.DeleteBodyPart -> {
                deleteBodyPart()
            }

            is DetailsEvent.DeleteBodyPartValue -> {
                deleteBodyPartValue(event.bodyPartValue)
                _state.update { it.copy(recentlyDeletedBodyPartValue = event.bodyPartValue) }
            }

            is DetailsEvent.OnDateChange -> {
                val date = event.millis.changeMillisToLocalDate()
                _state.update { it.copy(date = date) }
            }

            is DetailsEvent.OnTextFieldValueChange -> {
                _state.update { it.copy(textFieldValue = event.value) }
            }

            is DetailsEvent.OnTimeRangeChange -> {
                _state.update { it.copy(timeRange = event.timeRange) }
            }

            DetailsEvent.RestoreBodyPartValue -> {
                upsertBodyPartValue(state.value.recentlyDeletedBodyPartValue)
                _state.update { it.copy(recentlyDeletedBodyPartValue = null) }
            }
        }
    }

    private fun upsertBodyPart(bodyPart: BodyPart?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepository.upsertBodyPart(bodyPart)
                .onSuccess {
                    _uiEvent.send(UiEvent.Snackbar(message = "Body Part saved successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't saved. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepository.deleteBodyPart(bodyPartId)
                .onSuccess {
                    _uiEvent.send(UiEvent.Navigate)
                    _uiEvent.send(UiEvent.Snackbar(message = "Body Part deleted successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't delete. ${e.message}"))
                }
        }
    }

    private fun upsertBodyPartValue(bodyPartValue: BodyPartValue?) {
        viewModelScope.launch {
            bodyPartValue ?: return@launch
            databaseRepository.upsertBodyPartValue(bodyPartValue)
                .onSuccess {
                    _uiEvent.send(UiEvent.Snackbar(message = "Body Part Value saved successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't saved. ${e.message}"))
                }
        }
    }

    private fun deleteBodyPartValue(bodyPartValue: BodyPartValue) {
        viewModelScope.launch {
            databaseRepository.deleteBodyPartValue(bodyPartValue)
                .onSuccess {
                    _uiEvent.send(
                        UiEvent.Snackbar(
                            message = "Body Part Value deleted successfully",
                            actionLabel = "Undo"
                        )
                    )
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.Snackbar(message = "Couldn't delete. ${e.message}"))
                }
        }
    }
}