package com.example.measuremate.presentation.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}

fun String.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    val value = this.toFloatOrNull() ?: 0f
    return (value * multiplier).roundToInt() / multiplier.toFloat()
}

fun LocalDate?.changeLocalDateToGraphDate(
    defaultValue: LocalDate = LocalDate.now()
): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("MMM dd"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}

fun LocalDate?.changeLocalDateToDateString(
    defaultValue: LocalDate = LocalDate.now()
): String {
    return try {
        this?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            ?: defaultValue.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        defaultValue.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}

fun Long?.changeMillisToLocalDate(): LocalDate {
    return try {
        this?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } ?: LocalDate.now()
    } catch (e: Exception) {
        LocalDate.now()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}