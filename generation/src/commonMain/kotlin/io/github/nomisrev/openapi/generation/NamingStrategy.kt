package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Resolved
import net.pearx.kasechange.splitter.WordSplitterConfig
import net.pearx.kasechange.splitter.WordSplitterConfigurable
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase

interface NamingStrategy {
  fun typeName(model: Model): String

  fun toBinary(binary: Model.Binary): String

  fun toFreeFormJson(json: Model.FreeFormJson): String

  fun toCollection(collection: Collection): String

  fun toList(list: Collection.List): String

  fun toSet(set: Collection.Set): String

  fun toMap(map: Collection.Map): String

  fun toEnumClassName(context: NamingContext): String

  fun toEnumValueName(value: String): String

  fun toObjectClassName(context: NamingContext): String

  fun toFunctionName(context: NamingContext): String =
    toObjectClassName(context).replaceFirstChar { it.lowercase() }

  fun toParamName(objContext: NamingContext, paramName: String): String

  fun toUnionClassName(model: Model.Union): String

  fun toPrimitiveName(model: Model.Primitive): String

  /**
   * Union types are a hard piece to generate (oneOf, anyOf). Depending on where the union occurs,
   * we need a different name.
   * 1. Top-level case schema, use user defined name
   * 2. _Inline case schema_, if primitive we generate CasePrimitive => Int, Ints for List<Int>,
   *    IntsList for List<List<Int>>. => duplicate schemas can be filtered out.
   */
  fun toUnionCaseName(model: Model, depth: List<Model> = emptyList()): String

  fun toUnionCaseName(model: Resolved<Model>, depth: List<Model> = emptyList()): String =
    toUnionCaseName(model.value, depth)
}

object DefaultNamingStrategy : NamingStrategy {
  // OpenAI adds '/', so this WordSplitter takes that into account
  private val wordSplitter =
    WordSplitterConfigurable(
      WordSplitterConfig(
        boundaries = setOf(' ', '-', '_', '.', '/'),
        handleCase = true,
        treatDigitsAsUppercase = true
      )
    )

  private fun String.toPascalCase(): String = toPascalCase(wordSplitter)

  private fun String.toCamelCase(): String = toCamelCase(wordSplitter)

  override fun typeName(model: Model): String =
    when (model) {
      is Model.Binary -> toBinary(model)
      is Model.FreeFormJson -> toFreeFormJson(model)
      is Collection -> toCollection(model)
      is Model.Enum -> toEnumClassName(model.context)
      is Model.Object -> toObjectClassName(model.context)
      is Model.Union -> toUnionClassName(model)
      is Model.Primitive -> toPrimitiveName(model)
    }

  override fun toBinary(binary: Model.Binary): String = "Unit"

  override fun toFreeFormJson(json: Model.FreeFormJson): String = "JsonElement"

  override fun toCollection(collection: Collection): String =
    when (collection) {
      is Collection.List -> toList(collection)
      is Collection.Map -> toMap(collection)
      is Collection.Set -> toSet(collection)
    }

  override fun toList(list: Collection.List): String = "List<${typeName(list.resolved.value)}>"

  override fun toSet(set: Collection.Set): String = "Set<${typeName(set.resolved.value)}>"

  override fun toMap(map: Collection.Map): String =
    "Map<${typeName(map.key)}, ${typeName(map.resolved.value)}>"

  override fun toEnumClassName(context: NamingContext): String =
    when (context) {
      is NamingContext.Nested -> context.name.toPascalCase()
      is NamingContext.Named -> context.name.toPascalCase()
      is NamingContext.RouteParam -> {
        requireNotNull(context.operationId) { "Need operationId to generate enum name" }
        // $MyObject$Param, this allows for multiple custom objects in a single operation
        "${context.operationId.toPascalCase()}${context.name.toPascalCase()}"
      }
    }.dropArraySyntax()

  override fun toEnumValueName(value: String): String {
    val pascalCase = value.toPascalCase()
    return if (pascalCase.isValidClassname()) pascalCase
    else {
      val sanitise =
        pascalCase
          .run { if (startsWith("[")) drop(1) else this }
          .run { if (endsWith("]")) dropLast(1) else this }
      if (sanitise.isValidClassname()) sanitise else "`$sanitise`"
    }
  }

  override fun toObjectClassName(context: NamingContext): String =
    context.name.dropArraySyntax().toPascalCase()

  // Workaround for OpenAI
  private fun String.dropArraySyntax(): String = replace("[]", "")

  override fun toParamName(objContext: NamingContext, paramName: String): String =
    paramName.sanitize().dropArraySyntax().toCamelCase()

  override fun toUnionClassName(model: Model.Union): String {
    val context = model.context
    return when {
      context is NamingContext.Nested ->
        "${context.outer.name.toPascalCase()}${context.name.toPascalCase()}"
      else -> context.name.toPascalCase()
    }
  }

  override fun toPrimitiveName(model: Model.Primitive): String =
    when (model) {
      is Model.Primitive.Boolean -> "Boolean"
      is Model.Primitive.Double -> "Double"
      is Model.Primitive.Int -> "Int"
      is Model.Primitive.String -> "String"
      Model.Primitive.Unit -> "Unit"
    }

  override fun toUnionCaseName(model: Model, depth: List<Model>): String =
    when (model) {
      is Collection.List -> toUnionCaseName(model.resolved.value, depth + listOf(model))
      is Collection.Map -> toUnionCaseName(model.resolved.value, depth + listOf(model))
      is Collection.Set -> toUnionCaseName(model.resolved.value, depth + listOf(model))
      else -> {
        val head = depth.firstOrNull()
        val s =
          when (head) {
            is Collection.List -> "s"
            is Collection.Set -> "s"
            is Collection.Map -> "Map"
            else -> ""
          }
        val postfix =
          depth.drop(1).joinToString(separator = "") {
            when (it) {
              is Collection.List -> "List"
              is Collection.Map -> "Map"
              is Collection.Set -> "Set"
              else -> ""
            }
          }

        "Case${typeName(model)}${s}$postfix"
      }
    }
}
