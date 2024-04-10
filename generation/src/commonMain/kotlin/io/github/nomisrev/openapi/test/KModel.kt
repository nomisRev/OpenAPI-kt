package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.OpenAPI
import kotlinx.serialization.Serializable
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

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
  public enum class Primitive : KModel {
    Int, Double, Boolean, String, Unit;
  }

  public data object Binary : KModel
  public data object JsonObject : KModel

  public sealed interface Collection : KModel {
    public val value: KModel

    public data class List(override val value: KModel) : Collection {
      val simpleName: String = "List"
    }

    public data class Set(override val value: KModel) : Collection {
      val simpleName: String = "Set"
    }

    public data class Map(override val value: KModel) : Collection {
      public val key: Primitive = Primitive.String
      val simpleName: String = "Map"
    }
  }

  @Serializable
  public data class Object(
    val simpleName: String,
    val description: String?,
    val properties: List<Property>,
    val inline: List<KModel>
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
  }

  // TODO Currently doesn't deal with nested code
  //   When we have nested inline schemas, they should be generated in a nested way.
  public sealed interface Union : KModel {
    // TODO, get rid of simpleName? It's a Kotlin detail.
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
  }

  public data class Enum(
    val simpleName: String,
    val inner: KModel,
    val values: List<String>,
  ) : KModel
}
