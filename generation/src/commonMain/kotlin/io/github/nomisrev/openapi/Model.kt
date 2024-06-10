package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.http.MediaType
import io.github.nomisrev.openapi.http.Method
import io.github.nomisrev.openapi.http.StatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

data class Route(
  val operation: Operation,
  val path: String,
  val method: Method,
  val body: Bodies,
  val input: List<Input>,
  val returnType: Returns,
  val extensions: Map<String, JsonElement>
) {

  data class Bodies(
    /** Request bodies are optional by default! */
    val required: Boolean,
    val types: Map<MediaType, Body>,
    val extensions: Map<String, JsonElement>
  ) : Map<MediaType, Body> by types {
    fun jsonOrNull(): Body.Json? = types.getOrElse(MediaType.ApplicationJson) { null } as? Body.Json

    fun octetStreamOrNull(): Body.OctetStream? =
      types.getOrElse(MediaType.ApplicationOctetStream) { null } as? Body.OctetStream

    fun xmlOrNull(): Body.Xml? = types.getOrElse(MediaType.ApplicationXml) { null } as? Body.Xml

    fun multipartOrNull(): Body.Multipart? =
      types.getOrElse(MediaType.MultipartFormData) { null } as? Body.Multipart
  }

  sealed interface Body {
    val extensions: Map<String, JsonElement>

    data class OctetStream(override val extensions: Map<String, JsonElement>) : Body

    data class Json(val type: Model, override val extensions: Map<String, JsonElement>) : Body

    data class Xml(val type: Model, override val extensions: Map<String, JsonElement>) : Body

    data class Multipart(
      val model: Model?,
      val parameters: List<FormData>,
      override val extensions: Map<String, JsonElement>
    ) : Body, List<Multipart.FormData> by parameters {
      data class FormData(val name: String, val type: Model)
    }
  }

  // A Parameter can be isNullable, required while the model is not!
  data class Input(
    val name: String,
    val type: Model,
    val isNullable: Boolean,
    val isRequired: Boolean,
    val input: Parameter.Input
  )

  data class Returns(
    val types: Map<StatusCode, ReturnType>,
    val extensions: Map<String, JsonElement>
  ) : Map<StatusCode, ReturnType> by types

  // Required, isNullable ???
  data class ReturnType(val type: Model, val extensions: Map<String, JsonElement>)
}

/**
 * Our own "Generated" oriented KModel. The goal of this KModel is to make generation as easy as
 * possible, so we gather all information ahead of time.
 *
 * This KModel can/should be updated overtime to include all information we need for code
 * generation.
 *
 * The naming mechanism forces the same ordering as defined in the OpenAPI Specification, this gives
 * us the best logical structure, and makes it easier to compare code and spec. Every type that
 * needs to generate a name has a [NamingContext], see [NamingContext] for more details.
 */
sealed interface Model {

  sealed interface Primitive : Model {
    data class Int(val schema: Schema, val default: kotlin.Int?) : Primitive

    data class Double(val schema: Schema, val default: kotlin.Double?) : Primitive

    data class Boolean(val schema: Schema, val default: kotlin.Boolean?) : Primitive

    data class String(val schema: Schema, val default: kotlin.String?) : Primitive

    data object Unit : Primitive

    fun default(): kotlin.String? =
      when (this) {
        is Int -> default?.toString()
        is Double -> default?.toString()
        is Boolean -> default?.toString()
        is String -> default?.let { "\"$it\"" }
        is Unit -> null
      }
  }

  data object Binary : Model

  data object FreeFormJson : Model

  sealed interface Collection : Model {
    val value: Model
    val schema: Schema

    data class List(
      override val schema: Schema,
      override val value: Model,
      val default: kotlin.collections.List<String>?
    ) : Collection

    data class Set(
      override val schema: Schema,
      override val value: Model,
      val default: kotlin.collections.List<String>?
    ) : Collection

    data class Map(override val schema: Schema, override val value: Model) : Collection {
      val key = Primitive.String(Schema(type = Schema.Type.Basic.String), null)
    }
  }

  @Serializable
  data class Object(
    val schema: Schema,
    val context: NamingContext,
    val description: String?,
    val properties: List<Property>,
    val inline: List<Model>
  ) : Model {
    @Serializable
    data class Property(
      val schema: Schema,
      val baseName: String,
      val name: String,
      val type: Model,
      /**
       * isRequired != not-null. This means the value _has to be included_ in the payload, but it
       * might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?
    )
  }

  data class Union(
    val schema: Schema,
    val context: NamingContext,
    val schemas: List<Entry>,
    val inline: List<Model>,
    val default: String?
  ) : Model {
    data class Entry(val context: NamingContext, val model: Model)
  }

  sealed interface Enum : Model {
    val schema: Schema
    val context: NamingContext
    val values: List<String>
    val default: String?

    data class Closed(
      override val schema: Schema,
      override val context: NamingContext,
      val inner: Model,
      override val values: List<String>,
      override val default: String?
    ) : Enum

    data class Open(
      override val schema: Schema,
      override val context: NamingContext,
      override val values: List<String>,
      override val default: String?
    ) : Enum
  }
}
