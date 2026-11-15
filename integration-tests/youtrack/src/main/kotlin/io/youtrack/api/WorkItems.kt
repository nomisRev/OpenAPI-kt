package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import io.youtrack.model.BaseWorkItemRead
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.datetime.LocalDate

public class WorkItems internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun id(id: String): IdPath = IdPath(__client, id)

  public class Get internal constructor(
    private val __client: HttpClient,
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
    ): List<BaseWorkItemRead.IssueWorkItem> {
      val response = __client.get("workItems") {
        if (query != null) {
          this.url.encodedParameters.append("query", query.encodeURLParameter())
        }
        if (startDate != null) {
          this.url.encodedParameters.append("startDate", startDate.toString().encodeURLParameter())
        }
        if (endDate != null) {
          this.url.encodedParameters.append("endDate", endDate.toString().encodeURLParameter())
        }
        if (start != null) {
          this.url.encodedParameters.append("start", start.toString())
        }
        if (end != null) {
          this.url.encodedParameters.append("end", end.toString())
        }
        if (createdStart != null) {
          this.url.encodedParameters.append("createdStart", createdStart.toString())
        }
        if (createdEnd != null) {
          this.url.encodedParameters.append("createdEnd", createdEnd.toString())
        }
        if (updatedStart != null) {
          this.url.encodedParameters.append("updatedStart", updatedStart.toString())
        }
        if (updatedEnd != null) {
          this.url.encodedParameters.append("updatedEnd", updatedEnd.toString())
        }
        if (author != null) {
          this.url.encodedParameters.append("author", author.encodeURLParameter())
        }
        if (creator != null) {
          this.url.encodedParameters.append("creator", creator.encodeURLParameter())
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
    private val __client: HttpClient,
    private val _id: String,
  ) {
    public val `get`: Get = Get(__client, _id)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,author(${'$'}type,id,login,ringId),created,creator(${'$'}type,id,login,ringId),date,duration(${'$'}type,id),id,text,updated"): BaseWorkItemRead.IssueWorkItem {
        val response = __client.get("workItems/${this._id.encodeURLParameter()}") {
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
