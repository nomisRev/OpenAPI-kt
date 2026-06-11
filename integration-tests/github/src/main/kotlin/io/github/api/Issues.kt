package io.github.api

import io.github.model.BasicError
import io.github.model.Issue
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Issues internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      filter: Filter? = Filter.Assigned,
      state: State? = State.Open,
      labels: String? = null,
      sort: Sort? = Sort.Created,
      direction: Direction? = Direction.Desc,
      since: Instant? = null,
      collab: Boolean? = null,
      orgs: Boolean? = null,
      owned: Boolean? = null,
      pulls: Boolean? = null,
      perPage: Long? = 30L,
      page: Long? = 1L,
    ): Response {
      val response = client.get("/issues") {
        filter?.let { parameter("filter", it.value) }
        state?.let { parameter("state", it.value) }
        labels?.let { parameter("labels", it) }
        sort?.let { parameter("sort", it.value) }
        direction?.let { parameter("direction", it.value) }
        since?.let { parameter("since", it.toString()) }
        collab?.let { parameter("collab", it) }
        orgs?.let { parameter("orgs", it) }
        owned?.let { parameter("owned", it) }
        pulls?.let { parameter("pulls", it) }
        perPage?.let { parameter("per_page", it) }
        page?.let { parameter("page", it) }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        404 -> Response.NotFound(response.body())
        422 -> Response.UnprocessableEntity(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    public enum class Filter(
      public val `value`: String,
    ) {
      @SerialName("assigned")
      Assigned("assigned"),
      @SerialName("created")
      Created("created"),
      @SerialName("mentioned")
      Mentioned("mentioned"),
      @SerialName("subscribed")
      Subscribed("subscribed"),
      @SerialName("repos")
      Repos("repos"),
      @SerialName("all")
      All("all"),
      ;
    }

    @Serializable
    public enum class State(
      public val `value`: String,
    ) {
      @SerialName("open")
      Open("open"),
      @SerialName("closed")
      Closed("closed"),
      @SerialName("all")
      All("all"),
      ;
    }

    @Serializable
    public enum class Sort(
      public val `value`: String,
    ) {
      @SerialName("created")
      Created("created"),
      @SerialName("updated")
      Updated("updated"),
      @SerialName("comments")
      Comments("comments"),
      ;
    }

    @Serializable
    public enum class Direction(
      public val `value`: String,
    ) {
      @SerialName("asc")
      Asc("asc"),
      @SerialName("desc")
      Desc("desc"),
      ;
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<Issue>,
      ) : Response

      public data object NotModified : Response

      public data class NotFound(
        public val `value`: BasicError,
      ) : Response

      public data class UnprocessableEntity(
        public val `value`: ValidationError,
      ) : Response
    }
  }
}
