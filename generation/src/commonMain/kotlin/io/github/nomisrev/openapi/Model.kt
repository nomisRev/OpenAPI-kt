package io.github.nomisrev.openapi

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

data class Route(
  val operation: Operation,
  val path: String,
  val method: HttpMethod,
  val body: Bodies,
  val input: List<Input>,
  val returnType: Returns,
  val extensions: Map<String, JsonElement>
) {
  data class Bodies(
    /** Request bodies are optional by default! */
    val required: Boolean,
    val types: Map<ContentType, Body>,
    val extensions: Map<String, JsonElement>
  ) : Map<ContentType, Body> by types {
    fun jsonOrNull(): Body.Json? = types.getOrElse(ContentType.Application.Json) { null } as? Body.Json

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
      override val extensions: Map<String, JsonElement>
    ) : Body

    sealed interface Json : Body {
      val type: Resolved<Model>

      data class FreeForm(
        override val description: String?,
        override val extensions: Map<String, JsonElement>
      ) : Json {
        override val type: Resolved<Model> = Resolved.Value(Model.FreeFormJson(description))
      }

      data class Defined(
        override val type: Resolved<Model>,
        override val description: String?,
        override val extensions: Map<String, JsonElement>
      ) : Json
    }

    data class Xml(
      val type: Model,
      override val description: String?,
      override val extensions: Map<String, JsonElement>
    ) : Body

    sealed interface Multipart : Body {
      val parameters: List<FormData>

      data class FormData(val name: String, val type: Resolved<Model>)

      data class Value(
        val parameters: List<FormData>,
        override val description: String?,
        override val extensions: Map<String, JsonElement>
      ) : Body, List<FormData> by parameters

      data class Ref(
        val value: Resolved.Ref<Model>,
        override val description: String?,
        override val extensions: Map<String, JsonElement>
      ) : Multipart {
        override val parameters: List<FormData> = listOf(FormData(value.name, value))
      }
    }
  }

  // A Parameter can be isNullable, required while the model is not!
  data class Input(
    val name: String,
    val type: Resolved<Model>,
    val isRequired: Boolean,
    val input: Parameter.Input,
    val description: String?
  )

  data class Returns(
    val types: Map<HttpStatusCode, ReturnType>,
    val extensions: Map<String, JsonElement>
  ) : Map<HttpStatusCode, ReturnType> by types

  // Required, isNullable ???
  data class ReturnType(val type: Resolved<Model>, val extensions: Map<String, JsonElement>)
}

/**
 * Allows tracking whether data was referenced by name, or defined inline. This is important to be
 * able to maintain the structure of the specification.
 */
// TODO this can be removed.
//   Move 'nested' logic to OpenAPITransformer
//   Inline `namedOr` logic where used
//   Rely on `ReferenceOr<Schema>` everywhere within `OpenAPITransformer`?
sealed interface Resolved<A> {
  val value: A

  data class Ref<A>(val name: String, override val value: A) : Resolved<A>

  @JvmInline
  value class Value<A>(override val value: A) : Resolved<A>

  fun namedOr(orElse: () -> NamingContext): NamingContext =
    when (this) {
      is Ref -> NamingContext.Named(name)
      is Value -> orElse()
    }
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

  sealed interface Primitive : Model {
    data class Int(val default: kotlin.Int?, override val description: kotlin.String?) : Primitive

    data class Double(val default: kotlin.Double?, override val description: kotlin.String?) :
      Primitive

    data class Boolean(val default: kotlin.Boolean?, override val description: kotlin.String?) :
      Primitive

    data class String(val default: kotlin.String?, override val description: kotlin.String?) :
      Primitive

    data class Unit(override val description: kotlin.String?) : Primitive

    fun default(): kotlin.String? =
      when (this) {
        is Int -> default?.toString()
        is Double -> default?.toString()
        is Boolean -> default?.toString()
        is String -> default?.let { "\"$it\"" }
        is Unit -> null
      }
  }

  data class OctetStream(override val description: String?) : Model

  data class FreeFormJson(override val description: String?) : Model

  sealed interface Collection : Model {
    val inner: Resolved<Model>

    data class List(
      override val inner: Resolved<Model>,
      val default: kotlin.collections.List<String>?,
      override val description: String?
    ) : Collection

    data class Set(
      override val inner: Resolved<Model>,
      val default: kotlin.collections.List<String>?,
      override val description: String?
    ) : Collection

    data class Map(override val inner: Resolved<Model>, override val description: String?) :
      Collection {
      val key = Primitive.String(null, null)
    }
  }

  @Serializable
  data class Object(
    val context: NamingContext,
    override val description: String?,
    val properties: List<Property>
  ) : Model {
    val inline: List<Model> =
      properties.mapNotNull {
        if (it.model is Resolved.Value)
          when (val model = it.model.value) {
            is Collection ->
              when (model.inner) {
                is Resolved.Ref -> null
                is Resolved.Value -> model.inner.value
              }

            else -> model
          }
        else null
      }

    @Serializable
    data class Property(
      val baseName: String,
      val model: Resolved<Model>,
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
    val context: NamingContext,
    val cases: List<Case>,
    val default: String?,
    override val description: String?
  ) : Model {
    val inline: List<Model> =
      cases.mapNotNull {
        if (it.model is Resolved.Value)
          when (val model = it.model.value) {
            is Collection ->
              when (model.inner) {
                is Resolved.Ref -> null
                is Resolved.Value -> model.inner.value
              }

            else -> model
          }
        else null
      }

    data class Case(val context: NamingContext, val model: Resolved<Model>)
  }

  sealed interface Enum : Model {
    val context: NamingContext
    val values: List<String>
    val default: String?
    override val description: String?

    data class Closed(
      override val context: NamingContext,
      val inner: Resolved<Model>,
      override val values: List<String>,
      override val default: String?,
      override val description: String?
    ) : Enum

    data class Open(
      override val context: NamingContext,
      override val values: List<String>,
      override val default: String?,
      override val description: String?
    ) : Enum
  }
}
