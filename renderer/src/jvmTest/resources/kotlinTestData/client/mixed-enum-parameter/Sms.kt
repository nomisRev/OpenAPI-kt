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
import kotlinx.serialization.json.JsonPrimitive

public class Sms internal constructor(
  private val client: HttpClient,
) {
  public fun mode(mode: JsonPrimitive): ModePath = ModePath(client, mode)

  public class ModePath internal constructor(
    private val client: HttpClient,
    private val mode: JsonPrimitive,
  ) {
    public val post: Post = Post(client, mode)

    public class Post internal constructor(
      private val client: HttpClient,
      private val mode: JsonPrimitive,
    ) {
      public suspend operator fun invoke(
        delivered: JsonPrimitive,
        xDelivered: JsonPrimitive? = null,
        deliveredCookie: JsonPrimitive? = null,
      ) {
        val response = client.post("sms/${this.mode.content.encodeURLParameter()}") {
          this.url.encodedParameters.append("delivered", delivered.content.encodeURLParameter())
          if (xDelivered != null) {
            `header`("X-Delivered", xDelivered.content)
          }
          if (deliveredCookie != null) {
            cookie("delivered-cookie", deliveredCookie.content)
          }
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }

      public suspend operator fun invoke(
        delivered: JsonPrimitive,
        xDelivered: JsonPrimitive? = null,
        deliveredCookie: JsonPrimitive? = null,
        flag: JsonPrimitive,
        label: JsonPrimitive? = null,
      ) {
        val response = client.post("sms/${this.mode.content.encodeURLParameter()}") {
          this.url.encodedParameters.append("delivered", delivered.content.encodeURLParameter())
          if (xDelivered != null) {
            `header`("X-Delivered", xDelivered.content)
          }
          if (deliveredCookie != null) {
            cookie("delivered-cookie", deliveredCookie.content)
          }
          contentType(ContentType.Application.FormUrlEncoded)
          setBody(Parameters.build {
            append("flag", flag.content)
            if (label != null) {
              append("label", label.content)
            }
          }.formUrlEncode())
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
