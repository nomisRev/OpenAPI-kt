package io.github.nomisrev.render.test.client.mixed.`enum`.parameter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
import kotlinx.serialization.json.JsonElement

public class Sms internal constructor(
  private val client: HttpClient,
) {
  public fun mode(mode: JsonElement): ModePath = ModePath(client, mode)

  public class ModePath internal constructor(
    private val client: HttpClient,
    private val mode: JsonElement,
  ) {
    public val post: Post = Post(client, mode)

    public class Post internal constructor(
      private val client: HttpClient,
      private val mode: JsonElement,
    ) {
      public suspend operator fun invoke(
        delivered: JsonElement,
        xDelivered: JsonElement? = null,
        deliveredCookie: JsonElement? = null,
      ) {
        val response = client.post("sms/${this.mode.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() }.encodeURLParameter()}") {
          this.url.encodedParameters.append("delivered", delivered.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() }.encodeURLParameter())
          if (xDelivered != null) {
            `header`("X-Delivered", xDelivered.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
          }
          if (deliveredCookie != null) {
            cookie("delivered-cookie", deliveredCookie.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
          }
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }

      public suspend operator fun invoke(
        delivered: JsonElement,
        xDelivered: JsonElement? = null,
        deliveredCookie: JsonElement? = null,
        flag: JsonElement,
        label: JsonElement? = null,
      ) {
        val response = client.post("sms/${this.mode.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() }.encodeURLParameter()}") {
          this.url.encodedParameters.append("delivered", delivered.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() }.encodeURLParameter())
          if (xDelivered != null) {
            `header`("X-Delivered", xDelivered.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
          }
          if (deliveredCookie != null) {
            cookie("delivered-cookie", deliveredCookie.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
          }
          contentType(ContentType.Application.FormUrlEncoded)
          setBody(Parameters.build {
            append("flag", flag.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
            if (label != null) {
              append("label", label.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.toString() })
            }
          }.formUrlEncode())
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
