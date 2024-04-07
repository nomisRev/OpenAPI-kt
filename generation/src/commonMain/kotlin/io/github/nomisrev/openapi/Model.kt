package io.github.nomisrev.openapi

import kotlin.collections.Set
import kotlin.jvm.JvmInline
import kotlin.collections.List as KList
import kotlin.collections.Set as KSet
import kotlin.collections.Map as KMap

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

  public data class List(val inner: Model) : Model {
    override val typeName: String = "List<${inner.typeName}>"
    override fun toKotlinCode(indent: Int): String = typeName
    override fun imports(): Set<Import> = inner.imports()
  }

  public data class Map(val value: Model) : Model {
    public val key: Primitive = Primitive.String
    override val typeName: String = "Map<${key.typeName}, ${value.typeName}>"
    override fun toKotlinCode(indent: Int): String = typeName
    override fun imports(): Set<Import> = value.imports()
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
    override fun imports(): Set<Import> =
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

  sealed interface Union : Model {
    val simpleName: String
    val schemas: KList<Model>
    val postfix: String
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
      override val postfix: String = "OneOf"
    ) : Union

    public data class AnyOf(
      override val simpleName: String,
      override val schemas: KList<Model>,
      override val postfix: String = "AnyOf"
    ) : Union

    public data class TypeArray(
      override val simpleName: String,
      override val schemas: KList<Model>,
      override val postfix: String = "Type"
    ) : Union

    override fun imports(): Set<Import> =
      schemas.flatMapToSet { it.imports() } + Import("kotlin.jvm", "JvmInline") + kotlinXSerializerImports

    override fun toKotlinCode(indent: Int): String {
      val i = "  ".repeat(indent)
      val ii = "  ".repeat(indent + 1)
      val iiiii = "  ".repeat(indent + 4)
      val iiiiii = "  ".repeat(indent + 5)
      val descriptorCases = schemas.joinToString(separator = "\n") {
        // TODO serializer function for List, Map, etc.
        "${iiiii}element(\"Case${it.typeName}\", ${it.typeName}.serializer().descriptor)"
      }
      val deserializeCases = schemas.joinToString(separator = "\n") {
        "${iiiiii}Pair(Case${it.typeName}::class) { Case${it.typeName}(Json.decodeFromJsonElement(${it.typeName}.serializer(), json)) },"
      }
      val serializeCases = schemas.joinToString(separator = "\n") {
        "${iiiiii}is Case${it.typeName} -> encoder.encodeSerializableValue(${it.typeName}.serializer(), value.value)"
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
        """
        |${ii}@JvmInline
        |${ii}value class Case${it.typeName}(val value: ${it.typeName}): $typeName
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
    override fun imports(): Set<Import> = inner.imports()
    override fun toKotlinCode(indent: Int): String {
      val cases = values.joinToString(postfix = ";")
      val i = "  ".repeat(indent)
      return """
      |${i}enum class $typeName {
      |${i}  $cases
      |${i}}
      """.trimMargin()
    }
  }

  public data object Unit : Model {
    override val typeName: String = "Unit"
    override fun imports(): Set<Import> = setOf()
    override fun toKotlinCode(indent: Int): String = typeName
  }
}

private fun <A, B> Iterable<A>.flatMapToSet(transform: (A) -> Set<B>): Set<B> =
  flatMapTo(mutableSetOf(), transform)

private val kotlinXSerializerImports = listOf(
  Model.Import("kotlinx.serialization", "ExperimentalSerializationApi"),
  Model.Import("kotlinx.serialization", "InternalSerializationApi"),
  Model.Import("kotlinx.serialization", "KSerializer"),
  Model.Import("kotlinx.serialization", "Serializable"),
  Model.Import("kotlinx.serialization.builtins", "serializer"),
  Model.Import("kotlinx.serialization.descriptors", "PolymorphicKind"),
  Model.Import("kotlinx.serialization.descriptors", "SerialDescriptor"),
  Model.Import("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
  Model.Import("kotlinx.serialization.encoding", "Decoder"),
  Model.Import("kotlinx.serialization.encoding", "Encoder"),
  Model.Import("kotlinx.serialization.json", "Json"),
  Model.Import("kotlinx.serialization.json", "JsonElement"),
)
