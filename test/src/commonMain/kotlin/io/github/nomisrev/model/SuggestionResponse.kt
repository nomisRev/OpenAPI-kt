package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SuggestionResponse(
    val id: String? = null,
    val completionStart: Int? = null,
    val completionEnd: Int? = null,
    val matchingStart: Int? = null,
    val matchingEnd: Int? = null,
    val caret: Int? = null,
    val description: String? = null,
    val option: String? = null,
    val prefix: String? = null,
    val suffix: String? = null,
    val group: String? = null,
    val icon: String? = null,
    val auxiliaryIcon: String? = null,
    val className: String? = null,
    @SerialName($$"$type") val type: String? = null,
)
