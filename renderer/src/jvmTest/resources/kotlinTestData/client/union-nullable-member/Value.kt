package io.github.nomisrev.render.test.client.union.nullable.member

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Value internal constructor(
  private val __client: HttpClient,
) {
  public val put: Put = Put(__client)

  public class Put internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: RefMember?): AnyOfRefMember? {
      val response = __client.put("value") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
