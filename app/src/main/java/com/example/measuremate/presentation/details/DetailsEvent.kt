package com.example.measuremate.presentation.details

import com.example.measuremate.domain.model.BodyPartValue
import com.example.measuremate.domain.model.MeasuringUnit
import com.example.measuremate.domain.model.TimeRange

sealed class DetailsEvent {
    data object DeleteBodyPart: DetailsEvent()
    data object RestoreBodyPartValue: DetailsEvent()
    data object AddNewValue: DetailsEvent()
    data class DeleteBodyPartValue(val bodyPartValue: BodyPartValue): DetailsEvent()
    data class ChangeMeasuringUnit(val measuringUnit: MeasuringUnit): DetailsEvent()
    data class OnDateChange(val millis: Long?): DetailsEvent()
    data class OnTextFieldValueChange(val value: String): DetailsEvent()
    data class OnTimeRangeChange(val timeRange: TimeRange): DetailsEvent()
}