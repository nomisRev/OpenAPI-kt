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
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      fields: String? = "${'$'}type,allUsersGroup,id,name,ringId",
      skip: Int? = null,
      top: Int? = null,
    ): List<UserGroupRead> {
      val response = __client.get("groups") {
        if (fields != null) {
          this.url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
        if (skip != null) {
          this.url.encodedParameters.append("%24skip", skip.toString())
        }
        if (top != null) {
          this.url.encodedParameters.append("%24top", top.toString())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class IdPath internal constructor(
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId"): UserGroupRead {
        val response = __client.get("groups/${this._id.encodeURLParameter()}") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
