package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection

fun Model.default(naming: NamingStrategy): String? =
  when (this) {
    Model.Primitive.Unit -> "Unit"
    is Collection.List ->
      default?.joinToString(prefix = "listOf(", postfix = ")") {
        if (value is Model.Enum) {
          "${naming.toEnumClassName(value.context)}.${naming.toEnumValueName(it)}"
        } else it
      }
    is Collection.Set ->
      default?.joinToString(prefix = "setOf(", postfix = ")") {
        if (value is Model.Enum) {
          "${naming.toEnumClassName(value.context)}.${naming.toEnumValueName(it)}"
        } else it
      }
    is Model.Enum ->
      (default ?: values.singleOrNull())?.let {
        "${naming.toEnumClassName(context)}.${naming.toEnumValueName(it)}"
      }
    is Model.Primitive -> default()
    is Model.Union ->
      cases
        .find { it.model is Model.Primitive.String }
        ?.takeIf { default != null }
        ?.let { case ->
          "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")"
        }
    else -> null
  }
