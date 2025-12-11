package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.json.JsonElement

data class Route(
  val operationId: String?,
  val summary: String?,
  val path: String,
  val method: HttpMethod,
  val body: Bodies,
  val parameters: List<Input>,
  val returns: Returns,
  val extensions: Map<String, JsonElement>,
  val nested: Set<Model>,
  val deprecated: Boolean,
) {
  data class Bodies(
    /** Request bodies are optional by default! */
    val required: Boolean,
    val types: Map<ContentType, Body>,
    val extensions: Map<String, JsonElement>,
  ) {
    fun defaultOrNull(): Body? =
      setBodyOrNull() ?: formUrlEncodedOrNull() ?: multipartOrNull()

    private fun setBodyOrNull(): Body.SetBody? =
      types.entries.firstNotNullOfOrNull { (key, value) ->
        val isDefault =
          ContentType.Application.Json.match(key) ||
            ContentType.Application.Xml.match(key) ||
            ContentType.Application.OctetStream.match(key) ||
            ContentType.Text.Plain.match(key)
        if (isDefault) value as? Body.SetBody else null
      }

    fun formUrlEncodedOrNull(): Body.FormUrlEncoded? =
      types.entries.firstNotNullOfOrNull { (key, value) ->
        if (ContentType.Application.FormUrlEncoded.match(key)) value as? Body.FormUrlEncoded
        else null
      }

    fun multipartOrNull(): Body.Multipart? =
      types.entries.firstNotNullOfOrNull { (key, value) ->
        if (ContentType.MultiPart.FormData.match(key)) value as? Body.Multipart else null
      }
  }

  sealed interface Body {
    val description: String?
    val extensions: Map<String, JsonElement>

    /**
     * Generic body sent using setBody(...). Includes JSON, XML, octet-stream and other encodings
     * that are directly supported by Ktor serialization/plugins.
     */
    data class SetBody(
      val type: Model,
      override val description: String?,
      override val extensions: Map<String, JsonElement>,
    ) : Body

    /** application/x-www-form-urlencoded body. Represented as key/value pairs. */
    data class FormUrlEncoded(
      val parameters: List<Multipart.FormData>,
      override val description: String?,
      override val extensions: Map<String, JsonElement>,
    ) : Body, List<Multipart.FormData> by parameters

    sealed interface Multipart : Body {
      data class FormData(val name: String, val type: Model)

      // Inline schemas for multipart bodies do not generate a type,
      // they should be defined as functions parameters.
      data class Value(
        val parameters: List<FormData>,
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Multipart

      // Top-level references get a top-level type.
      data class Ref(
        val value: Model,
        override val description: String?,
        override val extensions: Map<String, JsonElement>,
      ) : Multipart
    }
  }

  data class Input(
    val name: String,
    val type: Model,
    val isRequired: Boolean,
    val input: Parameter.Input,
    val description: String?,
  )

  data class Returns(
    val success: ReturnType?,
    val default: ReturnType?,
    val entries: Map<HttpStatusCode, ReturnType>,
    val extensions: Map<String, JsonElement>,
  ) {
    constructor(
      vararg types: Pair<HttpStatusCode, ReturnType>,
      extensions: Map<String, JsonElement> = emptyMap(),
    ) : this(
      success =
        types
          .asSequence()
          .map { it.first }
          .filter { it.isSuccess() }
          .sortedBy { it.value }
          .firstOrNull()
          ?.let { code -> types.toMap()[code] },
      default = null,
      entries = types.toMap(),
      extensions = extensions,
    )

    constructor(
      types: Map<HttpStatusCode, ReturnType>,
      extensions: Map<String, JsonElement>,
    ) : this(
      success =
          types.keys
              .asSequence()
              .filter { it.value in 200..299 }
              .minByOrNull { it.value }
              ?.let { types[it] },
      default = null,
      entries = types,
      extensions = extensions,
    )
  }

  // Required, isNullable ???
  data class ReturnType(val types: Map<String, Model>, val extensions: Map<String, JsonElement>)
}