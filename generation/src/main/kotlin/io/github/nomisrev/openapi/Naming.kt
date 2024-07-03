package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import io.github.nomisrev.openapi.Model.Collection
import java.util.*
import net.pearx.kasechange.splitToWords
import net.pearx.kasechange.splitter.WordSplitterConfig
import net.pearx.kasechange.splitter.WordSplitterConfigurable
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toPascalCase

fun Naming(`package`: String): Naming = Nam(`package`)

context(Naming)
fun Model.Union.toCaseClassName(case: Model): ClassName = toCaseClassName(this, case)

interface Naming {
  fun toClassName(context: NamingContext): ClassName

  fun toEnumValueName(rawToName: String): String

  fun toPropName(param: Model.Object.Property): String

  fun toCaseClassName(union: Model.Union, case: Model): ClassName

  fun toParamName(named: NamingContext.Named): String
}

private class Nam(private val `package`: String) : Naming {

  // OpenAI adds '/', so this WordSplitter takes that into account
  private val wordSplitter =
    WordSplitterConfigurable(
      WordSplitterConfig(
        boundaries = setOf(' ', '-', '_', '.', '/'),
        handleCase = true,
        treatDigitsAsUppercase = true
      )
    )

  private fun String.toPascalCase(): String =
    splitToWords(wordSplitter).joinToString("") { word ->
      word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

  private fun String.toCamelCase(): String = toPascalCase().replaceFirstChar { it.lowercase() }

  override fun toClassName(context: NamingContext): ClassName =
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

  override fun toEnumValueName(rawToName: String): String {
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

  override fun toPropName(param: Model.Object.Property): String =
    param.baseName.sanitize().dropArraySyntax().toCamelCase()

  private fun toParamName(className: ClassName): String =
    className.simpleName.replaceFirstChar { it.lowercase() }

  override fun toParamName(named: NamingContext.Named): String = toParamName(toClassName(named))

  // Workaround for OpenAI
  private fun String.dropArraySyntax(): String = replace("[]", "")

  override fun toCaseClassName(union: Model.Union, case: Model): ClassName =
    toCaseClassName(union, case, emptyList())

  private fun toCaseClassName(union: Model.Union, case: Model, depth: List<Collection>): ClassName =
    when (case) {
      is Collection.List -> toCaseClassName(union, case.inner, depth + listOf(case))
      is Collection.Map -> toCaseClassName(union, case.inner, depth + listOf(case))
      is Collection.Set -> toCaseClassName(union, case.inner, depth + listOf(case))
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

        val typeName: String =
          when (case) {
            is Collection -> throw RuntimeException("Impossible path.")
            is Model.OctetStream -> "Binary"
            is Model.FreeFormJson -> "JsonElement"
            is Model.Enum -> toClassName(case.context).simpleName
            is Model.Object -> toClassName(case.context).simpleName
            is Model.Union -> toClassName(case.context).simpleName
            is Model.Primitive.Boolean -> "Boolean"
            is Model.Primitive.Double -> "Double"
            is Model.Primitive.Int -> "Int"
            is Model.Primitive.String -> "String"
            is Model.Primitive.Unit -> "Unit"
          }

        val unionCaseName = "Case$typeName${s}$postfix"
        ClassName(`package`, toClassName(union.context).simpleNames + unionCaseName)
      }
    }
}
