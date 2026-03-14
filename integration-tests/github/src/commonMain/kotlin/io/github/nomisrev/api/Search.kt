package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.CodeSearchResultItem
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.CommitSearchResultItem
import io.github.nomisrev.model.IssueSearchResultItem
import io.github.nomisrev.model.LabelSearchResultItem
import io.github.nomisrev.model.RepoSearchResultItem
import io.github.nomisrev.model.TopicSearchResultItem
import io.github.nomisrev.model.UserSearchResultItem
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Search {
    val code: Search.Code

    val commits: Search.Commits

    val issues: Search.Issues

    val labels: Search.Labels

    val repositories: Search.Repositories

    val topics: Search.Topics

    val users: Search.Users

    interface Code {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchCodeResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<CodeSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("indexed") Indexed;
        }

        sealed interface SearchCodeResult {
            data class OK(val value: SearchCodeResponse) : SearchCodeResult

            data object NotModified : SearchCodeResult

            data class Forbidden(val value: BasicError) : SearchCodeResult

            data class UnprocessableEntity(val value: ValidationError) : SearchCodeResult

            data class ServiceUnavailable(val value: SearchCodeResponse) : SearchCodeResult
        }

        suspend fun searchCode(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            sort: Sort? = null,
        ): SearchCodeResult
    }

    interface Commits {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchCommitsResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<CommitSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("author-date") AuthorDate, @SerialName("committer-date") CommitterDate;
        }

        sealed interface SearchCommitsResult {
            data class OK(val value: SearchCommitsResponse) : SearchCommitsResult

            data object NotModified : SearchCommitsResult
        }

        suspend fun searchCommits(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            sort: Sort? = null,
        ): SearchCommitsResult
    }

    interface Issues {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchIssuesAndPullRequestsResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<IssueSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("comments")
            Comments,
            @SerialName("reactions")
            Reactions,
            @SerialName("reactions-+1")
            `Reactions+1`,
            @SerialName("reactions--1")
            Reactions1,
            @SerialName("reactions-smile")
            ReactionsSmile,
            @SerialName("reactions-thinking_face")
            ReactionsThinkingFace,
            @SerialName("reactions-heart")
            ReactionsHeart,
            @SerialName("reactions-tada")
            ReactionsTada,
            @SerialName("interactions")
            Interactions,
            @SerialName("created")
            Created,
            @SerialName("updated")
            Updated;
        }

        sealed interface SearchIssuesAndPullRequestsResult {
            data class OK(val value: SearchIssuesAndPullRequestsResponse) : SearchIssuesAndPullRequestsResult

            data object NotModified : SearchIssuesAndPullRequestsResult

            data class Forbidden(val value: BasicError) : SearchIssuesAndPullRequestsResult

            data class UnprocessableEntity(val value: ValidationError) : SearchIssuesAndPullRequestsResult

            data class ServiceUnavailable(val value: SearchIssuesAndPullRequestsResponse) : SearchIssuesAndPullRequestsResult
        }

        suspend fun searchIssuesAndPullRequests(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            advancedSearch: String? = null,
            sort: Sort? = null,
        ): SearchIssuesAndPullRequestsResult
    }

    interface Labels {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchLabelsResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<LabelSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated;
        }

        sealed interface SearchLabelsResult {
            data class OK(val value: SearchLabelsResponse) : SearchLabelsResult

            data object NotModified : SearchLabelsResult

            data class Forbidden(val value: BasicError) : SearchLabelsResult

            data class NotFound(val value: BasicError) : SearchLabelsResult

            data class UnprocessableEntity(val value: ValidationError) : SearchLabelsResult
        }

        suspend fun searchLabels(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            repositoryId: Long,
            sort: Sort? = null,
        ): SearchLabelsResult
    }

    interface Repositories {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchReposResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<RepoSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("stars")
            Stars,
            @SerialName("forks")
            Forks,
            @SerialName("help-wanted-issues")
            HelpWantedIssues,
            @SerialName("updated")
            Updated;
        }

        sealed interface SearchReposResult {
            data class OK(val value: SearchReposResponse) : SearchReposResult

            data object NotModified : SearchReposResult

            data class UnprocessableEntity(val value: ValidationError) : SearchReposResult

            data class ServiceUnavailable(val value: SearchReposResponse) : SearchReposResult
        }

        suspend fun searchRepos(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            sort: Sort? = null,
        ): SearchReposResult
    }

    interface Topics {
        @Serializable
        data class SearchTopicsResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<TopicSearchResultItem>,
        )

        sealed interface SearchTopicsResult {
            data class OK(val value: SearchTopicsResponse) : SearchTopicsResult

            data object NotModified : SearchTopicsResult
        }

        suspend fun searchTopics(
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
        ): SearchTopicsResult
    }

    interface Users {
        @Serializable
        enum class Order {
            @SerialName("desc") Desc, @SerialName("asc") Asc;
        }


        @Serializable
        data class SearchUsersResponse(
            @SerialName("total_count") val totalCount: Long,
            @SerialName("incomplete_results") val incompleteResults: Boolean,
            val items: List<UserSearchResultItem>,
        )


        @Serializable
        enum class Sort {
            @SerialName("followers") Followers, @SerialName("repositories") Repositories, @SerialName("joined") Joined;
        }

        sealed interface SearchUsersResult {
            data class OK(val value: SearchUsersResponse) : SearchUsersResult

            data object NotModified : SearchUsersResult

            data class UnprocessableEntity(val value: ValidationError) : SearchUsersResult

            data class ServiceUnavailable(val value: SearchUsersResponse) : SearchUsersResult
        }

        suspend fun searchUsers(
            order: Order = Order.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            q: String,
            sort: Sort? = null,
        ): SearchUsersResult
    }
}

internal class KtorSearch(private val client: HttpClient) : Search {
    override val code: Search.Code = KtorSearchCode(client)

    override val commits: Search.Commits = KtorSearchCommits(client)

    override val issues: Search.Issues = KtorSearchIssues(client)

    override val labels: Search.Labels = KtorSearchLabels(client)

    override val repositories: Search.Repositories = KtorSearchRepositories(client)

    override val topics: Search.Topics = KtorSearchTopics(client)

    override val users: Search.Users = KtorSearchUsers(client)
}

internal class KtorSearchCode(private val client: HttpClient) : Search.Code {
    override suspend fun searchCode(order: Search.Code.Order, page: Long, perPage: Long, q: String, sort: Search.Code.Sort?): Search.Code.SearchCodeResult {
        val response = client.get("/search/code") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Code.SearchCodeResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Code.SearchCodeResult.NotModified
            HttpStatusCode.Forbidden -> Search.Code.SearchCodeResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Search.Code.SearchCodeResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Search.Code.SearchCodeResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchCommits(private val client: HttpClient) : Search.Commits {
    override suspend fun searchCommits(order: Search.Commits.Order, page: Long, perPage: Long, q: String, sort: Search.Commits.Sort?): Search.Commits.SearchCommitsResult {
        val response = client.get("/search/commits") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Commits.SearchCommitsResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Commits.SearchCommitsResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchIssues(private val client: HttpClient) : Search.Issues {
    override suspend fun searchIssuesAndPullRequests(order: Search.Issues.Order, page: Long, perPage: Long, q: String, advancedSearch: String?, sort: Search.Issues.Sort?): Search.Issues.SearchIssuesAndPullRequestsResult {
        val response = client.get("/search/issues") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            advancedSearch?.let { parameter("advanced_search", it) }
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Issues.SearchIssuesAndPullRequestsResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Issues.SearchIssuesAndPullRequestsResult.NotModified
            HttpStatusCode.Forbidden -> Search.Issues.SearchIssuesAndPullRequestsResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Search.Issues.SearchIssuesAndPullRequestsResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Search.Issues.SearchIssuesAndPullRequestsResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchLabels(private val client: HttpClient) : Search.Labels {
    override suspend fun searchLabels(order: Search.Labels.Order, page: Long, perPage: Long, q: String, repositoryId: Long, sort: Search.Labels.Sort?): Search.Labels.SearchLabelsResult {
        val response = client.get("/search/labels") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            parameter("repository_id", repositoryId)
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Labels.SearchLabelsResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Labels.SearchLabelsResult.NotModified
            HttpStatusCode.Forbidden -> Search.Labels.SearchLabelsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Search.Labels.SearchLabelsResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Search.Labels.SearchLabelsResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchRepositories(private val client: HttpClient) : Search.Repositories {
    override suspend fun searchRepos(order: Search.Repositories.Order, page: Long, perPage: Long, q: String, sort: Search.Repositories.Sort?): Search.Repositories.SearchReposResult {
        val response = client.get("/search/repositories") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Repositories.SearchReposResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Repositories.SearchReposResult.NotModified
            HttpStatusCode.UnprocessableEntity -> Search.Repositories.SearchReposResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Search.Repositories.SearchReposResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchTopics(private val client: HttpClient) : Search.Topics {
    override suspend fun searchTopics(page: Long, perPage: Long, q: String): Search.Topics.SearchTopicsResult {
        val response = client.get("/search/topics") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Topics.SearchTopicsResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Topics.SearchTopicsResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorSearchUsers(private val client: HttpClient) : Search.Users {
    override suspend fun searchUsers(order: Search.Users.Order, page: Long, perPage: Long, q: String, sort: Search.Users.Sort?): Search.Users.SearchUsersResult {
        val response = client.get("/search/users") {
            parameter("order", order)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("q", q)
            sort?.let { parameter("sort", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Search.Users.SearchUsersResult.OK(response.body())
            HttpStatusCode.NotModified -> Search.Users.SearchUsersResult.NotModified
            HttpStatusCode.UnprocessableEntity -> Search.Users.SearchUsersResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Search.Users.SearchUsersResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
