package io.github.nomisrev.openapi

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonElement

data class Route(
  val operationId: String?,
  val summary: String?,
  val path: String,
  val method: HttpMethod,
  val body: Bodies,
  val input: List<Input>,
  val returnType: Returns,
  val extensions: Map<String, JsonElement>,
  val nested: List<Model>,
) {
  data class Bodies(
    /** Request bodies are optional by default! */
    val required: Boolean,
    val types: Map<ContentType, Body>,
    val extensions: Map<String, JsonElement>,
  ) : Map<ContentType, Body> by types {
    fun jsonOrNull(): Body.Json? =
      types.getOrElse(ContentType.Application.Json) { null } as? Body.Json

    fun octetStreamOrNull(): Body.OctetStream? =
      types.getOrElse(ContentType.Application.OctetStream) { null } as? Body.OctetStream

    fun xmlOrNull(): Body.Xml? = types.getOrElse(ContentType.Application.Xml) { null } as? Body.Xml

    fun multipartOrNull(): Body.Multipart? =
      types.getOrElse(ContentType.MultiPart.FormData) { null } as? Body.Multipart
  }

  sealed interface Body {
    val description: String?
    val extensions: Map<String, JsonElement>

    data class OctetStream(
      override val description: String?,
      override val extensions: Map<String, JsonElement>,
    ) : Body

    sealed interface Json : Body {
      val type: Model

      data class FreeForm(
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Json {
        override val type: Model = Model.FreeFormJson(description, null)
      }

      data class Defined(
        override val type: Model,
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Json
    }

    data class Xml(
      val type: Model,
      override val description: String?,
      override val extensions: Map<String, JsonElement>,
    ) : Body

    sealed interface Multipart : Body {
      val parameters: List<FormData>

      data class FormData(val name: String, val type: Model)

      // Inline schemas for multipart bodies do not generate a type,
      // they should be defined as functions parameters.
      data class Value(
        override val parameters: List<FormData>,
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Multipart, List<FormData> by parameters

      // Top-level references get a top-level type.
      data class Ref(
        val name: String,
        val value: Model,
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Multipart {
        override val parameters: List<FormData> = listOf(FormData(name, value))
      }
    }
  }

  // A Parameter can be isNullable, required while the model is not!
  data class Input(
    val name: String,
    val type: Model,
    val isRequired: Boolean,
    val input: Parameter.Input,
    val description: String?,
  )

  data class Returns(
    val types: Map<HttpStatusCode, ReturnType>,
    val extensions: Map<String, JsonElement>,
  ) : Map<HttpStatusCode, ReturnType> by types {
    constructor(
      vararg types: Pair<HttpStatusCode, ReturnType>,
      extensions: Map<String, JsonElement> = emptyMap(),
    ) : this(types.toMap(), extensions)
  }

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
  val description: String?

  /**
   * Reference to a named component type. Used to preserve structure and avoid recursively expanding
   * schemas during transformation while still allowing code generation to refer to the correct
   * Kotlin type.
   */
  data class Reference(val context: NamingContext, override val description: String?) : Model

  sealed interface Primitive : Model {
    data class Int(
      val default: kotlin.Int?,
      override val description: kotlin.String?,
      val constraint: Constraints.Number?,
    ) : Primitive

    data class Double(
      val default: kotlin.Double?,
      override val description: kotlin.String?,
      val constraint: Constraints.Number?,
    ) : Primitive

    data class Boolean(val default: kotlin.Boolean?, override val description: kotlin.String?) :
      Primitive

    data class String(
      val default: kotlin.String?,
      override val description: kotlin.String?,
      val constraint: Constraints.Text?,
    ) : Primitive

    data class Unit(override val description: kotlin.String?) : Primitive

    fun default(): kotlin.String? =
      when (this) {
        is Int -> default?.toString()
        is Double -> default?.toString()
        is Boolean -> default?.toString()
        is String -> default?.let { "\"$it\"" }
        is Unit -> null
      }

    companion object
  }

  data class OctetStream(override val description: String?) : Model

  data class FreeFormJson(override val description: String?, val constraint: Constraints.Object?) :
    Model

  sealed interface Collection : Model {
    val inner: Model
    val constraint: Constraints.Collection?

    data class List(
      override val inner: Model,
      val default: kotlin.collections.List<String>?,
      override val description: String?,
      override val constraint: Constraints.Collection?,
    ) : Collection

    data class Set(
      override val inner: Model,
      val default: kotlin.collections.List<String>?,
      override val description: String?,
      override val constraint: Constraints.Collection?,
    ) : Collection

    data class Map(
      override val inner: Model,
      override val description: String?,
      override val constraint: Constraints.Collection?,
    ) : Collection {
      val key = Primitive.String(null, null, null)
    }

    companion object
  }

  data class Object(
    val context: NamingContext,
    override val description: String?,
    val properties: List<Property>,
    val inline: List<Model>,
  ) : Model {
    data class Property(
      val baseName: String,
      val model: Model,
      /**
       * isRequired != not-null. This means the value _has to be included_ in the payload, but it
       * might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?,
    )

    companion object
  }

  data class Union(
    val context: NamingContext,
    val cases: List<Case>,
    val default: String?,
    override val description: String?,
    val inline: List<Model>,
    val discriminator: Discriminator?,
  ) : Model {
    data class Case(val context: NamingContext, val model: Model)
  }

  data class Discriminator(
    val propertyName: String,
    val mapping: Map<String, String>?,
  )

  sealed interface Enum : Model {
    val context: NamingContext
    val values: List<String>
    val default: String?
    override val description: String?

    data class Closed(
      override val context: NamingContext,
      val inner: Model,
      override val values: List<String>,
      override val default: String?,
      override val description: String?,
    ) : Enum

    data class Open(
      override val context: NamingContext,
      override val values: List<String>,
      override val default: String?,
      override val description: String?,
    ) : Enum
  }

  companion object
}
