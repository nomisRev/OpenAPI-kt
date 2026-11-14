package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import io.youtrack.model.ActivityCursorPage
import kotlin.Boolean
import kotlin.String

public class ActivitiesPage internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

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
      cursor: String? = null,
      activityId: String? = null,
      fields: String? = "${'$'}type,activities(${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp),afterCursor,beforeCursor,hasAfter,hasBefore,id",
    ): ActivityCursorPage {
      val response = client.get("activitiesPage") {
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
        if (cursor != null) {
          this.url.encodedParameters.append("cursor", cursor.encodeURLParameter())
        }
        if (activityId != null) {
          this.url.encodedParameters.append("activityId", activityId.encodeURLParameter())
        }
        if (fields != null) {
          this.url.encodedParameters.append("fields", fields.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
