package com.example.measuremate.data.mapper

import com.example.measuremate.domain.model.BodyPartValue
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class BodyPartValueDto(
    val value: Float = 0.0f,
    val date: Timestamp = Timestamp.now(),
    val bodyPartId: String? = null,
    val bodyPartValueId: String? = null,
)

fun BodyPartValueDto.toBodyPartValue(): BodyPartValue {
    return BodyPartValue(
        value = value,
        date = date.toLocalDate(),
        bodyPartId = bodyPartId,
        bodyPartValueId = bodyPartValueId
    )
}

fun BodyPartValue.toBodyPartValueDto(): BodyPartValueDto {
    return BodyPartValueDto(
        value = value,
        date = date.toTimestamp(),
        bodyPartId = bodyPartId,
        bodyPartValueId = bodyPartValueId
    )
}

private fun Timestamp.toLocalDate(): LocalDate {
    return Instant
        .ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

private fun LocalDate.toTimestamp(): Timestamp {
    val instant: Instant = this
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
    return Timestamp(instant.toEpochMilli() / 1000, instant.nano % 1000000)
}