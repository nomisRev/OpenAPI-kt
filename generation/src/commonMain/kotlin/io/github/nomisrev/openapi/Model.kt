package io.github.nomisrev.openapi

import kotlin.collections.Set
import kotlin.jvm.JvmInline
import kotlin.collections.List as KList
import kotlin.collections.Map as KMap
import kotlin.collections.Set as KSet

@JvmInline
public value class Models(private val models: KSet<Model>) : KSet<Model> by models

/**
 * Our own "Generated" oriented model.
 * The goal of this model is to make generation as easy as possible,
 * so we gather all information ahead of time.
 *
 * This model can/should be updated overtime to include all information we need for code generation.
 */
public sealed interface Model {
  public val typeName: String
  public fun imports(): KSet<Import>
  public fun toKotlinCode(indent: Int = 0): String

  public enum class Primitive(override val typeName: kotlin.String) : Model {
    Int("Int"), Double("Int"), Boolean("Boolean"), String("String");

    override fun imports(): KSet<Import> = emptySet()
    override fun toKotlinCode(indent: kotlin.Int): kotlin.String = typeName
  }

  public data class Import(val `package`: String, override val typeName: String) : Model {
    override fun toKotlinCode(indent: Int): String = typeName
    override fun imports(): KSet<Import> = setOf(this)
  }

  public sealed interface BuiltIns : Model {
    public data class List(val inner: Model) : BuiltIns {
      override val typeName: String = "List<${inner.typeName}>"
      override fun toKotlinCode(indent: Int): String = typeName
      override fun imports(): KSet<Import> = inner.imports()
    }

    public data class Set(val inner: Model) : BuiltIns {
      override val typeName: String = "List<${inner.typeName}>"
      override fun toKotlinCode(indent: Int): String = typeName
      override fun imports(): KSet<Import> = inner.imports()
    }

    public data class Map(val value: Model) : BuiltIns {
      public val key: Primitive = Primitive.String
      override val typeName: String = "Map<${key.typeName}, ${value.typeName}>"
      override fun toKotlinCode(indent: Int): String = typeName
      override fun imports(): KSet<Import> = value.imports()
    }

    public data object Unit : Model {
      override val typeName: String = "Unit"
      override fun imports(): KSet<Import> = setOf()
      override fun toKotlinCode(indent: Int): String = typeName
    }
  }

  public data class Product(
    override val typeName: String,
    val description: String?,
    val properties: KMap<String, Property>,
    val nestedInlineModels: KList<Model>
  ) : Model {
    public data class Property(
      val name: String,
      val type: Model,
      val isNullable: Boolean,
      val description: String?,
      val defaultValue: String?
    ) {
      public fun toKotlinCode(): String {
        val nullable = if (isNullable) "?" else ""
        val default = defaultValue?.let { " = $it" } ?: ""
        return "val $name: ${type.typeName}$nullable$default"
      }
    }

    // TODO these need to be filtered, this will have unnecessary imports
    override fun imports(): KSet<Import> =
      properties.values.flatMapToSet { it.type.imports() } +
        nestedInlineModels.flatMapToSet { it.imports() } +
        setOf(Import("kotlinx.serialization", "Serializable"))

    override fun toKotlinCode(indent: Int): String {
      val i = "  ".repeat(indent)
      val body = if (nestedInlineModels.isNotEmpty()) {
        """
        |{
        |$i${nestedInlineModels.joinToString(separator = "\n$i") { it.toKotlinCode(indent + 1) }}
        |}
        |""".trimMargin()
      } else ""

      val params =
        properties.values
          .filter { it.description != null }
          .joinToString(separator = "\n * ") { "@param ${it.name} ${it.description}" }

      val descriptions = properties.values.count { it.description != null }
      val description = when {
        description.isNullOrBlank() && descriptions <= 0 -> ""
        description.isNullOrBlank() && descriptions >= 1 -> """
        |/**
        |  * $params
        |  */
        |""".trimMargin()

        description?.isNotBlank() == true && descriptions >= 1 ->
          "/** $description */"

        else -> """
        |/**
        | * $description
        | *
        | * $params
        | */
        |""".trimMargin()
      }

      return """
      |$description@Serializable
      |data class $typeName(
      |  ${properties.values.joinToString(separator = ",\n  ") { it.toKotlinCode() }}
      |)$body
      """.trimMargin()
    }
  }

  public sealed interface Union : Model {
    public val simpleName: String
    public val schemas: KList<Model>
    public val postfix: String
    override val typeName: String
      get() = "$simpleName$postfix"

    /**
     * This represents `oneOf`, and "array types"/multiple types.
     *  - oneOf is typically used for a union of objects
     *  - [Schema.Type.Array] is used for a union of primitives,
     *      or a union of object(s) and subsections or a single properties as `String`.
     */
    public data class OneOf(
      override val simpleName: String,
      override val schemas: KList<Model>,
      override val postfix: String = ""
    ) : Union

    public data class AnyOf(
      override val simpleName: String,
      override val schemas: KList<Model>,
      override val postfix: String = ""
    ) : Union

    public data class TypeArray(
      override val simpleName: String,
      override val schemas: KList<Model>,
      override val postfix: String = ""
    ) : Union

    override fun imports(): KSet<Import> =
      schemas.flatMapToSet { it.imports() } + Import("kotlin.jvm", "JvmInline") + kotlinXSerializerImports

    override fun toKotlinCode(indent: Int): String {
      val i = "  ".repeat(indent)
      val ii = "  ".repeat(indent + 1)
      val iiiii = "  ".repeat(indent + 4)
      val iiiiii = "  ".repeat(indent + 5)
      val descriptorCases = schemas.joinToString(separator = "\n") {
        val className = it.oneOfName()
        // TODO serializer function for List, Map, etc.
        "${iiiii}element(\"Case$className\", ${it.serializer()}.descriptor)"
      }
      val deserializeCases = schemas.joinToString(separator = "\n") {
        val className = it.oneOfName()
        "${iiiiii}Pair(Case$className::class) { Case$className(Json.decodeFromJsonElement(${it.serializer()}, json)) },"
      }
      val serializeCases = schemas.joinToString(separator = "\n") {
        val className = it.oneOfName()
        "${iiiiii}is Case$className -> encoder.encodeSerializableValue(${it.serializer()}, value.value)"
      }
      val serializer = """
      |${ii}object Serializer : KSerializer<$typeName> {
      |${ii}  @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
      |${ii}  override val descriptor: SerialDescriptor =
      |${ii}    buildSerialDescriptor("$typeName", PolymorphicKind.SEALED) {
      |$descriptorCases
      |${ii}    }
      |
      |${ii}  override fun deserialize(decoder: Decoder): $typeName {
      |${ii}    val json = decoder.decodeSerializableValue(JsonElement.serializer())
      |${ii}    return attemptDeserialize(json,
      |$deserializeCases
      |${ii}    )
      |${ii}  }
      |
      |${ii}  override fun serialize(encoder: Encoder, value: $typeName) {
      |${ii}    when(value) {
      |$serializeCases
      |${ii}    }
      |${ii}  }
      |${ii}}
      """.trimMargin()
      val cases = schemas.joinToString(separator = "\n") {
        val className = it.oneOfName()
        """
        |${ii}@JvmInline
        |${ii}value class Case$className(val value: ${it.typeName}): $typeName
        """.trimMargin()
      }
      return """
      |${i}@Serializable(with = $typeName.Serializer::class)
      |${i}sealed interface $typeName {
      |$cases
      |
      |$serializer
      |$i}
      """.trimMargin()
    }
  }

  public data class Enum(
    override val typeName: String,
    val inner: Model,
    val values: KList<String>
  ) : Model {
    override fun imports(): KSet<Import> =
      inner.imports() + kotlinXSerializerImports

    override fun toKotlinCode(indent: Int): String {
      val i = "  ".repeat(indent)
      val cases = values.joinToString(separator = ",\n", postfix = ";") {
        if (it.isValidClassname()) "$i  $it"
        else "$i  @SerialName(\"$it\") `${it.sanitize()}`(\"$it\")"
      }
      val simpleName = typeName.substringAfterLast(".")
      return """
      |${i}@Serializable
      |${i}enum class $simpleName {
      |$cases
      |${i}}
      """.trimMargin()
    }
  }
}

private fun <A, B> Iterable<A>.flatMapToSet(transform: (A) -> Set<B>): Set<B> =
  flatMapTo(mutableSetOf(), transform)

private val kotlinXSerializerImports = listOf(
  Model.Import("kotlinx.serialization", "ExperimentalSerializationApi"),
  Model.Import("kotlinx.serialization", "InternalSerializationApi"),
  Model.Import("kotlinx.serialization", "KSerializer"),
  Model.Import("kotlinx.serialization", "Serializable"),
  Model.Import("kotlinx.serialization", "SerialName"),
  Model.Import("kotlinx.serialization.builtins", "serializer"),
  Model.Import("kotlinx.serialization.builtins", "ListSerializer"),
  Model.Import("kotlinx.serialization.builtins", "MapSerializer"),
  Model.Import("kotlinx.serialization.builtins", "SetSerializer"),
  Model.Import("kotlinx.serialization.descriptors", "PolymorphicKind"),
  Model.Import("kotlinx.serialization.descriptors", "SerialDescriptor"),
  Model.Import("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
  Model.Import("kotlinx.serialization.encoding", "Decoder"),
  Model.Import("kotlinx.serialization.encoding", "Encoder"),
  Model.Import("kotlinx.serialization.json", "Json"),
  Model.Import("kotlinx.serialization.json", "JsonElement"),
)

private fun Model.serializer(): String =
  when (this) {
    is Model.BuiltIns.List -> "ListSerializer(${inner.serializer()})"
    is Model.BuiltIns.Map -> "MapSerializer(${key.serializer()}, ${value.serializer()})"
    is Model.BuiltIns.Set -> "SetSerializer(${inner.serializer()})"
    else -> "$typeName.serializer()"
  }

private fun Model.oneOfName(depth: KList<Model> = emptyList()): String =
  when (this) {
    is Model.BuiltIns.List -> inner.oneOfName(depth + listOf(this))
    is Model.BuiltIns.Map -> value.oneOfName(depth + listOf(this))
    is Model.BuiltIns.Set -> inner.oneOfName(depth + listOf(this))
    else -> {
      val head = depth.firstOrNull()
      val s = when (head) {
        is Model.BuiltIns.List -> "s"
        is Model.BuiltIns.Set -> "s"
        is Model.BuiltIns.Map -> "Map"
        else -> ""
      }
      val postfix = depth.drop(1).joinToString(separator = "") {
        when (it) {
          is Model.BuiltIns.List -> "List"
          is Model.BuiltIns.Map -> "Map"
          is Model.BuiltIns.Set -> "Set"
          else -> ""
        }
      }
      val typeName = when (this) {
        is Model.BuiltIns.List -> "List"
        is Model.BuiltIns.Map -> "Map"
        is Model.BuiltIns.Set -> "Set"
        else -> typeName
      }
      "$typeName${s}$postfix"
    }
  }