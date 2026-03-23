package io.github.api

import io.github.model.ActionsCacheRetentionLimitForOrganization
import io.github.model.ActionsCacheStorageLimitForOrganization
import io.github.model.BasicError
import io.github.model.BillingPremiumRequestUsageReportOrg
import io.github.model.BillingUsageReport
import io.github.model.BillingUsageSummaryReportOrg
import io.github.model.DeleteBudget
import io.github.model.DependabotRepositoryAccessDetails
import io.github.model.GetAllBudgets
import io.github.model.GetBudget
import io.github.model.OrganizationSimple
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Float
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Organizations internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun org(org: String): OrgPath = OrgPath(client, org)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(since: Long? = null, perPage: Long? = 30L): Response {
      val response = client.get("/organizations") {
        since?.let { parameter("since", it) }
        perPage?.let { parameter("per_page", it) }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<OrganizationSimple>,
      ) : Response

      public data object NotModified : Response
    }
  }

  public class OrgPath internal constructor(
    private val client: HttpClient,
    private val org: String,
  ) {
    public val actions: Actions = Actions(client, org)

    public val dependabot: Dependabot = Dependabot(client, org)

    public val settings: Settings = Settings(client, org)

    public class Actions internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val cache: Cache = Cache(client, org)

      public class Cache internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val retentionLimit: RetentionLimit = RetentionLimit(client, org)

        public val storageLimit: StorageLimit = StorageLimit(client, org)

        public class RetentionLimit internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/organizations/$org/actions/cache/retention-limit")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsCacheRetentionLimitForOrganization,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheRetentionLimitForOrganization): Response {
              val response = client.put("/organizations/$org/actions/cache/retention-limit") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class StorageLimit internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val put: Put = Put(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("/organizations/$org/actions/cache/storage-limit")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsCacheStorageLimitForOrganization,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheStorageLimitForOrganization): Response {
              val response = client.put("/organizations/$org/actions/cache/storage-limit") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class Dependabot internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val repositoryAccess: RepositoryAccess = RepositoryAccess(client, org)

      public class RepositoryAccess internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val `get`: Get = Get(client, org)

        public val patch: Patch = Patch(client, org)

        public val defaultLevel: DefaultLevel = DefaultLevel(client, org)

        public class Get internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
            val response = client.get("/organizations/$org/dependabot/repository-access") {
              page?.let { parameter("page", it) }
              perPage?.let { parameter("per_page", it) }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: DependabotRepositoryAccessDetails,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public suspend operator fun invoke(repositoryIdsToAdd: List<Long>? = null, repositoryIdsToRemove: List<Long>? = null): Response {
            val response = client.patch("/organizations/$org/dependabot/repository-access") {
              contentType(ContentType.Application.Json)
              setBody(Body(repositoryIdsToAdd = repositoryIdsToAdd, repositoryIdsToRemove = repositoryIdsToRemove))
            }
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            @SerialName("repository_ids_to_add")
            public val repositoryIdsToAdd: List<Long>? = null,
            @SerialName("repository_ids_to_remove")
            public val repositoryIdsToRemove: List<Long>? = null,
          )

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class DefaultLevel internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val put: Put = Put(client, org)

          public class Put internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(defaultLevel: DefaultLevel): Response {
              val response = client.put("/organizations/$org/dependabot/repository-access/default-level") {
                contentType(ContentType.Application.Json)
                setBody(Body(defaultLevel = defaultLevel))
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class DefaultLevel(
              public val `value`: String,
            ) {
              @SerialName("public")
              Public("public"),
              @SerialName("internal")
              Internal("internal"),
              ;
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("default_level")
              public val defaultLevel: DefaultLevel,
            )

            public sealed interface Response {
              public data object NoContent : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }
    }

    public class Settings internal constructor(
      private val client: HttpClient,
      private val org: String,
    ) {
      public val billing: Billing = Billing(client, org)

      public class Billing internal constructor(
        private val client: HttpClient,
        private val org: String,
      ) {
        public val budgets: Budgets = Budgets(client, org)

        public val premiumRequest: PremiumRequest = PremiumRequest(client, org)

        public val usage: Usage = Usage(client, org)

        public class Budgets internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public fun budgetId(budgetId: String): BudgetIdPath = BudgetIdPath(client, org, budgetId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 10L,
              scope: Scope? = null,
            ): Response {
              val response = client.get("/organizations/$org/settings/billing/budgets") {
                page?.let { parameter("page", it) }
                perPage?.let { parameter("per_page", it) }
                scope?.let { parameter("scope", it.value) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                500 -> Response.InternalServerError(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class Scope(
              public val `value`: String,
            ) {
              @SerialName("enterprise")
              Enterprise("enterprise"),
              @SerialName("organization")
              Organization("organization"),
              @SerialName("repository")
              Repository("repository"),
              @SerialName("cost_center")
              CostCenter("cost_center"),
              ;
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: GetAllBudgets,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class BudgetIdPath internal constructor(
            private val client: HttpClient,
            private val org: String,
            private val budgetId: String,
          ) {
            public val delete: Delete = Delete(client, org, budgetId)

            public val `get`: Get = Get(client, org, budgetId)

            public val patch: Patch = Patch(client, org, budgetId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val budgetId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/organizations/$org/settings/billing/budgets/$budgetId")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: DeleteBudget,
                ) : Response

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val budgetId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("/organizations/$org/settings/billing/budgets/$budgetId")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: GetBudget,
                ) : Response

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }

            public class Patch internal constructor(
              private val client: HttpClient,
              private val org: String,
              private val budgetId: String,
            ) {
              public suspend operator fun invoke(
                budgetAmount: Long? = null,
                preventFurtherUsage: Boolean? = null,
                budgetAlerting: BudgetAlerting? = null,
                budgetScope: BudgetScope? = null,
                budgetEntityName: String? = null,
                budgetType: BudgetType? = null,
                budgetProductSku: String? = null,
              ): Response {
                val response = client.patch("/organizations/$org/settings/billing/budgets/$budgetId") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(budgetAmount = budgetAmount, preventFurtherUsage = preventFurtherUsage, budgetAlerting = budgetAlerting, budgetScope = budgetScope, budgetEntityName = budgetEntityName, budgetType = budgetType, budgetProductSku = budgetProductSku))
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  400 -> Response.BadRequest(response.body())
                  401 -> Response.Unauthorized(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  500 -> Response.InternalServerError(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public data class BudgetAlerting(
                @SerialName("will_alert")
                public val willAlert: Boolean? = null,
                @SerialName("alert_recipients")
                public val alertRecipients: List<String>? = null,
              )

              @Serializable
              public enum class BudgetScope(
                public val `value`: String,
              ) {
                @SerialName("enterprise")
                Enterprise("enterprise"),
                @SerialName("organization")
                Organization("organization"),
                @SerialName("repository")
                Repository("repository"),
                @SerialName("cost_center")
                CostCenter("cost_center"),
                ;
              }

              /**
               * The type of pricing for the budget
               */
              @Serializable(with = BudgetType.Serializer::class)
              public sealed interface BudgetType {
                @Serializable
                public enum class ProductPricing : BudgetType {
                  ProductPricing,
                }

                @Serializable
                public enum class SkuPricing : BudgetType {
                  SkuPricing,
                }

                public object Serializer : KSerializer<BudgetType> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Organizations.OrgPath.Settings.Billing.Budgets.BudgetIdPath.Patch.BudgetType", PolymorphicKind.SEALED) {
                    element("ProductPricing", ProductPricing.serializer().descriptor)
                    element("SkuPricing", SkuPricing.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): BudgetType {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      ProductPricing::class to { decodeFromJsonElement(ProductPricing.serializer(), it) },
                      SkuPricing::class to { decodeFromJsonElement(SkuPricing.serializer(), it) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: BudgetType) {
                    when(value) {
                      is ProductPricing -> encoder.encodeSerializableValue(ProductPricing.serializer(), value)
                      is SkuPricing -> encoder.encodeSerializableValue(SkuPricing.serializer(), value)
                    }
                  }
                }
              }

              @Serializable
              internal data class Body(
                @SerialName("budget_amount")
                public val budgetAmount: Long? = null,
                @SerialName("prevent_further_usage")
                public val preventFurtherUsage: Boolean? = null,
                @SerialName("budget_alerting")
                public val budgetAlerting: BudgetAlerting? = null,
                @SerialName("budget_scope")
                public val budgetScope: BudgetScope? = null,
                @SerialName("budget_entity_name")
                public val budgetEntityName: String? = null,
                @SerialName("budget_type")
                public val budgetType: BudgetType? = null,
                @SerialName("budget_product_sku")
                public val budgetProductSku: String? = null,
              )

              public sealed interface Response {
                @Serializable
                public data class Ok(
                  public val message: String? = null,
                  public val budget: Budget? = null,
                ) : Response {
                  @Serializable
                  public data class Budget(
                    public val id: String? = null,
                    @SerialName("budget_amount")
                    public val budgetAmount: Float? = null,
                    @SerialName("prevent_further_usage")
                    public val preventFurtherUsage: Boolean? = null,
                    @SerialName("budget_alerting")
                    public val budgetAlerting: BudgetAlerting? = null,
                    @SerialName("budget_scope")
                    public val budgetScope: BudgetScope? = null,
                    @SerialName("budget_entity_name")
                    public val budgetEntityName: String? = null,
                    @SerialName("budget_type")
                    public val budgetType: BudgetType? = null,
                    @SerialName("budget_product_sku")
                    public val budgetProductSku: String? = null,
                  ) {
                    @Serializable
                    public data class BudgetAlerting(
                      @SerialName("will_alert")
                      public val willAlert: Boolean,
                      @SerialName("alert_recipients")
                      public val alertRecipients: List<String>,
                    )

                    @Serializable
                    public enum class BudgetScope(
                      public val `value`: String,
                    ) {
                      @SerialName("enterprise")
                      Enterprise("enterprise"),
                      @SerialName("organization")
                      Organization("organization"),
                      @SerialName("repository")
                      Repository("repository"),
                      @SerialName("cost_center")
                      CostCenter("cost_center"),
                      ;
                    }

                    /**
                     * The type of pricing for the budget
                     */
                    @Serializable(with = BudgetType.Serializer::class)
                    public sealed interface BudgetType {
                      @Serializable
                      public enum class ProductPricing : BudgetType {
                        ProductPricing,
                      }

                      @Serializable
                      public enum class SkuPricing : BudgetType {
                        SkuPricing,
                      }

                      public object Serializer : KSerializer<BudgetType> {
                        @OptIn(
                          InternalSerializationApi::class,
                          ExperimentalSerializationApi::class,
                        )
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.api.Organizations.OrgPath.Settings.Billing.Budgets.BudgetIdPath.Patch.Response.Ok.Budget.BudgetType", PolymorphicKind.SEALED) {
                          element("ProductPricing", ProductPricing.serializer().descriptor)
                          element("SkuPricing", SkuPricing.serializer().descriptor)
                        }

                        override fun deserialize(decoder: Decoder): BudgetType {
                          val value = decoder.decodeSerializableValue(JsonElement.serializer())
                          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                          return json.attemptDeserialize(
                            value,
                            ProductPricing::class to { decodeFromJsonElement(ProductPricing.serializer(), it) },
                            SkuPricing::class to { decodeFromJsonElement(SkuPricing.serializer(), it) },
                          )
                        }

                        override fun serialize(encoder: Encoder, `value`: BudgetType) {
                          when(value) {
                            is ProductPricing -> encoder.encodeSerializableValue(ProductPricing.serializer(), value)
                            is SkuPricing -> encoder.encodeSerializableValue(SkuPricing.serializer(), value)
                          }
                        }
                      }
                    }
                  }
                }

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class Unauthorized(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }

        public class PremiumRequest internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val usage: Usage = Usage(client, org)

          public class Usage internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                user: String? = null,
                model: String? = null,
                product: String? = null,
              ): Response {
                val response = client.get("/organizations/$org/settings/billing/premium_request/usage") {
                  year?.let { parameter("year", it) }
                  month?.let { parameter("month", it) }
                  day?.let { parameter("day", it) }
                  user?.let { parameter("user", it) }
                  model?.let { parameter("model", it) }
                  product?.let { parameter("product", it) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: BillingPremiumRequestUsageReportOrg,
                ) : Response

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }

        public class Usage internal constructor(
          private val client: HttpClient,
          private val org: String,
        ) {
          public val `get`: Get = Get(client, org)

          public val summary: Summary = Summary(client, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public suspend operator fun invoke(
              year: Long? = null,
              month: Long? = null,
              day: Long? = null,
            ): Response {
              val response = client.get("/organizations/$org/settings/billing/usage") {
                year?.let { parameter("year", it) }
                month?.let { parameter("month", it) }
                day?.let { parameter("day", it) }
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                500 -> Response.InternalServerError(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: BillingUsageReport,
              ) : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class InternalServerError(
                public val `value`: BasicError,
              ) : Response

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Summary internal constructor(
            private val client: HttpClient,
            private val org: String,
          ) {
            public val `get`: Get = Get(client, org)

            public class Get internal constructor(
              private val client: HttpClient,
              private val org: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                repository: String? = null,
                product: String? = null,
                sku: String? = null,
              ): Response {
                val response = client.get("/organizations/$org/settings/billing/usage/summary") {
                  year?.let { parameter("year", it) }
                  month?.let { parameter("month", it) }
                  day?.let { parameter("day", it) }
                  repository?.let { parameter("repository", it) }
                  product?.let { parameter("product", it) }
                  sku?.let { parameter("sku", it) }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  400 -> Response.BadRequest(response.body())
                  403 -> Response.Forbidden(response.body())
                  500 -> Response.InternalServerError(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: BillingUsageSummaryReportOrg,
                ) : Response

                public data class BadRequest(
                  public val `value`: BasicError,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class InternalServerError(
                  public val `value`: BasicError,
                ) : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }
      }
    }
  }
}
