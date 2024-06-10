package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.NamingStrategy
import io.github.nomisrev.openapi.generation.default

fun Root.toFileSpecs(naming: NamingStrategy = DefaultNamingStrategy): List<FileSpec> =
  this.endpoints(naming) + this.root(naming)

private fun Root.endpoints(naming: NamingStrategy): List<FileSpec> =
  endpoints.map { api ->
    FileSpec.builder("io.github.nomisrev.openapi", api.name).addType(api.toTypeSpec(naming)).build()
  }

private fun Root.root(naming: NamingStrategy) =
  FileSpec.builder("io.github.nomisrev.openapi", "OpenAPI")
    .addType(
      TypeSpec.interfaceBuilder("OpenAPI")
        .apply {
          endpoints.forEach { api ->
            val className = naming.toObjectClassName(Named(api.name))
            val name = naming.toFunctionName(Named(api.name))
            addProperty(
              PropertySpec.builder(
                  name,
                  ClassName.bestGuess("io.github.nomisrev.openapi.$className")
                )
                .build()
            )
          }
        }
        .build()
    )
    .addFunctions(operations.map { it.toFun(naming).abstract() })
    .build()

private fun API.propertySpec(naming: NamingStrategy): PropertySpec {
  val className = naming.toObjectClassName(Named(name))
  return PropertySpec.builder(naming.toFunctionName(Named(name)), ClassName.bestGuess(className))
    .build()
}

private fun Route.nestedTypes(naming: NamingStrategy): List<TypeSpec> =
  inputs(naming) + returns(naming) + bodies(naming)

private fun Route.inputs(naming: NamingStrategy): List<TypeSpec> =
  input.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec(naming) }

private fun Route.returns(naming: NamingStrategy): List<TypeSpec> =
  returnType.types.values.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec(naming) }

private fun Route.bodies(naming: NamingStrategy): List<TypeSpec> =
  body.types.values.flatMap { body ->
    when (body) {
      is Route.Body.Json.Defined ->
        listOfNotNull((body.type as? Resolved.Value)?.value?.toTypeSpec(naming))
      is Route.Body.Multipart.Value ->
        body.parameters.mapNotNull { it.type.value.toTypeSpec(naming) }
      is Route.Body.Multipart.Ref,
      is Route.Body.Xml,
      is Route.Body.Json.FreeForm,
      is Route.Body.OctetStream -> emptyList()
    }
  }

private fun API.toTypeSpec(naming: NamingStrategy): TypeSpec =
  TypeSpec.interfaceBuilder(naming.toObjectClassName(Named(name)))
    .addFunctions(routes.map { it.toFun(naming).abstract() })
    .addTypes(routes.flatMap { it.nestedTypes(naming) })
    .addTypes(nested.map { it.toTypeSpec(naming) })
    .addProperties(nested.map { it.propertySpec(naming) })
    .build()

private fun FunSpec.abstract(): FunSpec = toBuilder().addModifiers(KModifier.ABSTRACT).build()

fun FunSpec.Builder.addParameter(
  naming: NamingStrategy,
  name: String,
  type: Resolved<Model>,
  nullable: Boolean,
): FunSpec.Builder =
  addParameter(
    ParameterSpec.builder(name, type.toTypeName(naming).copy(nullable = nullable))
      .apply { if (nullable) defaultValue("null") }
      .build()
  )

private fun Route.toFun(naming: NamingStrategy): FunSpec =
  FunSpec.builder(naming.toFunctionName(Named(operation.operationId!!)))
    .addParameters(
      input.map { input ->
        ParameterSpec.builder(
            naming.toFunctionName(Named(input.name)),
            input.type.toTypeName(naming).copy(nullable = !input.isRequired)
          )
          .apply {
            val default = input.type.value.default(naming)
            when {
              default != null -> defaultValue(default)
              !input.isRequired -> defaultValue("null")
            }
          }
          .build()
      }
    )
    .apply {
      // TODO support binary, and Xml
      body.jsonOrNull()?.let { json -> addParameter(naming, "body", json.type, !body.required) }
        ?: body.multipartOrNull()?.let { multipart ->
          multipart.parameters.forEach { parameter ->
            addParameter(
              naming,
              naming.toFunctionName(Named(parameter.name)),
              parameter.type,
              !body.required
            )
          }
        }
    }
    .returns(returnType(naming))
    .build()

// TODO generate an ADT to properly support all return types
private fun Route.returnType(naming: NamingStrategy): TypeName {
  val success =
    returnType.types.toSortedMap { s1, s2 -> s1.code.compareTo(s2.code) }.entries.first()
  return when (success.value.type.value) {
    is Model.Binary -> ClassName("io.ktor.client.statement", "HttpResponse")
    else -> success.value.type.toTypeName(naming)
  }
}
