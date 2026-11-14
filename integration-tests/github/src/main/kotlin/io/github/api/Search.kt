package io.github.api

import io.github.model.BasicError
import io.github.model.CodeSearchResultItem
import io.github.model.CommitSearchResultItem
import io.github.model.IssueSearchResultItem
import io.github.model.LabelSearchResultItem
import io.github.model.RepoSearchResultItem
import io.github.model.TopicSearchResultItem
import io.github.model.UserSearchResultItem
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Search internal constructor(
  private val client: HttpClient,
) {
  public val code: Code = Code(client)

  public val commits: Commits = Commits(client)

  public val issues: Issues = Issues(client)

  public val labels: Labels = Labels(client)

  public val repositories: Repositories = Repositories(client)

  public val topics: Topics = Topics(client)

  public val users: Users = Users(client)

  public class Code internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/code") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("indexed")
        Indexed("indexed"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<CodeSearchResultItem>,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class ServiceUnavailable(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val code: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response
      }
    }
  }

  public class Commits internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/commits") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("author-date")
        AuthorDate("author-date"),
        @SerialName("committer-date")
        CommitterDate("committer-date"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<CommitSearchResultItem>,
        ) : Response

        public data object NotModified : Response
      }
    }
  }

  public class Issues internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
        advancedSearch: String? = null,
      ): Response {
        val response = client.get("search/issues") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
          if (advancedSearch != null) {
            this.url.encodedParameters.append("advanced_search", advancedSearch.encodeURLParameter())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("comments")
        Comments("comments"),
        @SerialName("reactions")
        Reactions("reactions"),
        @SerialName("reactions-+1")
        `Reactions+1`("reactions-+1"),
        @SerialName("reactions--1")
        Reactions1("reactions--1"),
        @SerialName("reactions-smile")
        ReactionsSmile("reactions-smile"),
        @SerialName("reactions-thinking_face")
        ReactionsThinkingFace("reactions-thinking_face"),
        @SerialName("reactions-heart")
        ReactionsHeart("reactions-heart"),
        @SerialName("reactions-tada")
        ReactionsTada("reactions-tada"),
        @SerialName("interactions")
        Interactions("interactions"),
        @SerialName("created")
        Created("created"),
        @SerialName("updated")
        Updated("updated"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<IssueSearchResultItem>,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class ServiceUnavailable(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val code: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response
      }
    }
  }

  public class Labels internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        repositoryId: Long,
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/labels") {
          this.url.encodedParameters.append("repository_id", repositoryId.toString())
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("created")
        Created("created"),
        @SerialName("updated")
        Updated("updated"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<LabelSearchResultItem>,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response
      }
    }
  }

  public class Repositories internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/repositories") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          422 -> Response.UnprocessableEntity(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("stars")
        Stars("stars"),
        @SerialName("forks")
        Forks("forks"),
        @SerialName("help-wanted-issues")
        HelpWantedIssues("help-wanted-issues"),
        @SerialName("updated")
        Updated("updated"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<RepoSearchResultItem>,
        ) : Response

        public data object NotModified : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class ServiceUnavailable(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val code: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response
      }
    }
  }

  public class Topics internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/topics") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<TopicSearchResultItem>,
        ) : Response

        public data object NotModified : Response
      }
    }
  }

  public class Users internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        q: String,
        sort: Sort? = null,
        order: Order? = Order.Desc,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("search/users") {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
          if (sort != null) {
            this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
          }
          if (order != null) {
            this.url.encodedParameters.append("order", order.value.encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          304 -> Response.NotModified
          422 -> Response.UnprocessableEntity(response.body())
          503 -> response.body<Response.ServiceUnavailable>()
          else -> throw ResponseException(response, "")
        }
      }

      @Serializable
      public enum class Sort(
        public val `value`: String,
      ) {
        @SerialName("followers")
        Followers("followers"),
        @SerialName("repositories")
        Repositories("repositories"),
        @SerialName("joined")
        Joined("joined"),
        ;
      }

      @Serializable
      public enum class Order(
        public val `value`: String,
      ) {
        @SerialName("desc")
        Desc("desc"),
        @SerialName("asc")
        Asc("asc"),
        ;
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          @SerialName("total_count")
          public val totalCount: Long,
          @SerialName("incomplete_results")
          public val incompleteResults: Boolean,
          public val items: List<UserSearchResultItem>,
        ) : Response

        public data object NotModified : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class ServiceUnavailable(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val code: String? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response
      }
    }
  }
}
