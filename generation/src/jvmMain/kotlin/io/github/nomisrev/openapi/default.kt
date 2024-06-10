package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Collection

fun Resolved<Model>.default(): String? =
  value.default()

fun Model.default(): String? =
  when (this) {
    Model.Primitive.Unit -> "Unit"
    is Collection.List ->
      default?.joinToString(prefix = "listOf(", postfix = ")") {
        (inner.value as? Model.Enum)?.let { inner ->
          "${Nam.toClassName(inner.context)}.${Nam.toEnumValueName(it)}"
        }
          ?: it
      }
    is Collection.Set ->
      default?.joinToString(prefix = "setOf(", postfix = ")") {
        (inner.value as? Model.Enum)?.let { inner ->
          "${Nam.toClassName(inner.context)}.${Nam.toEnumValueName(it)}"
        }
          ?: it
      }
    is Model.Enum ->
      (default ?: values.singleOrNull())?.let {
        "${Nam.toClassName(context)}.${Nam.toEnumValueName(it)}"
      }
    is Model.Primitive -> default()
    is Model.Union ->
      cases
        .find { it.model.value is Model.Primitive.String }
        ?.takeIf { default != null }
        ?.let { case ->
          val typeName = Nam.toCaseClassName(this, case.model.value)
            .simpleNames
            .joinToString(".")

          "$typeName(\"${default}\")"
        }
    else -> null
  }
