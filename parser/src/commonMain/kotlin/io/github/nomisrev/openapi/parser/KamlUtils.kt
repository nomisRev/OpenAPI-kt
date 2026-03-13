package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlList
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlNull
import com.charleskorn.kaml.YamlScalar
import com.charleskorn.kaml.YamlTaggedNode
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun YamlMap.getOrNull(key: String): YamlNode? =
  entries.firstNotNullOfOrNull { (scalar, node) -> if (scalar.content == key) node else null }

internal fun YamlNode.toJsonElement(): JsonElement =
  when (this) {
    is YamlScalar -> yamlScalarToJsonPrimitive(this.content)
    is YamlNull -> JsonPrimitive(null as String?)
    is YamlList -> JsonArray(this.items.map { it.toJsonElement() })
    is YamlMap ->
      JsonObject(this.entries.mapKeys { it.key.content }.mapValues { it.value.toJsonElement() })
    is YamlTaggedNode -> this.innerNode.toJsonElement()
  }

private fun yamlScalarToJsonPrimitive(content: String): JsonPrimitive =
  content.toBooleanStrictOrNull()?.let { JsonPrimitive(it) }
    ?: content.toLongOrNull()?.let { JsonPrimitive(it) }
    ?: content.toDoubleOrNull()?.let { JsonPrimitive(it) }
    ?: JsonPrimitive(content)
