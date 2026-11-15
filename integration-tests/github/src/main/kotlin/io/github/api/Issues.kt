package io.github.api

import io.github.model.BasicError
import io.github.model.Issue
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Issues internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public class Get internal constructor(
    private val __client: HttpClient,
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
      val response = __client.get("issues") {
        if (filter != null) {
          this.url.encodedParameters.append("filter", filter.value.encodeURLParameter())
        }
        if (state != null) {
          this.url.encodedParameters.append("state", state.value.encodeURLParameter())
        }
        if (labels != null) {
          this.url.encodedParameters.append("labels", labels.encodeURLParameter())
        }
        if (sort != null) {
          this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
        }
        if (direction != null) {
          this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
        }
        if (since != null) {
          this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
        }
        if (collab != null) {
          this.url.encodedParameters.append("collab", collab.toString())
        }
        if (orgs != null) {
          this.url.encodedParameters.append("orgs", orgs.toString())
        }
        if (owned != null) {
          this.url.encodedParameters.append("owned", owned.toString())
        }
        if (pulls != null) {
          this.url.encodedParameters.append("pulls", pulls.toString())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
        if (page != null) {
          this.url.encodedParameters.append("page", page.toString())
        }
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
