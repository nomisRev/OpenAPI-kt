package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ParameterSpec
import io.github.nomisrev.openapi.Model.Collection

context(OpenAPIContext)
fun Model.hasDefault(): Boolean = when (this) {
  is Collection.Map,
  is Model.Reference,
  is Model.FreeFormJson,
  is Model.OctetStream -> false

  is Collection.List -> hasDefault(this, default)
  is Model.Union ->
    cases.find { it.model is Model.Primitive.String }
      ?.let { default != null } == true

  is Model.Object -> properties.all { it.model.hasDefault() }
  is Model.Enum -> (default ?: values.singleOrNull()) != null

  is Model.Primitive.Unit -> true
  is Model.Primitive -> default() != null
}

context(OpenAPIContext)
private fun hasDefault(model: Collection, default: List<String>?): Boolean = when {
  default == null -> false
  default.isEmpty() -> true
  model.inner is Model.Enum -> true
  else -> true
}

private val TQUOTE = "\"\"\""

context(OpenAPIContext)
fun ParameterSpec.Builder.defaultValue(model: Model): ParameterSpec.Builder = apply {
  when (model) {
    is Model.OctetStream,
    is Model.Reference,
    is Collection.Map,
    is Model.FreeFormJson -> {
    }

    is Collection.List -> when (val default = model.default) {
      null -> {}
      else if default.isEmpty() -> defaultValue("emptyList()")
      else if model.inner is Model.Enum -> {
        val enum = model.inner as Model.Enum
        val enumClassName = toClassName(enum.context)
        val content = default.joinToString { "%T.%L" }
        val args = default.flatMap { listOf(enumClassName, toEnumValueName(it)) }
        defaultValue("listOf($content)", *args.toTypedArray())
      }

      else -> defaultValue("listOf(${default.joinToString()})")
    }

    is Model.Union ->
      model.cases
        .find { it.model is Model.Primitive.String }
        ?.let { case ->
          model.default?.let { default ->
            val typeName = toCaseClassName(model, case.model)
            defaultValue("%T(%S)", typeName, default)
          }
        }

    is Model.Object ->
      if (model.properties.all { it.model.hasDefault() })
        defaultValue("%T()", toClassName(model.context))

    is Model.Enum ->
      (model.default ?: model.values.singleOrNull())?.let {
        defaultValue("%T.%L", toClassName(model.context), toEnumValueName(it))
      }

    is Model.Primitive.Unit -> defaultValue("Unit")
    is Model.Primitive.String if model.default != null ->
      defaultValue($$$"""$$$$$TQUOTE$$${model.default}$$$TQUOTE""")

    is Model.Primitive -> model.default()?.let { defaultValue(it) }
  }
}
