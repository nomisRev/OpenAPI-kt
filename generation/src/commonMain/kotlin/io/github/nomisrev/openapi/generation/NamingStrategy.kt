package io.github.nomisrev.openapi.generation

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.NamingContext
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase

interface NamingStrategy {
  public fun typeName(model: Model): String
  public fun toBinary(binary: Model.Binary): String
  public fun toFreeFormJson(json: Model.FreeFormJson): String
  public fun toCollection(collection: Collection): String
  public fun toList(list: Collection.List): String
  public fun toSet(set: Collection.Set): String
  public fun toMap(map: Collection.Map): String
  public fun toEnumClassName(context: NamingContext): String
  public fun toEnumValueName(value: String): String
  public fun toObjectClassName(context: NamingContext): String
  public fun toParamName(objContext: NamingContext, paramName: String): String
  public fun toUnionClassName(context: NamingContext): String
  public fun toPrimitiveName(model: Model.Primitive): String

  /**
   * Union types are a hard piece to generate (oneOf, anyOf).
   * Depending on where the union occurs, we need a different name.
   *  1. Top-level case schema, use user defined name
   *  2. _Inline case schema_, if primitive we generate CasePrimitive
   *     => Int, Ints for List<Int>, IntsList for List<List<Int>>.
   *     => duplicate schemas can be filtered out.
   */
  fun toUnionCaseName(context: NamingContext, model: Model, depth: List<Model> = emptyList()): String
}

object DefaultNamingStrategy : NamingStrategy {
  public override fun typeName(model: Model): String =
    when (model) {
      is Model.Binary -> toBinary(model)
      is Model.FreeFormJson -> toFreeFormJson(model)
      is Collection -> toCollection(model)
      is Model.Enum -> toEnumClassName(model.context)
      is Model.Object -> toObjectClassName(model.context)
      is Model.Union -> toUnionClassName(model.context)
      is Model.Primitive -> toPrimitiveName(model)
    }

  override fun toBinary(binary: Model.Binary): String = "FileUpload"

  override fun toFreeFormJson(json: Model.FreeFormJson): String = "JsonElement"

  override fun toCollection(collection: Collection): String =
    when (collection) {
      is Collection.List -> toList(collection)
      is Collection.Map -> toMap(collection)
      is Collection.Set -> toSet(collection)
    }

  override fun toList(list: Collection.List): String =
    "List<${typeName(list.value)}>"

  override fun toSet(set: Collection.Set): String =
    "Set<${typeName(set.value)}>"

  override fun toMap(map: Collection.Map): String =
    "Map<${typeName(map.key)}, ${typeName(map.value)}>"

  override fun toEnumClassName(context: NamingContext): String =
    when (context) {
      is NamingContext.Inline -> context.content.toPascalCase()
      is NamingContext.Ref -> context.outer.content.toPascalCase()
      is NamingContext.ClassName -> context.content.toPascalCase()
      is NamingContext.OperationParam -> {
        requireNotNull(context.operationId) { "Need operationId to generate enum name" }
        // $MyObject$Param$Request, this allows for multiple custom objects in a single operation
        "${context.operationId.toPascalCase()}${context.content.toPascalCase()}${context.postfix.toPascalCase()}"
      }
    }

  override fun toEnumValueName(value: String): String =
    value.toPascalCase()

  override fun toObjectClassName(context: NamingContext): String =
    context.content.toPascalCase()

  override fun toParamName(objContext: NamingContext, paramName: String): String =
    paramName.sanitize().toCamelCase()

  override fun toUnionClassName(context: NamingContext): String =
    context.content.toPascalCase()

  override fun toPrimitiveName(model: Model.Primitive): String =
    model.name

  override fun toUnionCaseName(context: NamingContext, model: Model, depth: List<Model>): String =
    when (model) {
      is Collection.List -> toUnionCaseName(context, model.value, depth + listOf(model))
      is Collection.Map -> toUnionCaseName(context, model.value, depth + listOf(model))
      is Collection.Set -> toUnionCaseName(context, model.value, depth + listOf(model))
      else -> {
        val head = depth.firstOrNull()
        val s = when (head) {
          is Collection.List -> "s"
          is Collection.Set -> "s"
          is Collection.Map -> "Map"
          else -> ""
        }
        val postfix = depth.drop(1).joinToString(separator = "") {
          when (it) {
            is Collection.List -> "List"
            is Collection.Map -> "Map"
            is Collection.Set -> "Set"
            else -> ""
          }
        }
        val typeName = when (model) {
          is Collection.List -> "List"
          is Collection.Map -> "Map"
          is Collection.Set -> "Set"
          else -> typeName(model)
        }
        "Case$typeName${s}$postfix"
      }
    }
}