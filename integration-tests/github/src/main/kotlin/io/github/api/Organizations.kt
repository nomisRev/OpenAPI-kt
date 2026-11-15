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
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Float
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonPrimitive

public class Organizations internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public fun org(org: String): OrgPath = OrgPath(__client, org)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(since: Long? = null, perPage: Long? = 30L): Response {
      val response = __client.get("organizations") {
        if (since != null) {
          this.url.encodedParameters.append("since", since.toString())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
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
    private val __client: HttpClient,
    private val _org: String,
  ) {
    public val actions: Actions = Actions(__client, _org)

    public val dependabot: Dependabot = Dependabot(__client, _org)

    public val settings: Settings = Settings(__client, _org)

    public class Actions internal constructor(
      private val __client: HttpClient,
      private val _org: String,
    ) {
      public val cache: Cache = Cache(__client, _org)

      public class Cache internal constructor(
        private val __client: HttpClient,
        private val _org: String,
      ) {
        public val retentionLimit: RetentionLimit = RetentionLimit(__client, _org)

        public val storageLimit: StorageLimit = StorageLimit(__client, _org)

        public class RetentionLimit internal constructor(
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val `get`: Get = Get(__client, _org)

          public val put: Put = Put(__client, _org)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("organizations/${this._org.encodeURLParameter()}/actions/cache/retention-limit")
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
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheRetentionLimitForOrganization): Response {
              val response = __client.put("organizations/${this._org.encodeURLParameter()}/actions/cache/retention-limit") {
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
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val `get`: Get = Get(__client, _org)

          public val put: Put = Put(__client, _org)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = __client.get("organizations/${this._org.encodeURLParameter()}/actions/cache/storage-limit")
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
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheStorageLimitForOrganization): Response {
              val response = __client.put("organizations/${this._org.encodeURLParameter()}/actions/cache/storage-limit") {
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
      private val __client: HttpClient,
      private val _org: String,
    ) {
      public val repositoryAccess: RepositoryAccess = RepositoryAccess(__client, _org)

      public class RepositoryAccess internal constructor(
        private val __client: HttpClient,
        private val _org: String,
      ) {
        public val `get`: Get = Get(__client, _org)

        public val patch: Patch = Patch(__client, _org)

        public val defaultLevel: DefaultLevel = DefaultLevel(__client, _org)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public suspend operator fun invoke(page: Long? = 1L, perPage: Long? = 30L): Response {
            val response = __client.get("organizations/${this._org.encodeURLParameter()}/dependabot/repository-access") {
              if (page != null) {
                this.url.encodedParameters.append("page", page.toString())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
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
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public suspend operator fun invoke(repositoryIdsToAdd: List<Long>? = null, repositoryIdsToRemove: List<Long>? = null): Response {
            val response = __client.patch("organizations/${this._org.encodeURLParameter()}/dependabot/repository-access") {
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

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @SerialName("repository_ids_to_add")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val repositoryIdsToAdd: List<Long>? = null,
            @SerialName("repository_ids_to_remove")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
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
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val put: Put = Put(__client, _org)

          public class Put internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(defaultLevel: DefaultLevel): Response {
              val response = __client.put("organizations/${this._org.encodeURLParameter()}/dependabot/repository-access/default-level") {
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

            @Serializable
            internal data class Body(
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
      private val __client: HttpClient,
      private val _org: String,
    ) {
      public val billing: Billing = Billing(__client, _org)

      public class Billing internal constructor(
        private val __client: HttpClient,
        private val _org: String,
      ) {
        public val budgets: Budgets = Budgets(__client, _org)

        public val premiumRequest: PremiumRequest = PremiumRequest(__client, _org)

        public val usage: Usage = Usage(__client, _org)

        public class Budgets internal constructor(
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val `get`: Get = Get(__client, _org)

          public fun budgetId(budgetId: String): BudgetIdPath = BudgetIdPath(__client, _org, budgetId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(
              page: Long? = 1L,
              perPage: Long? = 10L,
              scope: Scope? = null,
            ): Response {
              val response = __client.get("organizations/${this._org.encodeURLParameter()}/settings/billing/budgets") {
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (scope != null) {
                  this.url.encodedParameters.append("scope", scope.value.encodeURLParameter())
                }
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
            private val __client: HttpClient,
            private val _org: String,
            private val _budgetId: String,
          ) {
            public val delete: Delete = Delete(__client, _org, _budgetId)

            public val `get`: Get = Get(__client, _org, _budgetId)

            public val patch: Patch = Patch(__client, _org, _budgetId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _org: String,
              private val _budgetId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.delete("organizations/${this._org.encodeURLParameter()}/settings/billing/budgets/${this._budgetId.encodeURLParameter()}")
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

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _org: String,
              private val _budgetId: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = __client.get("organizations/${this._org.encodeURLParameter()}/settings/billing/budgets/${this._budgetId.encodeURLParameter()}")
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

            public class Patch internal constructor(
              private val __client: HttpClient,
              private val _org: String,
              private val _budgetId: String,
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
                val response = __client.patch("organizations/${this._org.encodeURLParameter()}/settings/billing/budgets/${this._budgetId.encodeURLParameter()}") {
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

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class BudgetAlerting(
                @SerialName("will_alert")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val willAlert: Boolean? = null,
                @SerialName("alert_recipients")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
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
                    return when {
                      value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                        value,
                        ProductPricing::class to { decodeFromJsonElement(ProductPricing.serializer(), it) },
                        SkuPricing::class to { decodeFromJsonElement(SkuPricing.serializer(), it) },
                      )
                      else -> throw UnionSerializationException(value, emptyMap())
                    }
                  }

                  override fun serialize(encoder: Encoder, `value`: BudgetType) {
                    when(value) {
                      is ProductPricing -> encoder.encodeSerializableValue(ProductPricing.serializer(), value)
                      is SkuPricing -> encoder.encodeSerializableValue(SkuPricing.serializer(), value)
                    }
                  }
                }
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("budget_amount")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetAmount: Long? = null,
                @SerialName("prevent_further_usage")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val preventFurtherUsage: Boolean? = null,
                @SerialName("budget_alerting")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetAlerting: BudgetAlerting? = null,
                @SerialName("budget_scope")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetScope: BudgetScope? = null,
                @SerialName("budget_entity_name")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetEntityName: String? = null,
                @SerialName("budget_type")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetType: BudgetType? = null,
                @SerialName("budget_product_sku")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val budgetProductSku: String? = null,
              )

              public sealed interface Response {
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Ok(
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val message: String? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val budget: Budget? = null,
                ) : Response {
                  @OptIn(ExperimentalSerializationApi::class)
                  @Serializable
                  public data class Budget(
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val id: String? = null,
                    @SerialName("budget_amount")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val budgetAmount: Float? = null,
                    @SerialName("prevent_further_usage")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val preventFurtherUsage: Boolean? = null,
                    @SerialName("budget_alerting")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val budgetAlerting: BudgetAlerting? = null,
                    @SerialName("budget_scope")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val budgetScope: BudgetScope? = null,
                    @SerialName("budget_entity_name")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val budgetEntityName: String? = null,
                    @SerialName("budget_type")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
                    public val budgetType: BudgetType? = null,
                    @SerialName("budget_product_sku")
                    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
                          return when {
                            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                              value,
                              ProductPricing::class to { decodeFromJsonElement(ProductPricing.serializer(), it) },
                              SkuPricing::class to { decodeFromJsonElement(SkuPricing.serializer(), it) },
                            )
                            else -> throw UnionSerializationException(value, emptyMap())
                          }
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
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val usage: Usage = Usage(__client, _org)

          public class Usage internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public val `get`: Get = Get(__client, _org)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _org: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                user: String? = null,
                model: String? = null,
                product: String? = null,
              ): Response {
                val response = __client.get("organizations/${this._org.encodeURLParameter()}/settings/billing/premium_request/usage") {
                  if (year != null) {
                    this.url.encodedParameters.append("year", year.toString())
                  }
                  if (month != null) {
                    this.url.encodedParameters.append("month", month.toString())
                  }
                  if (day != null) {
                    this.url.encodedParameters.append("day", day.toString())
                  }
                  if (user != null) {
                    this.url.encodedParameters.append("user", user.encodeURLParameter())
                  }
                  if (model != null) {
                    this.url.encodedParameters.append("model", model.encodeURLParameter())
                  }
                  if (product != null) {
                    this.url.encodedParameters.append("product", product.encodeURLParameter())
                  }
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

        public class Usage internal constructor(
          private val __client: HttpClient,
          private val _org: String,
        ) {
          public val `get`: Get = Get(__client, _org)

          public val summary: Summary = Summary(__client, _org)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public suspend operator fun invoke(
              year: Long? = null,
              month: Long? = null,
              day: Long? = null,
            ): Response {
              val response = __client.get("organizations/${this._org.encodeURLParameter()}/settings/billing/usage") {
                if (year != null) {
                  this.url.encodedParameters.append("year", year.toString())
                }
                if (month != null) {
                  this.url.encodedParameters.append("month", month.toString())
                }
                if (day != null) {
                  this.url.encodedParameters.append("day", day.toString())
                }
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

          public class Summary internal constructor(
            private val __client: HttpClient,
            private val _org: String,
          ) {
            public val `get`: Get = Get(__client, _org)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _org: String,
            ) {
              public suspend operator fun invoke(
                year: Long? = null,
                month: Long? = null,
                day: Long? = null,
                repository: String? = null,
                product: String? = null,
                sku: String? = null,
              ): Response {
                val response = __client.get("organizations/${this._org.encodeURLParameter()}/settings/billing/usage/summary") {
                  if (year != null) {
                    this.url.encodedParameters.append("year", year.toString())
                  }
                  if (month != null) {
                    this.url.encodedParameters.append("month", month.toString())
                  }
                  if (day != null) {
                    this.url.encodedParameters.append("day", day.toString())
                  }
                  if (repository != null) {
                    this.url.encodedParameters.append("repository", repository.encodeURLParameter())
                  }
                  if (product != null) {
                    this.url.encodedParameters.append("product", product.encodeURLParameter())
                  }
                  if (sku != null) {
                    this.url.encodedParameters.append("sku", sku.encodeURLParameter())
                  }
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
      }
    }
  }
}
