package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Resolved

fun Resolved<Model>.default(naming: NamingStrategy): String? = value.default(naming)

fun Model.default(naming: NamingStrategy): String? =
  when (this) {
    Model.Primitive.Unit -> "Unit"
    is Collection.List ->
      default?.joinToString(prefix = "listOf(", postfix = ")") {
        (resolved.value as? Model.Enum)?.let { inner ->
          "${naming.toEnumClassName(inner.context)}.${naming.toEnumValueName(it)}"
        }
          ?: it
      }
    is Collection.Set ->
      default?.joinToString(prefix = "setOf(", postfix = ")") {
        (resolved.value as? Model.Enum)?.let { inner ->
          "${naming.toEnumClassName(inner.context)}.${naming.toEnumValueName(it)}"
        }
          ?: it
      }
    is Model.Enum ->
      (default ?: values.singleOrNull())?.let {
        "${naming.toEnumClassName(context)}.${naming.toEnumValueName(it)}"
      }
    is Model.Primitive -> default()
    is Model.Union ->
      cases
        .find { it.model.value is Model.Primitive.String }
        ?.takeIf { default != null }
        ?.let { case ->
          "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")"
        }
    else -> null
  }
