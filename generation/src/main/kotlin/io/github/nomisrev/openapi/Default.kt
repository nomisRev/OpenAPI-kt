package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ParameterSpec
import io.github.nomisrev.openapi.Model.Collection

context(OpenAPIContext)
fun Model.hasDefault(): Boolean = defaultValueImpl(this) != null

context(OpenAPIContext)
fun ParameterSpec.Builder.defaultValue(model: Model): ParameterSpec.Builder =
  defaultValueImpl(model)?.let { (code, args) -> defaultValue(code, *args.toTypedArray()) } ?: this

context(OpenAPIContext)
private fun defaultValueImpl(model: Model): Pair<String, List<Any>>? =
  when (model) {
    is Model.OctetStream -> null
    is Collection.Map -> null
    is Model.FreeFormJson -> null
    is Collection.List -> default(model, "listOf", model.default)
    is Collection.Set -> default(model, "setOf", model.default)
    is Model.Union ->
      model.cases
        .find { it.model is Model.Primitive.String }
        ?.let { case ->
          model.default?.let { default ->
            val typeName = toCaseClassName(model, case.model)
            Pair("%T(%S)", listOf(typeName, default))
          }
        }
    is Model.Object ->
      if (model.properties.all { it.model.hasDefault() })
        Pair("%T()", listOf(toClassName(model.context)))
      else null
    is Model.Enum ->
      (model.default ?: model.values.singleOrNull())?.let {
        Pair("%T.%L", listOf(toClassName(model.context), toEnumValueName(it)))
      }
    is Model.Primitive.Unit -> Pair("Unit", emptyList())
    is Model.Primitive -> model.default()?.let { Pair(it, emptyList()) }
  }

context(OpenAPIContext)
private fun default(
  model: Collection,
  builder: String,
  default: List<String>?,
): Pair<String, List<Any>>? =
  when {
    default == null -> null
    default.isEmpty() -> Pair("emptyList()", emptyList())
    model.inner is Model.Enum -> {
      val enum = model.inner as Model.Enum
      val enumClassName = toClassName(enum.context)
      val content = default.joinToString { "%T.%L" }
      val args = default.flatMap { listOf(enumClassName, toEnumValueName(it)) }
      Pair("$builder($content)", args)
    }
    else -> Pair("$builder(${default.joinToString()})", emptyList())
  }
