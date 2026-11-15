package io.github.api

import io.github.model.BasicError
import io.github.model.MarketplaceListingPlan
import io.github.model.MarketplacePurchase
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class MarketplaceListing internal constructor(
  private val __client: HttpClient,
) {
  public val accounts: Accounts = Accounts(__client)

  public val plans: Plans = Plans(__client)

  public val stubbed: Stubbed = Stubbed(__client)

  public class Accounts internal constructor(
    private val __client: HttpClient,
  ) {
    public fun accountId(accountId: Long): AccountIdPath = AccountIdPath(__client, accountId)

    public class AccountIdPath internal constructor(
      private val __client: HttpClient,
      private val _accountId: Long,
    ) {
      public val `get`: Get = Get(__client, _accountId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _accountId: Long,
      ) {
        public suspend operator fun invoke(): Response {
          val response = __client.get("marketplace_listing/accounts/${this._accountId}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            401 -> Response.Unauthorized(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: MarketplacePurchase,
          ) : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }
  }

  public class Plans internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public fun planId(planId: Long): PlanIdPath = PlanIdPath(__client, planId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
        val response = __client.get("marketplace_listing/plans") {
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          401 -> Response.Unauthorized(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<MarketplaceListingPlan>,
        ) : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class PlanIdPath internal constructor(
      private val __client: HttpClient,
      private val _planId: Long,
    ) {
      public val accounts: Accounts = Accounts(__client, _planId)

      public class Accounts internal constructor(
        private val __client: HttpClient,
        private val _planId: Long,
      ) {
        public val `get`: Get = Get(__client, _planId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _planId: Long,
        ) {
          public suspend operator fun invoke(
            sort: Sort? = Sort.Created,
            direction: Direction? = null,
            perPage: Long? = 30L,
            page: Long? = 1L,
          ): Response {
            val response = __client.get("marketplace_listing/plans/${this._planId}/accounts") {
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
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
              401 -> Response.Unauthorized(response.body())
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
              public val `value`: List<MarketplacePurchase>,
            ) : Response

            public data class Unauthorized(
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
    }
  }

  public class Stubbed internal constructor(
    private val __client: HttpClient,
  ) {
    public val accounts: Accounts = Accounts(__client)

    public val plans: Plans = Plans(__client)

    public class Accounts internal constructor(
      private val __client: HttpClient,
    ) {
      public fun accountId(accountId: Long): AccountIdPath = AccountIdPath(__client, accountId)

      public class AccountIdPath internal constructor(
        private val __client: HttpClient,
        private val _accountId: Long,
      ) {
        public val `get`: Get = Get(__client, _accountId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _accountId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.get("marketplace_listing/stubbed/accounts/${this._accountId}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              401 -> Response.Unauthorized(response.body())
              404 -> Response.NotFound
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: MarketplacePurchase,
            ) : Response

            public data class Unauthorized(
              public val `value`: BasicError,
            ) : Response

            public data object NotFound : Response
          }
        }
      }
    }

    public class Plans internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public fun planId(planId: Long): PlanIdPath = PlanIdPath(__client, planId)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = __client.get("marketplace_listing/stubbed/plans") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            401 -> Response.Unauthorized(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<MarketplaceListingPlan>,
          ) : Response

          public data class Unauthorized(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class PlanIdPath internal constructor(
        private val __client: HttpClient,
        private val _planId: Long,
      ) {
        public val accounts: Accounts = Accounts(__client, _planId)

        public class Accounts internal constructor(
          private val __client: HttpClient,
          private val _planId: Long,
        ) {
          public val `get`: Get = Get(__client, _planId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _planId: Long,
          ) {
            public suspend operator fun invoke(
              sort: Sort? = Sort.Created,
              direction: Direction? = null,
              perPage: Long? = 30L,
              page: Long? = 1L,
            ): Response {
              val response = __client.get("marketplace_listing/stubbed/plans/${this._planId}/accounts") {
                if (sort != null) {
                  this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
                }
                if (direction != null) {
                  this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
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
                401 -> Response.Unauthorized(response.body())
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
                public val `value`: List<MarketplacePurchase>,
              ) : Response

              public data class Unauthorized(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }
  }
}
