package io.github.nomisrev.render.test.client.discriminated.union.nullable

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

public class Tagged internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: TaggedNullableMemberPet?): TaggedNullableMemberPet? {
      val response = client.put("tagged") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
