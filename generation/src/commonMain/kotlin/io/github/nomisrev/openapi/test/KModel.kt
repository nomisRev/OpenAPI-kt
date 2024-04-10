package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.OpenAPI
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

private val noDefaultJson = Json {
  encodeDefaults = false
  prettyPrint = true
  prettyPrintIndent = "  "
}

private inline fun <reified A> pprint(
  value: A,
  serializer: KSerializer<A> = serializer()
): String =
  noDefaultJson.encodeToString(serializer, value)

public fun FileSystem.test(pathSpec: String) {
  val rawSpec = source(pathSpec.toPath()).buffer().use(BufferedSource::readUtf8)
  val openAPI = OpenAPI.fromJson(rawSpec)
  val toplevel = openAPI.models()
  println(toplevel.size)
  println(toplevel)
//  println(openAPI.toplevel().joinToString(separator = "\n\n"))
}

public data class TopLevel(val key: String, val model: KModel)

/**
 * Our own "Generated" oriented KModel.
 * The goal of this KModel is to make generation as easy as possible,
 * so we gather all information ahead of time.
 *
 * This KModel can/should be updated overtime to include all information we need for code generation.
 */
public sealed interface KModel {
  public fun imports(): Set<Import>

  public enum class Primitive : KModel {
    Int, Double, Boolean, String, Unit;

    override fun imports(): Set<Import> = emptySet()
  }

  public data class Import(val `package`: String, val simpleName: String) : KModel {
    override fun imports(): Set<Import> = setOf(this)
  }

  public sealed interface BuiltIns : KModel {
    public data class List(val value: KModel) : BuiltIns {
      val simpleName: String = "List"
      override fun imports(): kotlin.collections.Set<Import> = value.imports()
    }

    public data class Set(val value: KModel) : BuiltIns {
      val simpleName: String = "Set"
      override fun imports(): kotlin.collections.Set<Import> = value.imports()
    }

    public data class Map(val value: KModel) : BuiltIns {
      public val key: Primitive = Primitive.String
      val simpleName: String = "Map"
      override fun imports(): kotlin.collections.Set<Import> = value.imports()
    }
  }

  @Serializable
  public data class Object(
    val typeName: String,
    val description: String?,
    val properties: List<Property>,
    val inline: List<KModel>,
    // TODO additional imports for properties ??
  ) : KModel {
    @Serializable
    public data class Property(
      val name: String,
      val type: KModel,
      /**
       * isRequired != not-null.
       * This means the value _has to be included_ in the payload,
       * but it might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?,
      val defaultValue: String?
    )

    // TODO these need to be filtered, this will have unnecessary imports
    override fun imports(): Set<Import> =
      inline.flatMapToSet { it.imports() } +
        setOf(Import("kotlinx.serialization", "Serializable"))
  }

  // Currently doesn't deal with nested code
  // When we have nested inline schemas, they should be generated in a nested way.
  public sealed interface Union : KModel {
    public val simpleName: String
    public val schemas: List<KModel>

    public data class OneOf(
      override val simpleName: String,
      override val schemas: List<KModel>
    ) : Union

    public data class AnyOf(
      override val simpleName: String,
      override val schemas: List<KModel>
    ) : Union

    public data class TypeArray(
      override val simpleName: String,
      override val schemas: List<KModel>,
    ) : Union

    override fun imports(): Set<Import> =
      schemas.flatMapToSet { it.imports() } + Import("kotlin.jvm", "JvmInline") + kotlinXSerializerImports
  }

  public data class Enum(
    val typeName: String,
    val inner: KModel,
    val values: List<String>,
  ) : KModel {
    override fun imports(): Set<Import> =
      inner.imports() + kotlinXSerializerImports
  }
}

private fun <A, B> Iterable<A>.flatMapToSet(transform: (A) -> Set<B>): Set<B> =
  flatMapTo(mutableSetOf(), transform)

private val kotlinXSerializerImports = listOf(
  KModel.Import("kotlinx.serialization", "ExperimentalSerializationApi"),
  KModel.Import("kotlinx.serialization", "InternalSerializationApi"),
  KModel.Import("kotlinx.serialization", "KSerializer"),
  KModel.Import("kotlinx.serialization", "Serializable"),
  KModel.Import("kotlinx.serialization", "SerialName"),
  KModel.Import("kotlinx.serialization.builtins", "serializer"),
  KModel.Import("kotlinx.serialization.builtins", "ListSerializer"),
  KModel.Import("kotlinx.serialization.builtins", "MapSerializer"),
  KModel.Import("kotlinx.serialization.builtins", "SetSerializer"),
  KModel.Import("kotlinx.serialization.descriptors", "PolymorphicKind"),
  KModel.Import("kotlinx.serialization.descriptors", "SerialDescriptor"),
  KModel.Import("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
  KModel.Import("kotlinx.serialization.encoding", "Decoder"),
  KModel.Import("kotlinx.serialization.encoding", "Encoder"),
  KModel.Import("kotlinx.serialization.json", "Json"),
  KModel.Import("kotlinx.serialization.json", "JsonElement"),
)