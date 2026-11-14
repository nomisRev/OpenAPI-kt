package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import io.youtrack.model.UserGroupRead
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Groups internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,allUsersGroup,id,name,ringId",
      skip: Int? = null,
      top: Int? = null,
    ): List<UserGroupRead> {
      val response = client.get("groups") {
        if (fields != null) {
          url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
        if (skip != null) {
          url.encodedParameters.append("%24skip", skip.toString())
        }
        if (top != null) {
          url.encodedParameters.append("%24top", top.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId"): UserGroupRead {
        val response = client.get("groups/${id.encodeURLParameter()}") {
          if (fields != null) {
            url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
