package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

/**
 * The JSON value of each `enum` member of this schema, or the single value of a scalar `const`, which is an enum of
 * one. A `const` that is an array or an object is not an enum.
 */
internal fun Schema.enumMembers(): List<JsonElement>? =
    enum ?: `const`?.takeIf { it is JsonPrimitive }?.let(::listOf)

/**
 * The text of each [enumMembers], for the code that names things after a literal (a discriminator value, a union
 * case) and so only ever compares text. `null` stands for the JSON `null`; the string `"null"` is `"null"`.
 */
internal fun Schema.enumLikeValues(): List<String?>? =
    enumMembers()?.map { member ->
        when (member) {
            JsonNull -> null
            is JsonPrimitive -> member.content
            else -> member.toString()
        }
    }

internal fun Schema.withoutEnumLikeValues(): Schema =
    copy(enum = null, `const` = null)
