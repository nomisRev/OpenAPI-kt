package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberSpecHolder
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpecHolder
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested
import io.ktor.http.*

fun configure(defaults: Boolean) =
  ParameterSpec(
    "configure",
    LambdaTypeName.get(
      receiver = ClassName("io.ktor.client.request", "HttpRequestBuilder"),
      returnType = Unit::class.asTypeName()
    )
  ) {
    if (defaults) defaultValue("{}")
  }

context(OpenAPIContext)
fun Root.toFileSpecs(): List<FileSpec> = apis() + root()

context(OpenAPIContext)
private fun Root.apis(): List<FileSpec> =
  endpoints
    .map { intercept(it) }
    .map { api ->
      val namingContext = Named(api.name)
      val className = toClassName(namingContext)
      FileSpec.builder(`package`, className.simpleName)
        .addType(api.toInterface())
        .addFunction(
          FunSpec.builder(className.simpleName)
            .addParameter("client", ClassName("io.ktor.client", "HttpClient"))
            .addStatement("return %T(client)", api.toImplementationClassName(namingContext))
            .returns(className)
            .build()
        )
        .addType(api.toImplementation())
        .build()
    }

context(OpenAPIContext)
private fun Root.className(): ClassName = toClassName(Named(name))

context(OpenAPIContext, FileSpec.Builder)
private fun Root.addInterface() {
  val properties =
    endpoints.map { api ->
      PropertySpec(toParamName(Named(api.name)), toClassName(Named(api.name)))
    }
  val type =
    TypeSpec.interfaceBuilder(className())
      .addSuperinterface(AutoCloseable::class)
      .addProperties(properties)
      .build()
  addType(type)
}

context(OpenAPIContext, FileSpec.Builder)
private fun Root.smartConstructor() {
  val className = className()
  val function =
    FunSpec.builder(className.simpleName)
      .addParameter("client", ClassName("io.ktor.client", "HttpClient"))
      .addStatement("return %T(client)", className.postfix("Ktor"))
      .returns(className)
      .build()
  addFunction(function)
}

context(OpenAPIContext, FileSpec.Builder)
private fun Root.implementation() {
  val properties =
    endpoints.map { api ->
      val className = toClassName(Named(api.name))
      val name = toParamName(Named(api.name))
      PropertySpec(name, className) {
        addModifiers(KModifier.OVERRIDE)
        initializer("%T(client)", className)
      }
    }
  val className = className()
  addType(
    TypeSpec.classBuilder(className.postfix("Ktor"))
      .addModifiers(KModifier.PRIVATE)
      .addSuperinterfaces(listOf(className, AutoCloseable::class.asClassName()))
      .apiConstructor()
      .addProperties(properties)
      .addFunction(
        FunSpec.builder("close")
          .addModifiers(KModifier.OVERRIDE)
          .addStatement("client.close()")
          .build()
      )
      .build()
  )
}

context(OpenAPIContext)
private fun Root.root() =
  FileSpec.builder(`package`, className().simpleName)
    .apply {
      addInterface()
      operations.forEach { it.addFunction(implemented = false) }
      smartConstructor()
      implementation()
    }
    .build()

private fun TypeSpec.Builder.apiConstructor(): TypeSpec.Builder =
  primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameter(ParameterSpec("client", ClassName("io.ktor.client", "HttpClient")))
        .build()
    )
    .addProperty(
      PropertySpec("client", ClassName("io.ktor.client", "HttpClient")) { initializer("client") }
    )

context(OpenAPIContext)
private fun API.toInterface(outerContext: NamingContext? = null): TypeSpec {
  val outer = outerContext?.let { Nested(Named(name), it) } ?: Named(name)
  val nested = nested.map { intercept(it) }
  val typeSpec =
    TypeSpec.interfaceBuilder(toClassName(outer)).apply {
      routes.forEach { route ->
        route.addFunction(implemented = false)
        addTypes(route.nested.mapNotNull { it.toTypeSpecOrNull() })
      }
      addTypes(nested.map { it.toInterface(outer) })
      val paths =
        nested.map {
          PropertySpec(toParamName(Named(it.name)), toClassName(Nested(Named(it.name), outer)))
        }
      addProperties(paths)
    }
  return modifyInterface(this, typeSpec).build()
}

context(OpenAPIContext)
private fun API.toImplementationClassName(namingContext: NamingContext): ClassName {
  val original = toClassName(namingContext)
  return ClassName(original.packageName, original.simpleNames.map { it + "Ktor" })
}

context(OpenAPIContext)
private fun API.toImplementation(context: NamingContext? = null): TypeSpec {
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

  val context = if (context != null) Nested(Named(name), context) else Named(name)
  val className = toImplementationClassName(context)
  val nested = nested.map { intercept(it) }
  val typeSpec =
    TypeSpec.classBuilder(className)
      .addModifiers(KModifier.PRIVATE)
      .addSuperinterface(toClassName(context))
      .apiConstructor()
      .apply { routes.forEach { it.addFunction(implemented = true) } }
      .addTypes(nested.map { it.toImplementation(context) })
      .addProperties(
        nested.map {
          PropertySpec(toParamName(Named(it.name)), toClassName(Nested(Named(it.name), context))) {
            addModifiers(KModifier.OVERRIDE)
            initializer(
              "%T(client)",
              toClassName(Nested(Named(it.name + "Ktor"), className.toContext()))
            )
          }
        }
      )
  return modifyImplementation(this, typeSpec).build()
}

context(OpenAPIContext, TypeSpecHolder.Builder<*>, MemberSpecHolder.Builder<*>)
private fun Route.addFunction(implemented: Boolean) {
  val function =
    FunSpec.builder(toParamName(Named(operation.operationId!!)))
      .addModifiers(KModifier.SUSPEND, if (implemented) KModifier.OVERRIDE else KModifier.ABSTRACT)
      .addParameters(params(defaults = !implemented))
      .addParameters(requestBody(defaults = !implemented))
      .addParameter(configure(defaults = !implemented))
      .apply {
        operation.summary?.let { addKdoc(it) }
        returnType()
        if (implemented) {
          addCode(
            CodeBlock.builder()
              .addStatement("val response = client.%M {", request)
              .withIndent {
                addStatement("configure()")
                addStatement("method = %T.%L", HttpMethod, method.name())
                addPathAndContent()
                addBody()
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
  addFunction(function)
}

context(OpenAPIContext, CodeBlock.Builder)
fun Route.addPathAndContent() {
  val replace =
    input
      .mapNotNull {
        if (it.input == Parameter.Input.Path)
          ".replace(\"{${it.name}}\", ${toParamName(Named(it.name))})"
        else null
      }
      .joinToString(separator = "")
  addStatement(
    "url { %M(%S$replace) }",
    MemberName("io.ktor.http", "path", isExtension = true),
    path
  )
  addContentType(body)
}

context(OpenAPIContext, CodeBlock.Builder)
fun addContentType(bodies: Route.Bodies) {
  bodies.firstNotNullOfOrNull { (_, body) ->
    when (body) {
      is Route.Body.Json ->
        addStatement("%M(%T.%L)", contentType, ContentType.nested("Application"), "Json")
      is Route.Body.Xml -> TODO("Xml input body not supported yet.")
      is Route.Body.OctetStream -> TODO("OctetStream  input body not supported yet.")
      is Route.Body.Multipart.Ref,
      is Route.Body.Multipart.Value ->
        addStatement("%M(%T.%L)", contentType, ContentType.nested("MultiPart"), "FormData")
    }
  }
}

context(OpenAPIContext, CodeBlock.Builder)
fun Route.addBody() {
  body.firstNotNullOfOrNull { (_, body) ->
    when (body) {
      is Route.Body.Json -> addStatement("%M(%L)", setBody, "body")
      is Route.Body.Xml -> TODO("Xml input body not supported yet.")
      is Route.Body.OctetStream -> TODO("OctetStream  input body not supported yet.")
      is Route.Body.Multipart -> {
        addStatement("%M(", setBody)
        withIndent {
          addStatement("%M {", formData)
          withIndent {
            when (body) {
              is Route.Body.Multipart.Value ->
                body.parameters.map { addStatement("appendAll(%S, %L)", it.name, it.name) }
              is Route.Body.Multipart.Ref -> {
                val obj =
                  requireNotNull(body.value as? Model.Object) {
                    "Only supports objects for FreeForm Multipart"
                  }
                val name = toParamName(Named(body.name))
                obj.properties.forEach { prop ->
                  addStatement("appendAll(%S, $name.%L)", toPropName(prop), toPropName(prop))
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
}

context(OpenAPIContext)
fun Route.params(defaults: Boolean): List<ParameterSpec> =
  input.map { input ->
    ParameterSpec(
      toParamName(Named(input.name)),
      input.type.toTypeName().copy(nullable = !input.isRequired)
    ) {
      input.description?.let { addKdoc(it) }
      if (defaults) {
        defaultValue(input.type)
        if (!input.isRequired && !input.type.hasDefault()) {
          defaultValue("null")
        }
      }
    }
  }

// TODO support binary, and Xml
context(OpenAPIContext)
fun Route.requestBody(defaults: Boolean): List<ParameterSpec> {
  fun parameter(name: String, type: Model, nullable: Boolean, description: String?): ParameterSpec =
    ParameterSpec(name, type.toTypeName().copy(nullable = nullable)) {
      description(description)
      if (defaults && nullable) defaultValue("null")
    }

  return (body.jsonOrNull()?.let { json ->
      listOf(parameter("body", json.type, !body.required, json.description))
    }
      ?: body.multipartOrNull()?.let { multipart ->
        multipart.parameters.map { parameter ->
          parameter(
            toParamName(Named(parameter.name)),
            parameter.type,
            !body.required,
            parameter.type.description
          )
        }
      })
    .orEmpty()
}

context(OpenAPIContext, FunSpec.Builder, TypeSpecHolder.Builder<*>)
fun Route.returnType() {
  if (returnType.types.size == 1) {
    val single = returnType.types.entries.single()
    val typeName =
      when (single.value.type) {
        is Model.OctetStream -> HttpResponse
        else -> single.value.type.toTypeName()
      }
    returns(typeName)
  } else {
    val response = ClassName(`package`, "${operation.operationId}Response")
    addType(
      TypeSpec.interfaceBuilder(response)
        .addModifiers(KModifier.SEALED)
        .addTypes(
          returnType.types.map { (status: HttpStatusCode, type) ->
            val case = ClassName(`package`, status.description.split(" ").joinToString(""))
            TypeSpec.dataClass(case, listOf(ParameterSpec("value", type.type.toTypeName())))
          }
        )
        .build()
    )
    returns(response)
  }
}
