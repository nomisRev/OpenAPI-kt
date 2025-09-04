package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.ir.*
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

/** Build a single KtFile containing all declarations for the provided models. */
fun Collection<Model>.toIrFile(fileName: String = "Models.kt", pkg: String? = null): KtFile {
  val registry = ModelRegistry.from(this)
  val decls = mutableListOf<KtDeclaration>()
  for (m in this) decls += m.toIrDeclarations(registry)
  return KtFile(name = fileName, pkg = pkg, declarations = decls)
}

/** Transform a top-level model into zero or more declarations. */
fun Model.toIrDeclarations(registry: ModelRegistry): List<KtDeclaration> =
  when (this) {
    is Model.Object -> {
      val result = mutableListOf<KtDeclaration>()
      // Recurse first to ensure referenced inline types exist (deterministic traversal order)
      inline.forEach { result += it.toIrDeclarations(registry) }
      result += toDataClass(registry)
      result
    }

    is Model.Enum.Closed -> listOf(toEnum())
    is Model.Enum.Open -> listOf(toEnum())
    is Model.Primitive,
    is Model.FreeFormJson,
    is Model.OctetStream,
    is Model.Collection -> listOf(toTypeAlias(this, registry))

    is Model.Reference -> emptyList() // references don't declare new types at top-level
    is Model.Union -> emptyList() // out of scope for M2
  }

private fun toTypeAlias(model: Model, registry: ModelRegistry): KtTypeAlias {
  val name = registry.nameOf(model)
  val type = registry.mapType(model)
  val kdoc = model.description?.let { KtKDoc(it.lines()) }
  return KtTypeAlias(name = name, type = type, kdoc = kdoc)
}

private fun Model.Object.toDataClass(registry: ModelRegistry): KtClass {
  val className = registry.nameOf(this)
  val params =
    properties.map { p ->
      val baseType = registry.mapType(p.model)
      val schemaHasDefault = hasSchemaDefault(p, registry)
      val defaultExpr = defaultForProperty(p, registry, className)
      val effectiveNullable = p.isNullable || (!p.isRequired && !schemaHasDefault)
      val type =
        when (baseType) {
          is KtType.Simple -> baseType.copy(nullable = effectiveNullable)
          is KtType.Generic -> baseType.copy(nullable = effectiveNullable)
          is KtType.Function -> baseType.copy(nullable = effectiveNullable)
        }
      // Add @Required when OpenAPI marks the property as required but a default is present,
      // to force presence in JSON and always encode it.
      val requiredAnn =
        if (p.isRequired && defaultExpr != null) {
          listOf(KtAnnotation(name = KtType.Simple("kotlinx.serialization.Required")))
        } else emptyList()
      KtParam(
        name = p.baseName, // emitter will escape if needed
        type = type,
        default = defaultExpr,
        annotations = requiredAnn,
        asProperty = true,
      )
    }

  return KtClass(
    name = className,
    kind = KtClassKind.Class,
    modifiers = listOf(KtModifier.Data),
    primaryCtor = KtPrimaryConstructor(params),
    annotations = listOf(KtAnnotation(name = KtType.Simple("kotlinx.serialization.Serializable"))),
    kdoc = description?.let { KtKDoc(it.lines()) },
  )
}

private fun Model.Enum.toEnum(): KtClass {
  val className =
    when (this) {
      is Model.Enum.Closed -> this.context.toFlatName()
      is Model.Enum.Open -> this.context.toFlatName()
    }

  val entries =
    values.map { value ->
      val enumName = value.toPascalCaseIdentifier()
      val needsSerialName = enumName != value
      val anns =
        if (needsSerialName) {
          listOf(
            KtAnnotation(
              name = KtType.Simple("kotlinx.serialization.SerialName"),
              args = mapOf(null to KtExpr(quote(value))),
            )
          )
        } else emptyList()
      KtEnumEntry(name = enumName, annotations = anns)
    }

  return KtClass(
    name = className,
    kind = KtClassKind.Enum,
    enumEntries = entries,
    annotations = listOf(KtAnnotation(name = KtType.Simple("kotlinx.serialization.Serializable"))),
    kdoc = description?.let { KtKDoc(it.lines()) },
  )
}

private fun defaultForProperty(
  p: Model.Object.Property,
  registry: ModelRegistry,
  ownerClassName: String,
): KtExpr? {
  // explicit defaults based on the model type
  return when (val m = p.model) {
    is Model.Primitive -> m.default()?.let { KtExpr(it) }
    is Model.Collection.List ->
      m.default?.let { vals ->
        if (vals.isEmpty()) KtExpr("emptyList()")
        else KtExpr("listOf(" + vals.joinToString(", ") { quote(it) } + ")")
      }

    is Model.Collection.Set ->
      m.default?.let { vals ->
        if (vals.isEmpty()) KtExpr("setOf()")
        else KtExpr("setOf(" + vals.joinToString(", ") { quote(it) } + ")")
      }

    is Model.Enum.Closed -> m.default?.let { enumDefaultExpr(m.context, it) }
    is Model.Enum.Open -> m.default?.let { enumDefaultExpr(m.context, it) }
    is Model.Reference -> {
      // If the reference targets an enum with a default, apply it.
      val target = registry.lookup(m.context)
      when (target) {
        is Model.Enum.Closed -> target.default?.let { enumDefaultExpr(target.context, it) }
        is Model.Enum.Open -> target.default?.let { enumDefaultExpr(target.context, it) }
        else -> null
      }
    }

    else -> null
  }
    ?: run {
      // Optional fields default to null
      if (!p.isRequired) KtExpr("null") else null
    }
}

private fun hasSchemaDefault(p: Model.Object.Property, registry: ModelRegistry): Boolean =
  when (val m = p.model) {
    is Model.Primitive.Int -> m.default != null
    is Model.Primitive.Double -> m.default != null
    is Model.Primitive.Boolean -> m.default != null
    is Model.Primitive.String -> m.default != null
    is Model.Collection.List -> m.default != null
    is Model.Collection.Set -> m.default != null
    is Model.Enum.Closed -> m.default != null
    is Model.Enum.Open -> m.default != null
    is Model.Reference -> {
      when (val target = registry.lookup(m.context)) {
        is Model.Enum.Closed -> target.default != null
        is Model.Enum.Open -> target.default != null
        else -> false
      }
    }

    else -> false
  }

private fun enumDefaultExpr(ctx: NamingContext, value: String): KtExpr {
  val typeName = ctx.toFlatName()
  val entry = value.toPascalCaseIdentifier()
  return KtExpr("$typeName.$entry")
}

/** Registry for name resolution and type mapping support. */
class ModelRegistry private constructor(private val byName: Map<String, Model>) {
  fun nameOf(model: Model): String =
    when (model) {
      is Model.Object -> model.context.toFlatName()
      is Model.Enum.Closed -> model.context.toFlatName()
      is Model.Enum.Open -> model.context.toFlatName()
      is Model.Reference -> model.context.toFlatName()
      is Model.Primitive.Int -> "Int"
      is Model.Primitive.Double -> "Double"
      is Model.Primitive.Boolean -> "Boolean"
      is Model.Primitive.String -> "String"
      is Model.Primitive.Unit -> "Unit"
      is Model.FreeFormJson -> "JsonElement"
      is Model.OctetStream -> "Binary"
      is Model.Collection.List -> nameOf(model.inner) + "List"
      is Model.Collection.Set -> nameOf(model.inner) + "Set"
      is Model.Collection.Map -> nameOf(model.inner) + "Map"
      is Model.Union -> model.context.toFlatName()
    }

  fun lookup(ctx: NamingContext): Model? = byName[ctx.toFlatName()]

  fun mapType(model: Model): KtType =
    when (model) {
      is Model.Reference -> KtType.Simple(model.context.toFlatName())
      is Model.Primitive.Int -> KtType.Simple("kotlin.Int")
      is Model.Primitive.Double -> KtType.Simple("kotlin.Double")
      is Model.Primitive.Boolean -> KtType.Simple("kotlin.Boolean")
      is Model.Primitive.String -> KtType.Simple("kotlin.String")
      is Model.Primitive.Unit -> KtType.Simple("kotlin.Unit")
      is Model.OctetStream -> KtType.Simple("kotlin.ByteArray")
      is Model.FreeFormJson -> KtType.Simple("kotlinx.serialization.json.JsonElement")
      is Model.Collection.List ->
        KtType.Generic(
          raw = KtType.Simple("kotlin.collections.List"),
          args = listOf(mapType(model.inner)),
        )

      is Model.Collection.Set ->
        KtType.Generic(
          raw = KtType.Simple("kotlin.collections.Set"),
          args = listOf(mapType(model.inner)),
        )

      is Model.Collection.Map ->
        KtType.Generic(
          raw = KtType.Simple("kotlin.collections.Map"),
          args = listOf(KtType.Simple("kotlin.String"), mapType(model.inner)),
        )

      is Model.Object -> KtType.Simple(model.context.toFlatName())
      is Model.Enum.Closed -> KtType.Simple(model.context.toFlatName())
      is Model.Enum.Open -> KtType.Simple(model.context.toFlatName())
      is Model.Union -> error("Union out of scope for M2")
    }

  companion object {
    fun from(models: Collection<Model>): ModelRegistry {
      val all = mutableListOf<Model>()
      fun collect(m: Model) {
        all += m
        if (m is Model.Object) m.inline.forEach(::collect)
      }
      models.forEach(::collect)
      val byName = buildMap {
        for (m in all) {
          val name =
            when (m) {
              is Model.Object -> m.context.toFlatName()
              is Model.Enum.Closed -> m.context.toFlatName()
              is Model.Enum.Open -> m.context.toFlatName()
              is Model.Reference -> m.context.toFlatName()
              else -> null
            }
          if (name != null) put(name, m)
        }
      }
      return ModelRegistry(byName)
    }
  }
}

// ---- Naming helpers ----

private fun NamingContext.toSegments(): List<String> =
  when (this) {
    is NamingContext.Nested -> this.outer.toSegments() + this.inner.toSegments()
    is NamingContext.Named -> listOf(this.name)
    is NamingContext.RouteParam -> listOf(this.operationId + this.postfix)
    is NamingContext.RouteBody -> listOf(this.name + this.postfix)
  }

fun NamingContext.toFlatName(): String =
  this.toSegments().joinToString(separator = "") { it.toPascalCase() }

private fun String.toPascalCase(): String {
  if (this.isEmpty()) return this
  val parts = this.split(Regex("[^A-Za-z0-9]+")).filter { it.isNotEmpty() }
  if (parts.isEmpty()) return this
  return parts.joinToString(separator = "") { it.replaceFirstChar { c -> c.uppercaseChar() } }
}

private fun String.toUpperSnakeCaseIdentifier(): String {
  val sb = StringBuilder()
  var prevWasUnderscore = false
  var prevWasLower = false
  for (ch in this) {
    when {
      ch.isLetterOrDigit() -> {
        val isUpper = ch.isUpperCase()
        if (sb.isNotEmpty() && isUpper && prevWasLower) {
          sb.append('_')
          prevWasUnderscore = true
        }
        sb.append(ch.uppercaseChar())
        prevWasUnderscore = false
        prevWasLower = ch.isLowerCase()
      }

      else -> {
        if (!prevWasUnderscore && sb.isNotEmpty()) sb.append('_')
        prevWasUnderscore = true
        prevWasLower = false
      }
    }
  }
  // trim underscores
  var res = sb.toString().trim('_')
  if (res.isEmpty()) res = "_"
  if (res.first().isDigit()) res = "_" + res
  // collapse multiple underscores
  res = res.replace(Regex("_+"), "_")
  return res
}

private fun String.toPascalCaseIdentifier(): String {
  val parts = this.split(Regex("[^A-Za-z0-9]+")).filter { it.isNotEmpty() }
  val joined =
    parts.joinToString(separator = "") { part ->
      part.lowercase().replaceFirstChar { c -> c.uppercaseChar() }
    }
  var res = joined
  if (res.isEmpty()) res = "_"
  if (res.first().isDigit()) res = "_" + res
  return res
}

private fun quote(s: String): String = buildString {
  append('"')
  for (ch in s) {
    when (ch) {
      '\\' -> append("\\\\")
      '"' -> append("\\\"")
      '\n' -> append("\\n")
      '\r' -> append("\\r")
      '\t' -> append("\\t")
      else -> append(ch)
    }
  }
  append('"')
}

/**
 * Marker for models that are emitted as typealiases but still need a name; in M2 we require callers
 * to provide the name via context only. Left here for potential future extension.
 */
private interface HasContextName {
  fun contextName(): String
}
