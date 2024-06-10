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
    is Model.Union.AnyOf ->
      when {
        default == null -> null
        isOpenEnumeration() -> {
          val case = schemas.firstNotNullOf { it.model as? Model.Enum }
          val defaultEnum =
            case.values.find { it == default }?.let { naming.toEnumClassName(case.context) }
          defaultEnum ?: "Custom(\"${default}\")"
        }
        else ->
          schemas
            .find { it.model is Model.Primitive.String }
            ?.let { case ->
              "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")"
            }
      }
    is Model.Union.OneOf ->
      schemas
        .find { it.model is Model.Primitive.String }
        ?.takeIf { default != null }
        ?.let { case ->
          "${naming.toUnionClassName(this)}.${naming.toUnionCaseName(case.model)}(\"${default}\")"
        }
    else -> null
  }
