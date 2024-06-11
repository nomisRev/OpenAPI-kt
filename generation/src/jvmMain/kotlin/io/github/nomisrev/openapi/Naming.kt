package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SET
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import io.github.nomisrev.openapi.Model.Collection
import kotlinx.serialization.json.JsonElement
import net.pearx.kasechange.splitter.WordSplitterConfig
import net.pearx.kasechange.splitter.WordSplitterConfigurable
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase

object Nam {
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

  private const val `package`: String = "io.github.nomisrev.openapi"

  fun toClassName(context: NamingContext): ClassName =
    when (context) {
      is NamingContext.Nested -> {
        val outer = toClassName(context.outer)
        val inner = toClassName(context.inner)
        ClassName(`package`, outer.simpleNames + inner.simpleNames)
      }
      is NamingContext.Named -> ClassName(`package`, context.name.toPascalCase().dropArraySyntax())
      is NamingContext.RouteParam -> {
        requireNotNull(context.operationId) { "Need operationId to generate enum name" }
        ClassName(
          `package`,
          // $OuterClass$MyOperation$Param, this allows for multiple custom objects in a single
          // operation
          "${context.operationId.toPascalCase()}${context.name.toPascalCase()}".dropArraySyntax()
        )
      }
      is NamingContext.RouteBody ->
        ClassName(
          `package`,
          "${context.name.toPascalCase()}${context.postfix.toPascalCase()}".dropArraySyntax()
        )
    }

  fun toEnumValueName(rawToName: String): String {
    val pascalCase = rawToName.toPascalCase()
    return if (pascalCase.isValidClassname()) pascalCase
    else {
      val sanitise =
        pascalCase
          .run { if (startsWith("[")) drop(1) else this }
          .run { if (endsWith("]")) dropLast(1) else this }
      if (sanitise.isValidClassname()) sanitise else "`$sanitise`"
    }
  }

  private fun typeName(model: Model): String =
    when (model) {
      is Collection -> TODO("Cannot occur")
      is Model.Binary -> "Binary"
      is Model.FreeFormJson -> "JsonElement"
      is Model.Enum -> toClassName(model.context).simpleName
      is Model.Object -> toClassName(model.context).simpleName
      is Model.Union -> toClassName(model.context).simpleName
      is Model.Primitive.Boolean -> "Boolean"
      is Model.Primitive.Double -> "Double"
      is Model.Primitive.Int -> "Int"
      is Model.Primitive.String -> "String"
      Model.Primitive.Unit -> "Unit"
    }

  fun toPropName(param: Model.Object.Property): String =
    param.baseName.sanitize().dropArraySyntax().toCamelCase()

  fun toParamName(className: ClassName): String =
    className.simpleName.replaceFirstChar { it.lowercase() }

  fun toParamName(named: NamingContext.Named): String = toParamName(toClassName(named))

  // Workaround for OpenAI
  private fun String.dropArraySyntax(): String = replace("[]", "")

  fun toCaseClassName(
    union: Model.Union,
    case: Model,
    depth: List<Collection> = emptyList()
  ): ClassName =
    when (case) {
      is Collection.List -> toCaseClassName(union, case.inner.value, depth + listOf(case))
      is Collection.Map -> toCaseClassName(union, case.inner.value, depth + listOf(case))
      is Collection.Set -> toCaseClassName(union, case.inner.value, depth + listOf(case))
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

        val unionCaseName = "Case${typeName(case)}${s}$postfix"
        ClassName(`package`, toClassName(union.context).simpleNames + unionCaseName)
      }
    }
}

val ContentType = ClassName("io.ktor.http", "ContentType")

val HttpResponse = ClassName("io.ktor.client.statement", "HttpResponse")

val PrimitiveSerialDescriptor =
  MemberName("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor")

val ListSerializer = MemberName("kotlinx.serialization.builtins", "ListSerializer")

val SetSerializer = MemberName("kotlinx.serialization.builtins", "SetSerializer")

val MapSerializer = MemberName("kotlinx.serialization.builtins", "MapSerializer")

val TODO = MemberName("kotlin", "TODO")

val SerialDescriptor = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")

fun Resolved<Model>.toTypeName(): TypeName = value.toTypeName()

fun Model.toTypeName(): TypeName =
  when (this) {
    is Model.Primitive.Boolean -> BOOLEAN
    is Model.Primitive.Double -> DOUBLE
    is Model.Primitive.Int -> INT
    is Model.Primitive.String -> STRING
    Model.Primitive.Unit -> UNIT
    is Collection.List -> LIST.parameterizedBy(inner.toTypeName())
    is Collection.Set -> SET.parameterizedBy(inner.toTypeName())
    is Collection.Map -> MAP.parameterizedBy(STRING, inner.toTypeName())
    Model.Binary -> ClassName(`package`, "UploadFile")
    Model.FreeFormJson -> JsonElement::class.asTypeName()
    is Model.Enum -> Nam.toClassName(context)
    is Model.Object -> Nam.toClassName(context)
    is Model.Union -> Nam.toClassName(context)
  }
