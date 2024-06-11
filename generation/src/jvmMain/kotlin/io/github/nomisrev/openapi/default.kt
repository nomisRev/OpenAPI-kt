package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ParameterSpec
import io.github.nomisrev.openapi.Model.Collection

fun Model.hasDefault(): Boolean = defaultValueImpl(this) != null

fun ParameterSpec.Builder.defaultValue(model: Model): ParameterSpec.Builder =
  defaultValueImpl(model)?.let { (code, args) -> defaultValue(code, *args.toTypedArray()) } ?: this

private fun defaultValueImpl(model: Model): Pair<String, List<Any>>? =
  when (model) {
    Model.Binary -> null
    is Collection.Map -> null
    Model.FreeFormJson -> null
    is Collection.List -> default(model, "listOf", model.default)
    is Collection.Set -> default(model, "setOf", model.default)
    is Model.Union ->
      model.cases
        .find { it.model.value is Model.Primitive.String }
        ?.let { case ->
          model.default?.let {
            val typeName = Nam.toCaseClassName(model, case.model.value)
            Pair("%T(%S)", listOf(typeName, model.default))
          }
        }
    is Model.Object ->
      if (model.properties.all { it.model.value.hasDefault() })
        Pair("%T()", listOf(Nam.toClassName(model.context)))
      else null
    is Model.Enum ->
      (model.default ?: model.values.singleOrNull())?.let {
        Pair("%T.%L", listOf(Nam.toClassName(model.context), Nam.toEnumValueName(it)))
      }
    is Model.Primitive -> model.default()?.let { Pair(it, emptyList()) }
    Model.Primitive.Unit -> Pair("Unit", emptyList())
  }

private fun default(
  model: Collection,
  builder: String,
  default: List<String>?
): Pair<String, List<Any>>? =
  when {
    default == null -> null
    default.isEmpty() -> Pair("emptyList()", emptyList())
    model.inner.value is Model.Enum -> {
      val enum = model.inner.value as Model.Enum
      val enumClassName = Nam.toClassName(enum.context)
      val content = default.joinToString { "%T.%L" }
      val args = default.flatMap { listOf(enumClassName, Nam.toEnumValueName(it)) }
      Pair("$builder($content)", args)
    }
    else -> Pair("$builder(${default.joinToString()})", emptyList())
  }
