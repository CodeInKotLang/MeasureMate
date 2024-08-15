package com.example.measuremate.data.mapper

import com.example.measuremate.domain.model.BodyPart

data class BodyPartDto(
    val name: String = "",
    val active: Boolean = false,
    val measuringUnit: String = "",
    val latestValue: Float? = null,
    val bodyPartId: String? = null
)

fun BodyPart.toBodyPartDto(): BodyPartDto {
    return BodyPartDto(
        name = name,
        active = isActive,
        measuringUnit = measuringUnit,
        bodyPartId = bodyPartId,
        latestValue = latestValue
    )
}

fun BodyPartDto.toBodyPart(): BodyPart {
    return BodyPart(
        name = name,
        isActive = active,
        measuringUnit = measuringUnit,
        bodyPartId = bodyPartId,
        latestValue = latestValue
    )
}