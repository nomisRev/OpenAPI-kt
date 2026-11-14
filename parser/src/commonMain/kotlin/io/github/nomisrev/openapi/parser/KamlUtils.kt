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
  entries.firstNotNullOfOrNull { [scalar, node] -> if (scalar.content == key) node else null }

internal fun YamlNode.toJsonElement(): JsonElement =
  when (this) {
    is YamlScalar -> yamlScalarToJsonPrimitive(this.content)
    is YamlNull -> JsonPrimitive(null as String?)
    is YamlList -> JsonArray(this.items.map { it.toJsonElement() })
    is YamlMap ->
      JsonObject(this.entries.mapKeys { it.key.content }.mapValues { it.value.toJsonElement() })
    is YamlTaggedNode -> this.innerNode.toJsonElement()
  }

private val yamlTrue = setOf("true", "True", "TRUE")
private val yamlFalse = setOf("false", "False", "FALSE")
private val yamlDecimalInt = Regex("[-+]?[0-9]+")
private val yamlOctalInt = Regex("0o[0-7]+")
private val yamlHexInt = Regex("0x[0-9a-fA-F]+")
private val yamlFloat = Regex("[-+]?(\\.[0-9]+|[0-9]+(\\.[0-9]*)?)([eE][-+]?[0-9]+)?")

/**
 * Resolves a plain scalar by the YAML 1.2 core schema (https://yaml.org/spec/1.2.2/#103-core-schema), not by what
 * `String.toDouble` accepts: on the JVM that also reads `1d`, `1f`, `NaN`, `Infinity` and hex floats as numbers, which
 * turned the string `1d` into `1.0`. `.inf` and `.nan` have no JSON number and stay text.
 */
private fun yamlScalarToJsonPrimitive(content: String): JsonPrimitive =
  when {
    content in yamlTrue -> JsonPrimitive(true)
    content in yamlFalse -> JsonPrimitive(false)
    yamlDecimalInt.matches(content) ->
      content.toLongOrNull()?.let { JsonPrimitive(it) } ?: JsonPrimitive(content.toDouble())
    yamlOctalInt.matches(content) ->
      content.drop(2).toLongOrNull(8)?.let { JsonPrimitive(it) } ?: JsonPrimitive(content)
    yamlHexInt.matches(content) ->
      content.drop(2).toLongOrNull(16)?.let { JsonPrimitive(it) } ?: JsonPrimitive(content)
    yamlFloat.matches(content) -> JsonPrimitive(content.toDouble())
    else -> JsonPrimitive(content)
  }
