package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import io.youtrack.model.ActivityItem
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Activities internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      categories: String? = null,
      reverse: Boolean? = null,
      start: String? = null,
      end: String? = null,
      author: String? = null,
      issueQuery: String? = null,
      fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp",
      skip: Int? = null,
      top: Int? = null,
    ): List<ActivityItem> {
      val response = client.get("activities") {
        if (categories != null) {
          this.url.encodedParameters.append("categories", categories.encodeURLParameter())
        }
        if (reverse != null) {
          this.url.encodedParameters.append("reverse", reverse.toString())
        }
        if (start != null) {
          this.url.encodedParameters.append("start", start.encodeURLParameter())
        }
        if (end != null) {
          this.url.encodedParameters.append("end", end.encodeURLParameter())
        }
        if (author != null) {
          this.url.encodedParameters.append("author", author.encodeURLParameter())
        }
        if (issueQuery != null) {
          this.url.encodedParameters.append("issueQuery", issueQuery.encodeURLParameter())
        }
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
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp"): ActivityItem {
        val response = client.get("activities/${id.encodeURLParameter()}") {
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
