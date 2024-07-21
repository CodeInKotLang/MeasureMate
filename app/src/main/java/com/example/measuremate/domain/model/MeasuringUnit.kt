package com.example.measuremate.domain.model

enum class MeasuringUnit(
    val code: String,
    val label: String
) {
    CM(code = "cm", label = "Centimeters"),
    IN(code = "in", label = "Inches"),
    FT(code = "ft", label = "Feet"),
    PERCENT(code = "%", label = "Percentage"),
    KG(code = "kg", label = "Kilograms"),
    PD(code = "pd", label = "Pounds"),
    M(code = "m", label = "Meters"),
    MM(code = "mm", label = "Millimeters")
}