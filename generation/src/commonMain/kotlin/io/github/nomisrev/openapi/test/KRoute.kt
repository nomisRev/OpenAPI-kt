package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.ExternalDocs
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Operation
import io.github.nomisrev.openapi.Schema
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmInline

public enum class HttpMethod {
  Get, Put, Post, Delete, Head, Options, Trace, Patch;
}

public data class KRoute(
  val operation: Operation,
  val path: String,
  val method: HttpMethod,
  val body: Body?,
  val input: List<Input>,
  val returnType: ReturnType?,
  val extensions: Map<String, JsonElement>
) {
  // Required, isNullable
  public sealed interface Body {
    public data object OctetStream : Body
    public data class Json(public val type: KModel) : Body

    public data class Multipart(val parameters: List<FormData>) : Body {
      public data class FormData(public val name: String, public val type: KModel)
    }
  }

  // A Parameter can be isNullable, required while the model is not!
  public sealed interface Input {
    public val name: String
    public val type: KModel

    public data class Query(override val name: String, override val type: KModel) : Input
    public data class Path(override val name: String, override val type: KModel) : Input
    public data class Header(override val name: String, override val type: KModel) : Input
    public data class Cookie(override val name: String, override val type: KModel) : Input
  }

  // Required, isNullable
  @JvmInline
  public value class ReturnType(public val type: KModel)
}