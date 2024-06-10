package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested

fun Root.toFileSpecs(): List<FileSpec> =
  endpoints() + root()

private fun Root.endpoints(): List<FileSpec> =
  endpoints.map { api ->
    FileSpec.builder("io.github.nomisrev.openapi", api.name)
      .addType(api.toTypeSpec())
//      .addType(api.toTypeSpec2())
      .build()
  }

private fun Root.root() =
  FileSpec.builder("io.github.nomisrev.openapi", "OpenAPI")
    .addType(
      TypeSpec.interfaceBuilder("OpenAPI")
        .addProperties(
          endpoints.map { api ->
            val className = Nam.toClassName(Named(api.name))
            val name = Nam.toParamName(Named(api.name))
            PropertySpec.builder(name, className).build()
          }
        )
        .build()
    )
    .addFunctions(operations.map { it.toAbstractFun() })
    .build()

private fun Route.nestedTypes(): List<TypeSpec> =
  inputs() + returns() + bodies()

private fun Route.inputs(): List<TypeSpec> =
  input.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec() }

private fun Route.returns(): List<TypeSpec> =
  returnType.types.values.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec() }

private fun Route.bodies(): List<TypeSpec> =
  body.types.values.flatMap { body ->
    when (body) {
      is Route.Body.Json.Defined ->
        listOfNotNull((body.type as? Resolved.Value)?.value?.toTypeSpec())

      is Route.Body.Multipart.Value ->
        body.parameters.mapNotNull { it.type.value.toTypeSpec() }

      is Route.Body.Multipart.Ref,
      is Route.Body.Xml,
      is Route.Body.Json.FreeForm,
      is Route.Body.OctetStream -> emptyList()
    }
  }

private fun API.toTypeSpec(outerContext: NamingContext? = null): TypeSpec {
  val outer = outerContext?.let { Nested(Named(name), it) } ?: Named(name)
  return TypeSpec.interfaceBuilder(Nam.toClassName(outer))
    .addFunctions(routes.map { it.toAbstractFun() })
    .addTypes(routes.flatMap { it.nestedTypes() })
    .addTypes(nested.map { it.toTypeSpec(outer) })
    .addProperties(nested.map {
      PropertySpec.builder(
        Nam.toParamName(Named(it.name)),
        Nam.toClassName(Nested(Named(it.name), outer))
      ).build()
    })
    .build()
}

private fun API.toTypeSpec2(): TypeSpec =
  TypeSpec.classBuilder("Ktor${Nam.toClassName(Named(name)).simpleName}")
    .addSuperinterface(Nam.toClassName(Named(name)))
    .addFunctions(routes.map { it.implemented() })
    .addTypes(nested.map { it.toTypeSpec2() })
    .build()

private fun Route.implemented(): FunSpec =
  FunSpec.builder(Nam.toParamName(Named(operation.operationId!!)))
    .addModifiers(KModifier.OVERRIDE)
    .addParameters(params(defaults = false))
    .addParameters(requestBody(defaults = false))
    .returns(returnType())
    .addCode(CodeBlock.builder().addStatement("%M()", MemberName("kotlin", "TODO")).build())
    .build()

private fun Route.toAbstractFun(): FunSpec =
  FunSpec.builder(Nam.toParamName(Named(operation.operationId!!)))
    .addModifiers(KModifier.ABSTRACT)
    .addParameters(params(defaults = true))
    .addParameters(requestBody(defaults = true))
    .returns(returnType())
    .build()

private fun Route.params(defaults: Boolean) =
  input.map { input ->
    ParameterSpec.builder(
      Nam.toParamName(Named(input.name)),
      input.type.toTypeName().copy(nullable = !input.isRequired)
    )
      .apply {
        if (defaults) {
          val default = input.type.value.default()
          when {
            default != null -> defaultValue(default)
            !input.isRequired -> defaultValue("null")
          }
        }
      }
      .build()
  }

// TODO support binary, and Xml
private fun Route.requestBody(defaults: Boolean): List<ParameterSpec> {
  fun parameter(name: String, type: Resolved<Model>, nullable: Boolean): ParameterSpec =
    ParameterSpec.builder(name, type.toTypeName().copy(nullable = nullable))
      .apply { if (defaults && nullable) defaultValue("null") }
      .build()

  return (body.jsonOrNull()?.let { json -> listOf(parameter("body", json.type, !body.required)) }
    ?: body.multipartOrNull()?.let { multipart ->
      multipart.parameters.map { parameter ->
        parameter(Nam.toParamName(Named(parameter.name)), parameter.type, !body.required)
      }
    })
    .orEmpty()
}

// TODO generate an ADT to properly support all return types
private fun Route.returnType(): TypeName {
  val success =
    returnType.types.toSortedMap { s1, s2 -> s1.code.compareTo(s2.code) }.entries.first()
  return when (success.value.type.value) {
    is Model.Binary -> ClassName("io.ktor.client.statement", "HttpResponse")
    else -> success.value.type.toTypeName()
  }
}
