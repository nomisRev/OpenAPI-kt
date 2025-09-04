package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.ir.*
import io.github.nomisrev.openapi.API
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.Route
import io.ktor.http.HttpMethod

/**
 * API generation (interfaces, factories, and Ktor implementations) using the IR model. This ports
 * generation/src/.../APIs.kt behavior into codegen without context receivers.
 */
object ApiToIr {
  data class Ctx(val pkg: String, val name: String, val registry: ModelRegistry)

  fun generate(root: Root, ctx: Ctx): List<KtFile> {
    val files = mutableListOf<KtFile>()

    // Per-API files, including nested ones (flattened names)
    for (api in root.endpoints) {
      files += generateApiFiles(api, parentNames = emptyList(), ctx = ctx)
    }

    // Root aggregator file
    files += generateRootFile(root, ctx)

    return files
  }

  private fun generateApiFiles(api: API, parentNames: List<String>, ctx: Ctx): List<KtFile> {
    val all = mutableListOf<KtFile>()

    val apiName = buildApiName(parentNames + api.name)
    val fileName = "$apiName.kt"

    val declarations = mutableListOf<KtDeclaration>()

    // Interface
    declarations += buildApiInterface(api, parentNames, ctx)

    // Factory function
    declarations += buildApiFactory(apiName)

    // Implementation class
    declarations += buildApiImpl(api, parentNames, ctx)

    // Imports required for request DSL and deserialization
    // Base imports
    val baseImports =
      mutableListOf(
        KtImport("io.ktor.client.request.request"),
        KtImport("io.ktor.http.HttpMethod"),
        KtImport("io.ktor.http.path"),
        KtImport("io.ktor.client.call.body"),
      )

    // File for this API
    all +=
      KtFile(name = fileName, pkg = ctx.pkg, imports = baseImports, declarations = declarations)

    // Nested APIs -> own files (flattened names)
    if (api.nested.isNotEmpty()) {
      for (nested in api.nested) {
        all += generateApiFiles(nested, parentNames = parentNames + api.name, ctx = ctx)
      }
    }

    return all
  }

  private fun buildApiInterface(api: API, parentNames: List<String>, ctx: Ctx): KtClass {
    val name = buildApiName(parentNames + api.name)

    val functions = api.routes.map { route -> buildRouteFunction(route, ctx, implemented = false) }

    val properties =
      api.nested.map { nested ->
        val nestedType = buildApiName(parentNames + api.name + nested.name)
        KtProperty(name = toCamelCase(nested.name), type = KtType.Simple("${ctx.pkg}.$nestedType"))
      }

    return KtClass(
      name = name,
      kind = KtClassKind.Interface,
      properties = properties,
      functions = functions,
    )
  }

  private fun buildApiFactory(apiName: String): KtFunction =
    KtFunction(
      name = apiName,
      params = listOf(KtParam("client", KtType.Simple("io.ktor.client.HttpClient"))),
      returnType = KtType.Simple(apiName),
      body = KtBlock("return ${apiName}Ktor(client)"),
    )

  private fun buildApiImpl(api: API, parentNames: List<String>, ctx: Ctx): KtClass {
    val apiName = buildApiName(parentNames + api.name)
    val implName = apiName + "Ktor"

    val ctor =
      KtPrimaryConstructor(
        params =
          listOf(KtParam("client", KtType.Simple("io.ktor.client.HttpClient"), asProperty = true))
      )

    val functions = api.routes.map { route -> buildRouteFunction(route, ctx, implemented = true) }

    val nestedProps =
      api.nested.map { nested ->
        val nestedName = buildApiName(parentNames + api.name + nested.name)
        KtProperty(
          name = toCamelCase(nested.name),
          type = KtType.Simple("${ctx.pkg}.$nestedName"),
          initializer = KtExpr("${nestedName}(client)"),
          modifiers = listOf(KtModifier.Override),
        )
      }

    return KtClass(
      name = implName,
      kind = KtClassKind.Class,
      visibility = KtVisibility.Private,
      primaryCtor = ctor,
      superTypes = listOf(KtType.Simple(apiName)),
      properties = nestedProps,
      functions = functions,
    )
  }

  private fun buildRouteFunction(route: Route, ctx: Ctx, implemented: Boolean): KtFunction {
    val params = buildParams(route, ctx, defaults = !implemented)
    val bodyParams = buildBodyParams(route, ctx, defaults = !implemented)
    val configureParam =
      KtParam(
        name = "configure",
        type =
          KtType.Function(
            params = listOf(KtType.Simple("io.ktor.client.request.HttpRequestBuilder")),
            returnType = KtType.Simple("kotlin.Unit"),
          ),
        default = if (!implemented) KtExpr("{}") else null,
      )

    val allParams = params + bodyParams + configureParam

    val returnType = computeReturnType(route, ctx)

    val modifiers =
      if (implemented) listOf(KtModifier.Suspend, KtModifier.Override)
      else listOf(KtModifier.Suspend)

    val body = if (implemented) buildRouteBody(route, returnType) else null

    return KtFunction(
      name = toFunName(route, ctx),
      params = allParams,
      returnType = returnType,
      modifiers = modifiers,
      kdoc = route.summary?.let { KtKDoc(listOf(it)) },
      body = body,
    )
  }

  private fun buildParams(route: Route, ctx: Ctx, defaults: Boolean): List<KtParam> =
    route.input.map { input ->
      val type = ctx.registry.mapType(input.type)
      val nullable = !input.isRequired
      KtParam(
        name = toCamelCase(input.name),
        type = if (nullable) type.copyNullable(true) else type,
        default = if (defaults && nullable) KtExpr("null") else null,
      )
    }

  private fun buildBodyParams(route: Route, ctx: Ctx, defaults: Boolean): List<KtParam> {
    val bodies = route.body
    bodies.jsonOrNull()?.let { json ->
      val nullable = !bodies.required
      val t = ctx.registry.mapType(json.type)
      return listOf(
        KtParam(
          name = "body",
          type = if (nullable) t.copyNullable(true) else t,
          default = if (defaults && nullable) KtExpr("null") else null,
        )
      )
    }
    bodies.multipartOrNull()?.let { multipart ->
      // Expand multipart parameters inline
      return multipart.parameters.map { p ->
        val t = ctx.registry.mapType(p.type)
        KtParam(
          name = toCamelCase(p.name),
          type = if (!bodies.required) t.copyNullable(true) else t,
          default = if (defaults && !bodies.required) KtExpr("null") else null,
        )
      }
    }
    return emptyList()
  }

  private fun computeReturnType(route: Route, ctx: Ctx): KtType {
    val entries = route.returnType.types
    if (entries.size == 1) {
      val (status, rt) = entries.entries.first()
      return when (rt.type) {
        is Model.OctetStream -> KtType.Simple("io.ktor.client.statement.HttpResponse")
        else -> ctx.registry.mapType(rt.type)
      }
    }
    // Fallback for multiple status codes: return response
    return KtType.Simple("io.ktor.client.statement.HttpResponse")
  }

  fun HttpMethod.code(): String =
    when (this) {
      HttpMethod.Get -> "HttpMethod.Get"
      HttpMethod.Post -> "HttpMethod.Post"
      HttpMethod.Put -> "HttpMethod.Put"
      HttpMethod.Delete -> "HttpMethod.Delete"
      HttpMethod.Head -> "HttpMethod.Head"
      HttpMethod.Options -> "HttpMethod.Options"
      HttpMethod.Patch -> "HttpMethod.Patch"
      else -> "HttpMethod($this)"
    }

  private fun buildRouteBody(route: Route, returnType: KtType): KtBlock {
    val sb = StringBuilder()

    sb.append("val response = client.request {\n")
    sb.append("    configure(this)\n")
    sb.append("    method = ").append(route.method.code()).append("\n")

    // URL path using Ktor path DSL
    sb.append(buildUrlPathLine(route))

    // Content-Type
    route.body.firstNotNullOfOrNull { (_, body) ->
      when (body) {
        is Route.Body.Json -> {
          sb.append("    contentType(ContentType.Application.Json)\n")
        }
        is Route.Body.Xml -> {
          // TODO: Not supported yet
        }
        is Route.Body.OctetStream -> {
          // TODO: Not supported yet
        }
        is Route.Body.Multipart -> {
          sb.append("    contentType(io.ktor.http.ContentType.MultiPart.FormData)\n")
        }
      }
    }

    // Body
    route.body.firstNotNullOfOrNull { (_, body) ->
      when (body) {
        is Route.Body.Json -> {
          sb.append("    setBody(body)\n")
        }
        is Route.Body.Xml -> {
          // TODO
        }
        is Route.Body.OctetStream -> {
          // TODO
        }
        is Route.Body.Multipart -> {
          if (body is Route.Body.Multipart.Value) {
            sb.append("    setBody(io.ktor.client.request.forms.formData {\n")
            for (p in body.parameters) {
              sb
                .append("        appendAll(\"")
                .append(p.name)
                .append("\", ")
                .append(toCamelCase(p.name))
                .append(")\n")
            }
            sb.append("    })\n")
          } else if (body is Route.Body.Multipart.Ref) {
            // Only support object ref with properties appended
            val obj = body.value as? Model.Object
            if (obj != null) {
              val vName = toCamelCase(body.name)
              sb.append("    setBody(io.ktor.client.request.forms.formData {\n")
              for (prop in obj.properties) {
                val propName = toCamelCase(prop.baseName)
                sb
                  .append("        appendAll(\"")
                  .append(propName)
                  .append("\", ")
                  .append(vName)
                  .append(".")
                  .append(propName)
                  .append(")\n")
              }
              sb.append("    })\n")
            }
          }
        }
      }
    }

    sb.append("}\n")

    val returnsHttpResponse =
      (returnType as? KtType.Simple)?.qualifiedName == "io.ktor.client.statement.HttpResponse"
    if (returnsHttpResponse) {
      sb.append("return response")
    } else {
      sb.append("return response.body()")
    }

    return KtBlock(sb.toString())
  }

  private fun buildUrlPathLine(route: Route): String {
    // If no parameters, keep the original path (including leading slash)
    if (!route.path.contains("{")) {
      return "    url { path(\"${route.path}\") }\n"
    }
    val parts = route.path.trim('/').split('/')
    val args =
      parts.joinToString(", ") { part ->
        if (part.startsWith("{") && part.endsWith("}")) {
          val name = part.substring(1, part.length - 1)
          toCamelCase(name)
        } else {
          "\"" + part + "\""
        }
      }
    return "    url { path($args) }\n"
  }

  private fun generateRootFile(root: Root, ctx: Ctx): KtFile {
    val rootName = toPascalCaseIdentifier(ctx.name)

    val properties =
      root.endpoints.map { api ->
        val apiType = buildApiName(listOf(api.name))
        KtProperty(name = toCamelCase(api.name), type = KtType.Simple("${ctx.pkg}.$apiType"))
      }

    val rootInterface =
      KtClass(
        name = rootName,
        kind = KtClassKind.Interface,
        superTypes = listOf(KtType.Simple("kotlin.AutoCloseable")),
        properties = properties,
      )

    val ctor =
      KtPrimaryConstructor(
        params =
          listOf(KtParam("client", KtType.Simple("io.ktor.client.HttpClient"), asProperty = true))
      )

    val implProps =
      root.endpoints.map { api ->
        val typeName = buildApiName(listOf(api.name))
        KtProperty(
          name = toCamelCase(api.name),
          type = KtType.Simple("${ctx.pkg}.$typeName"),
          initializer = KtExpr("${typeName}(client)"),
          modifiers = listOf(KtModifier.Override),
        )
      }

    val closeFun =
      KtFunction(
        name = "close",
        params = emptyList(),
        returnType = KtType.Simple("kotlin.Unit"),
        modifiers = listOf(KtModifier.Override),
        body = KtBlock("client.close()"),
      )

    val rootImpl =
      KtClass(
        name = rootName + "Ktor",
        kind = KtClassKind.Class,
        visibility = KtVisibility.Private,
        primaryCtor = ctor,
        superTypes = listOf(KtType.Simple(rootName), KtType.Simple("kotlin.AutoCloseable")),
        properties = implProps,
        functions = listOf(closeFun),
      )

    val factory =
      KtFunction(
        name = rootName,
        params = listOf(KtParam("client", KtType.Simple("io.ktor.client.HttpClient"))),
        returnType = KtType.Simple(rootName),
        body = KtBlock("return ${rootName}Ktor(client)"),
      )

    return KtFile(
      name = "$rootName.kt",
      pkg = ctx.pkg,
      declarations = listOf(rootInterface, factory, rootImpl),
    )
  }

  // --- helpers ---

  private fun buildApiName(segments: List<String>): String =
    segments.joinToString(separator = "") { toPascalCaseIdentifier(it) }

  private fun toCamelCase(s: String): String {
    val p = toPascalCaseIdentifier(s)
    return p.replaceFirstChar { it.lowercase() }
  }

  private fun toPascalCaseIdentifier(s: String): String {
    val parts = s.split(Regex("[^A-Za-z0-9]+")).filter { it.isNotEmpty() }
    var res = parts.joinToString("") { it.lowercase().replaceFirstChar { c -> c.uppercaseChar() } }
    if (res.isEmpty()) res = "_"
    if (res.first().isDigit()) res = "_" + res
    return res
  }

  private fun KtType.copyNullable(nullable: Boolean): KtType =
    when (this) {
      is KtType.Simple -> this.copy(nullable = nullable)
      is KtType.Generic -> this.copy(nullable = nullable)
      is KtType.Function -> this.copy(nullable = nullable)
    }

  private fun toFunName(route: Route, ctx: Ctx): String {
    val base =
      route.operationId
        ?: run {
          val lastSeg = route.path.split('/').filter { it.isNotEmpty() }.lastOrNull() ?: "op"
          val method = route.method.value.lowercase().replaceFirstChar { it.uppercase() }
          lastSeg + method
        }
    return toCamelCaseIdentifierPreserving(base)
  }

  private fun toCamelCaseIdentifierPreserving(s: String): String {
    if (s.isEmpty()) return "_"
    return if (s.any { !it.isLetterOrDigit() }) {
      val pascal = toPascalCaseIdentifier(s)
      val camel = pascal.replaceFirstChar { it.lowercase() }
      if (camel.first().isDigit()) "_" + camel else camel
    } else {
      val camel = s.replaceFirstChar { it.lowercase() }
      if (camel.first().isDigit()) "_" + camel else camel
    }
  }
}
