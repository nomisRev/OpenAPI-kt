package io.github.nomisrev.render.test.client.untyped.parameter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.json.JsonPrimitive

public class Lookup internal constructor(
  private val __client: HttpClient,
) {
  public fun key(key: JsonPrimitive): KeyPath = KeyPath(__client, key)

  public class KeyPath internal constructor(
    private val __client: HttpClient,
    private val _key: JsonPrimitive,
  ) {
    public val `get`: Get = Get(__client, _key)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _key: JsonPrimitive,
    ) {
      public suspend operator fun invoke(
        required: JsonPrimitive,
        any: JsonPrimitive? = null,
        ids: List<JsonPrimitive>? = null,
        tags: List<JsonPrimitive>? = null,
        addOns: Map<String, JsonPrimitive>? = null,
        flat: Map<String, JsonPrimitive>? = null,
        xAny: JsonPrimitive? = null,
        xIds: List<JsonPrimitive>? = null,
        anyCookie: JsonPrimitive? = null,
      ) {
        val response = __client.get("lookup/${this._key.content.encodeURLParameter()}") {
          if (any != null) {
            this.url.encodedParameters.append("any", any.content.encodeURLParameter())
          }
          this.url.encodedParameters.append("required", required.content.encodeURLParameter())
          if (!ids.isNullOrEmpty()) {
            this.url.encodedParameters.append("ids", ids.joinToString(",") { it.content.encodeURLParameter() })
          }
          tags?.forEach { this.url.encodedParameters.append("tags", it.content.encodeURLParameter()) }
          addOns?.forEach { this.url.encodedParameters.append(it.key.encodeURLParameter(), it.value.content.encodeURLParameter()) }
          if (!flat.isNullOrEmpty()) {
            this.url.encodedParameters.append("Flat", flat.entries.joinToString(",") { "${it.key.encodeURLParameter()},${it.value.content.encodeURLParameter()}" })
          }
          if (xAny != null) {
            `header`("X-Any", xAny.content)
          }
          if (!xIds.isNullOrEmpty()) {
            `header`("X-Ids", xIds.joinToString(",") { it.content })
          }
          if (anyCookie != null) {
            cookie("any-cookie", anyCookie.content)
          }
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
