package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
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
    ): List<ActivityItem> = client.get("/activities") {
      categories?.let { parameter("categories", it) }
      reverse?.let { parameter("reverse", it) }
      start?.let { parameter("start", it) }
      end?.let { parameter("end", it) }
      author?.let { parameter("author", it) }
      issueQuery?.let { parameter("issueQuery", it) }
      fields?.let { parameter("fields", it) }
      skip?.let { parameter("${'$'}skip", it) }
      top?.let { parameter("${'$'}top", it) }
    }.body()
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,added,author(${'$'}type,id,login,ringId),category(${'$'}type,id),field(${'$'}type,customField(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,name),id,removed,target,targetMember,timestamp"): ActivityItem = client.get("/activities/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }
  }
}
