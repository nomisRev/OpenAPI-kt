package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.Issue
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import kotlinx.datetime.LocalDateTime
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Issues {
    @Serializable
    enum class Direction {
        @SerialName("asc") Asc, @SerialName("desc") Desc;
    }


    @Serializable
    enum class Filter {
        @SerialName("assigned")
        Assigned,
        @SerialName("created")
        Created,
        @SerialName("mentioned")
        Mentioned,
        @SerialName("subscribed")
        Subscribed,
        @SerialName("repos")
        Repos,
        @SerialName("all")
        All;
    }


    @Serializable
    enum class Sort {
        @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("comments") Comments;
    }


    @Serializable
    enum class State {
        @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
    }

    sealed interface IssuesListResult {
        data class OK(val value: List<Issue>) : IssuesListResult

        data object NotModified : IssuesListResult

        data class NotFound(val value: BasicError) : IssuesListResult

        data class UnprocessableEntity(val value: ValidationError) : IssuesListResult
    }

    suspend fun issuesList(
        direction: Direction = Direction.Desc,
        filter: Filter = Filter.Assigned,
        page: Long = 1L,
        perPage: Long = 30L,
        sort: Sort = Sort.Created,
        state: State = State.Open,
        collab: Boolean? = null,
        labels: String? = null,
        orgs: Boolean? = null,
        owned: Boolean? = null,
        pulls: Boolean? = null,
        since: LocalDateTime? = null,
    ): IssuesListResult
}

internal class KtorIssues(private val client: HttpClient) : Issues {
    override suspend fun issuesList(direction: Issues.Direction, filter: Issues.Filter, page: Long, perPage: Long, sort: Issues.Sort, state: Issues.State, collab: Boolean?, labels: String?, orgs: Boolean?, owned: Boolean?, pulls: Boolean?, since: LocalDateTime?): Issues.IssuesListResult {
        val response = client.get("/issues") {
            parameter("direction", direction)
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
            collab?.let { parameter("collab", it) }
            labels?.let { parameter("labels", it) }
            orgs?.let { parameter("orgs", it) }
            owned?.let { parameter("owned", it) }
            pulls?.let { parameter("pulls", it) }
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Issues.IssuesListResult.OK(response.body())
            HttpStatusCode.NotModified -> Issues.IssuesListResult.NotModified
            HttpStatusCode.NotFound -> Issues.IssuesListResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Issues.IssuesListResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
