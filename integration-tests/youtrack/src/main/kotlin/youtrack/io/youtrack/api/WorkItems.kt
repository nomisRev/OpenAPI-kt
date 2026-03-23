package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import io.youtrack.model.BaseWorkItemRead
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.datetime.LocalDate

public class WorkItems internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun id(id: String): IdPath = IdPath(client, id)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      query: String? = null,
      startDate: LocalDate? = null,
      endDate: LocalDate? = null,
      start: Long? = null,
      end: Long? = null,
      createdStart: Long? = null,
      createdEnd: Long? = null,
      updatedStart: Long? = null,
      updatedEnd: Long? = null,
      author: String? = null,
      creator: String? = null,
      fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated",
      skip: Int? = null,
      top: Int? = null,
    ): List<BaseWorkItemRead.IssueWorkItem> = client.get("/workItems") {
      query?.let { parameter("query", it) }
      startDate?.let { parameter("startDate", it.toString()) }
      endDate?.let { parameter("endDate", it.toString()) }
      start?.let { parameter("start", it) }
      end?.let { parameter("end", it) }
      createdStart?.let { parameter("createdStart", it) }
      createdEnd?.let { parameter("createdEnd", it) }
      updatedStart?.let { parameter("updatedStart", it) }
      updatedEnd?.let { parameter("updatedEnd", it) }
      author?.let { parameter("author", it) }
      creator?.let { parameter("creator", it) }
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
      public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated"): BaseWorkItemRead.IssueWorkItem = client.get("/workItems/$id") {
        fields?.let { parameter("fields", it) }
      }.body()
    }
  }
}
