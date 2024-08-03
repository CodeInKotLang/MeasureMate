package com.example.measuremate.domain.model

data class BodyPart(
    val name: String,
    val isActive: Boolean,
    val measuringUnit: String,
    val latestValue: Float? = null,
    val bodyPartId: String? = null
)

val predefinedBodyParts: List<BodyPart> = listOf(
    BodyPart(
        name = "Waist",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code,
        bodyPartId = "541"
    ),
    BodyPart(
        name = "Body Fat",
        isActive = true,
        measuringUnit = MeasuringUnit.PERCENT.code
    ),
    BodyPart(
        name = "Height",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Weight",
        isActive = true,
        measuringUnit = MeasuringUnit.KG.code
    ),
    BodyPart(
        name = "Biceps (Left)",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Biceps (Right)",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Chest",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Triceps (Left)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Triceps (Right)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Shoulders",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Hips",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Thigh (Left)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Thigh (Right)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Calve (Left)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    ),
    BodyPart(
        name = "Calve (Right)",
        isActive = false,
        measuringUnit = MeasuringUnit.CM.code
    )
)
