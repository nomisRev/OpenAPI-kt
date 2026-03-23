package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
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
    ): ActivityCursorPage = client.get("/activitiesPage") {
      categories?.let { parameter("categories", it) }
      reverse?.let { parameter("reverse", it) }
      start?.let { parameter("start", it) }
      end?.let { parameter("end", it) }
      author?.let { parameter("author", it) }
      issueQuery?.let { parameter("issueQuery", it) }
      cursor?.let { parameter("cursor", it) }
      activityId?.let { parameter("activityId", it) }
      fields?.let { parameter("fields", it) }
    }.body()
  }
}
