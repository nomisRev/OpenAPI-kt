package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.Operation
import io.github.nomisrev.openapi.test.KRoute.Body.Multipart.FormData
import kotlinx.serialization.json.JsonElement

public data class KRoute(
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
    public data class Json(public val type: KModel, override val extensions: Map<String, JsonElement>) : Body

    public data class Multipart(val parameters: List<FormData>, override val extensions: Map<String, JsonElement>) :
      Body, List<FormData> by parameters {
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

  public data class Returns(
    public val types: Map<StatusCode, ReturnType>,
    public val extensions: Map<String, JsonElement>
  ) : Map<StatusCode, ReturnType> by types

  // Required, isNullable ???
  public data class ReturnType(
    public val type: KModel,
    public val extensions: Map<String, JsonElement>
  )
}