package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested

private fun configure(defaults: Boolean) =
  ParameterSpec.builder(
      "configure",
      LambdaTypeName.get(
        receiver = ClassName("io.ktor.client.request", "HttpRequestBuilder"),
        returnType = Unit::class.asTypeName()
      )
    )
    .apply { if (defaults) defaultValue("{}") }
    .build()

fun Root.toFileSpecs(): List<FileSpec> = endpoints() + root()

private fun Root.endpoints(): List<FileSpec> =
  endpoints.map { api ->
    val className = Nam.toClassName(Named(api.name))
    FileSpec.builder("io.github.nomisrev.openapi", className.simpleName)
      .addType(api.toInterface())
      // We need to add a fun Chat(client: HttpClient) for private class ChatKtor(client:
      // HttpClient)
      .addFunction(
        FunSpec.builder(className.simpleName)
          .addParameter("client", ClassName("io.ktor.client", "HttpClient"))
          .addStatement("return %T(client)", className)
          .returns(className)
          .build()
      )
      .addType(api.toImplementation())
      .build()
  }

private fun Root.root() =
  FileSpec.builder("io.github.nomisrev.openapi", "OpenAPI")
    .addType(
      TypeSpec.interfaceBuilder("OpenAPI")
        .addProperties(
          endpoints.map { api ->
            PropertySpec.builder(Nam.toParamName(Named(api.name)), Nam.toClassName(Named(api.name)))
              .build()
          }
        )
        .build()
    )
    .addFunctions(operations.map { it.toFun(implemented = false) })
    .addFunction(
      FunSpec.builder("OpenAPI")
        .addParameter("client", ClassName("io.ktor.client", "HttpClient"))
        .addStatement("return %T(client)", ClassName("io.github.nomisrev.openapi", "OpenAPIKtor"))
        .returns(ClassName("io.github.nomisrev.openapi", "OpenAPI"))
        .build()
    )
    .addType(
      TypeSpec.classBuilder(ClassName("io.github.nomisrev.openapi", "OpenAPIKtor"))
        .addModifiers(KModifier.PRIVATE)
        .addSuperinterface(ClassName("io.github.nomisrev.openapi", "OpenAPI"))
        .apiConstructor()
        .addProperties(
          endpoints.map { api ->
            val className = Nam.toClassName(Named(api.name))
            val name = Nam.toParamName(Named(api.name))
            PropertySpec.builder(name, className)
              .addModifiers(KModifier.OVERRIDE)
              .initializer("%T(client)", className)
              .build()
          }
        )
        .build()
    )
    .build()

private fun TypeSpec.Builder.apiConstructor(): TypeSpec.Builder =
  primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameter(ParameterSpec("client", ClassName("io.ktor.client", "HttpClient")))
        .build()
    )
    .addProperty(
      PropertySpec.builder("client", ClassName("io.ktor.client", "HttpClient"))
        .initializer("client")
        .build()
    )

private fun Route.nestedTypes(): List<TypeSpec> = inputs() + returns() + bodies()

private fun Route.inputs(): List<TypeSpec> =
  input.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec() }

private fun Route.returns(): List<TypeSpec> =
  returnType.types.values.mapNotNull { (it.type as? Resolved.Value)?.value?.toTypeSpec() }

private fun Route.bodies(): List<TypeSpec> =
  body.types.values.flatMap { body ->
    when (body) {
      is Route.Body.Json.Defined ->
        listOfNotNull((body.type as? Resolved.Value)?.value?.toTypeSpec())
      is Route.Body.Multipart.Value -> body.parameters.mapNotNull { it.type.value.toTypeSpec() }
      is Route.Body.Multipart.Ref,
      is Route.Body.Xml,
      is Route.Body.Json.FreeForm,
      is Route.Body.OctetStream -> emptyList()
    }
  }

private fun API.toInterface(outerContext: NamingContext? = null): TypeSpec {
  val outer = outerContext?.let { Nested(Named(name), it) } ?: Named(name)
  return TypeSpec.interfaceBuilder(Nam.toClassName(outer))
    .addFunctions(routes.map { it.toFun(implemented = false) })
    .addTypes(routes.flatMap { it.nestedTypes() })
    .addTypes(nested.map { it.toInterface(outer) })
    .addProperties(
      nested.map {
        PropertySpec.builder(
            Nam.toParamName(Named(it.name)),
            Nam.toClassName(Nested(Named(it.name), outer))
          )
          .build()
      }
    )
    .build()
}

private fun API.toImplementation(outerContext: NamingContext? = null): TypeSpec {
  fun ClassName.postfix(postfix: String): ClassName =
    ClassName(packageName, simpleNames.map { it + postfix })

  fun ClassName.toContext(): NamingContext {
    val names = simpleNames
    return when (names.size) {
      1 -> Named(names[0])
      else ->
        names.drop(1).fold<String, NamingContext>(Named(names[0])) { acc, part ->
          Nested(Named(part), acc)
        }
    }
  }

  val outer = outerContext?.let { Nested(Named(name), it) } ?: Named(name)
  val className = Nam.toClassName(outer).postfix("Ktor")
  return TypeSpec.classBuilder(className)
    .addModifiers(KModifier.PRIVATE)
    .addSuperinterface(Nam.toClassName(outer))
    .apiConstructor()
    .addFunctions(routes.map { it.toFun(implemented = true) })
    .addTypes(nested.map { it.toImplementation(outer) })
    .addProperties(
      nested.map {
        PropertySpec.builder(
            Nam.toParamName(Named(it.name)),
            Nam.toClassName(Nested(Named(it.name), outer))
          )
          .addModifiers(KModifier.OVERRIDE)
          .initializer(
            "%T(client)",
            Nam.toClassName(Nested(Named(it.name + "Ktor"), className.toContext()))
          )
          .build()
      }
    )
    .build()
}

private fun Route.toFun(implemented: Boolean): FunSpec =
  FunSpec.builder(Nam.toParamName(Named(operation.operationId!!)))
    .apply { operation.summary?.let { addKdoc(it) } }
    .addModifiers(KModifier.SUSPEND, if (implemented) KModifier.OVERRIDE else KModifier.ABSTRACT)
    .addParameters(params(defaults = !implemented))
    .addParameters(requestBody(defaults = !implemented))
    .addParameter(configure(defaults = !implemented))
    .returns(returnType())
    .apply {
      if (implemented) {
        addCode(
          CodeBlock.builder()
            .addStatement(
              "val response = client.%M {",
              MemberName("io.ktor.client.request", "request", isExtension = true)
            )
            .withIndent {
              addStatement("configure()")
              addStatement("method = %T.%L", ClassName("io.ktor.http", "HttpMethod"), method.value)
              val replace =
                input
                  .mapNotNull {
                    if (it.input == Parameter.Input.Path)
                      ".replace(\"{${it.name}}\", ${Nam.toParamName(Named(it.name))})"
                    else null
                  }
                  .joinToString(separator = "")
              addStatement(
                "url { %M(%S$replace) }",
                MemberName("io.ktor.http", "path", isExtension = true),
                path
              )
              addBody(body)
            }
            .addStatement("}")
            .addStatement(
              "return response.%M()",
              MemberName("io.ktor.client.call", "body", isExtension = true)
            )
            .build()
        )
      }
    }
    .build()

// For now, we just try to find a JSON content type
fun CodeBlock.Builder.addBody(bodies: Route.Bodies): CodeBlock.Builder =
  bodies.firstNotNullOfOrNull { (_, body) ->
    when (body) {
      is Route.Body.Json -> {
        addStatement(
          "%M(%T.%L)",
          MemberName("io.ktor.http", "contentType"),
          ClassName("io.ktor.http", "ContentType", "Application"),
          "Json"
        )
        addStatement(
          "%M(%L)",
          MemberName("io.ktor.client.request", "setBody", isExtension = true),
          "body"
        )
      }
      is Route.Body.Xml -> TODO("Xml input body not supported yet.")
      is Route.Body.OctetStream -> TODO("OctetStream  input body not supported yet.")
      is Route.Body.Multipart.Ref,
      is Route.Body.Multipart.Value -> {
        body as Route.Body.Multipart
        addStatement(
          "%M(%T.%L)",
          MemberName("io.ktor.http", "contentType"),
          ClassName("io.ktor.http", "ContentType", "MultiPart"),
          "FormData"
        )
        addStatement("%M(", MemberName("io.ktor.client.request", "setBody", isExtension = true))
        withIndent {
          addStatement(
            "%M {",
            MemberName("io.ktor.client.request.forms", "formData", isExtension = true)
          )
          withIndent {
            when (body) {
              is Route.Body.Multipart.Value ->
                body.parameters.map { addStatement("appendAll(%S, %L)", it.name, it.name) }
              is Route.Body.Multipart.Ref -> {
                val obj =
                  requireNotNull(body.value.value as? Model.Object) {
                    "Only supports objects for FreeForm Multipart"
                  }
                val name = Nam.toParamName(Named(body.value.name))
                obj.properties.forEach { prop ->
                  addStatement(
                    "appendAll(%S, $name.%L)",
                    Nam.toPropName(prop),
                    Nam.toPropName(prop)
                  )
                }
              }
            }
          }
          addStatement("}")
        }
        addStatement(")")
      }
    }
  }
    ?: this

private fun Route.params(defaults: Boolean): List<ParameterSpec> =
  input.map { input ->
    ParameterSpec.builder(
        Nam.toParamName(Named(input.name)),
        input.type.toTypeName().copy(nullable = !input.isRequired)
      )
      .apply {
        input.description?.let { addKdoc(it) }
        if (defaults) {
          defaultValue(input.type.value)
          if (!input.isRequired && !input.type.value.hasDefault()) {
            defaultValue("null")
          }
        }
      }
      .build()
  }

// TODO support binary, and Xml
private fun Route.requestBody(defaults: Boolean): List<ParameterSpec> {
  fun parameter(
    name: String,
    type: Resolved<Model>,
    nullable: Boolean,
    description: String?
  ): ParameterSpec =
    ParameterSpec.builder(name, type.toTypeName().copy(nullable = nullable))
      .apply { if (defaults && nullable) defaultValue("null") }
      .build()

  return (body.jsonOrNull()?.let { json ->
      listOf(parameter("body", json.type, !body.required, json.description))
    }
      ?: body.multipartOrNull()?.let { multipart ->
        multipart.parameters.map { parameter ->
          parameter(
            Nam.toParamName(Named(parameter.name)),
            parameter.type,
            !body.required,
            parameter.type.value.description
          )
        }
      })
    .orEmpty()
}

// TODO generate an ADT to properly support all return types
private fun Route.returnType(): TypeName {
  val success =
    returnType.types.toSortedMap { s1, s2 -> s1.code.compareTo(s2.code) }.entries.first()
  return when (success.value.type.value) {
    is Model.OctetStream -> HttpResponse
    else -> success.value.type.toTypeName()
  }
}
