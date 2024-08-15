package com.example.measuremate.presentation.add_item

import com.example.measuremate.domain.model.BodyPart

data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyParts: List<BodyPart> = emptyList()
)
