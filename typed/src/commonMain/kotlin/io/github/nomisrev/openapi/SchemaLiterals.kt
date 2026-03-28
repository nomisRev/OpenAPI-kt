package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

internal fun Schema.enumLikeValues(): List<String?>? =
    enum ?: `const`.asEnumLikeValues()

internal fun Schema.withoutEnumLikeValues(): Schema =
    copy(enum = null, `const` = null)

private fun JsonElement?.asEnumLikeValues(): List<String?>? =
    when (this) {
        null -> null
        JsonNull -> listOf(null)
        is JsonPrimitive -> listOf(content)
        else -> null
    }
