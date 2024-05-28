package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.http.MediaType
import io.github.nomisrev.openapi.http.Method
import io.github.nomisrev.openapi.http.StatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

public data class Route(
  val operation: Operation,
  val path: String,
  val method: Method,
  val body: Bodies,
  val input: List<Input>,
  val returnType: Returns,
  val extensions: Map<String, JsonElement>
) {

  public data class Bodies(
    public val types: Map<MediaType, Body>,
    public val extensions: Map<String, JsonElement>
  ) : Map<MediaType, Body> by types

  // Required, isNullable
  public sealed interface Body {
    public val extensions: Map<String, JsonElement>

    public data class OctetStream(override val extensions: Map<String, JsonElement>) : Body
    public data class Json(public val type: Model, override val extensions: Map<String, JsonElement>) : Body
    public data class Xml(public val type: Model, override val extensions: Map<String, JsonElement>) : Body

    public data class Multipart(val parameters: List<FormData>, override val extensions: Map<String, JsonElement>) :
      Body, List<Multipart.FormData> by parameters {
      public data class FormData(public val name: String, public val type: Model)
    }
  }

  // A Parameter can be isNullable, required while the model is not!
  public sealed interface Input {
    public val name: String
    public val type: Model

    public data class Query(override val name: String, override val type: Model) : Input
    public data class Path(override val name: String, override val type: Model) : Input
    public data class Header(override val name: String, override val type: Model) : Input
    public data class Cookie(override val name: String, override val type: Model) : Input
  }

  public data class Returns(
    public val types: Map<StatusCode, ReturnType>,
    public val extensions: Map<String, JsonElement>
  ) : Map<StatusCode, ReturnType> by types

  // Required, isNullable ???
  public data class ReturnType(
    public val type: Model,
    public val extensions: Map<String, JsonElement>
  )
}

/**
 * Our own "Generated" oriented KModel.
 * The goal of this KModel is to make generation as easy as possible,
 * so we gather all information ahead of time.
 *
 * This KModel can/should be updated overtime to include all information we need for code generation.
 */
public sealed interface Model {
  public enum class Primitive : Model {
    Int, Double, Boolean, String, Unit;
  }

  public data object Binary : Model
  public data object FreeFormJson : Model

  public sealed interface Collection : Model {
    public val value: Model

    public data class List(override val value: Model) : Collection {
      val simpleName: String = "List"
    }

    public data class Set(override val value: Model) : Collection {
      val simpleName: String = "Set"
    }

    public data class Map(override val value: Model) : Collection {
      public val key: Primitive = Primitive.String
      val simpleName: String = "Map"
    }
  }

  @Serializable
  public data class Object(
    val context: NamingContext,
    val description: String?,
    val properties: List<Property>,
    val inline: List<Model>
  ) : Model {
    @Serializable
    public data class Property(
      val baseName: String,
      val name: String,
      val type: Model,
      /**
       * isRequired != not-null.
       * This means the value _has to be included_ in the payload,
       * but it might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?,
      /** This type is **guaranteed** to be of the same type as Model */
      val default: DefaultArgument?
    ) {
      public sealed interface DefaultArgument {
        public data class Enum(val enum: Model, val context: NamingContext, val value: String) : DefaultArgument
        public data class Union(
          val union: NamingContext,
          val case: Model,
          val value: String
        ) : DefaultArgument
        public data class Double(val value: kotlin.Double): DefaultArgument
        public data class Int(val value: kotlin.Int): DefaultArgument
        public data class List(val value: kotlin.collections.List<DefaultArgument>): DefaultArgument
        public data class Other(val value: String) : DefaultArgument
      }
    }
  }

  public sealed interface Union : Model {
    public val context: NamingContext
    public val schemas: List<UnionEntry>
    public val inline: List<Model>

    public data class UnionEntry(val context: NamingContext, val model: Model)

    public data class OneOf(
      override val context: NamingContext,
      override val schemas: List<UnionEntry>,
      override val inline: List<Model>
    ) : Union

    public data class AnyOf(
      override val context: NamingContext,
      override val schemas: List<UnionEntry>,
      override val inline: List<Model>
    ) : Union

    public data class TypeArray(
      override val context: NamingContext,
      override val schemas: List<UnionEntry>,
      override val inline: List<Model>
    ) : Union
  }

  public data class Enum(
    val context: NamingContext,
    val inner: Model,
    val values: List<String>,
  ) : Model
}
