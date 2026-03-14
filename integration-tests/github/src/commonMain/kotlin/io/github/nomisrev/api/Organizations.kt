package io.github.nomisrev.api

import io.github.nomisrev.model.OrganizationSimple
import io.github.nomisrev.model.ActionsCacheRetentionLimitForOrganization
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ActionsCacheStorageLimitForOrganization
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.DependabotRepositoryAccessDetails
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.GetAllBudgets
import io.github.nomisrev.model.GetBudget
import io.github.nomisrev.model.DeleteBudget
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.BillingPremiumRequestUsageReportOrg
import io.github.nomisrev.model.BillingUsageReport
import io.github.nomisrev.model.BillingUsageSummaryReportOrg
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Organizations {
    val actions: Organizations.Actions

    val dependabot: Organizations.Dependabot

    val settings: Organizations.Settings

    sealed interface OrgsListResult {
        data class OK(val value: List<OrganizationSimple>) : OrgsListResult

        data object NotModified : OrgsListResult
    }

    suspend fun orgsList(
        perPage: Long = 30L,
        since: Long? = null,
    ): OrgsListResult

    interface Actions {
        val cache: Organizations.Actions.Cache

        interface Cache {
            val retentionLimit: Organizations.Actions.Cache.RetentionLimit

            val storageLimit: Organizations.Actions.Cache.StorageLimit

            interface RetentionLimit {
                sealed interface ActionsGetActionsCacheRetentionLimitForOrganizationResult {
                    data class OK(val value: ActionsCacheRetentionLimitForOrganization) : ActionsGetActionsCacheRetentionLimitForOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForOrganizationResult
                }

                suspend fun actionsGetActionsCacheRetentionLimitForOrganization(
                    org: String,
                ): ActionsGetActionsCacheRetentionLimitForOrganizationResult

                sealed interface ActionsSetActionsCacheRetentionLimitForOrganizationResult {
                    data object NoContent : ActionsSetActionsCacheRetentionLimitForOrganizationResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForOrganizationResult
                }

                suspend fun actionsSetActionsCacheRetentionLimitForOrganization(
                    org: String,
                    body: ActionsCacheRetentionLimitForOrganization,
                ): ActionsSetActionsCacheRetentionLimitForOrganizationResult
            }

            interface StorageLimit {
                sealed interface ActionsGetActionsCacheStorageLimitForOrganizationResult {
                    data class OK(val value: ActionsCacheStorageLimitForOrganization) : ActionsGetActionsCacheStorageLimitForOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheStorageLimitForOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheStorageLimitForOrganizationResult
                }

                suspend fun actionsGetActionsCacheStorageLimitForOrganization(
                    org: String,
                ): ActionsGetActionsCacheStorageLimitForOrganizationResult

                sealed interface ActionsSetActionsCacheStorageLimitForOrganizationResult {
                    data object NoContent : ActionsSetActionsCacheStorageLimitForOrganizationResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheStorageLimitForOrganizationResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheStorageLimitForOrganizationResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheStorageLimitForOrganizationResult
                }

                suspend fun actionsSetActionsCacheStorageLimitForOrganization(
                    org: String,
                    body: ActionsCacheStorageLimitForOrganization,
                ): ActionsSetActionsCacheStorageLimitForOrganizationResult
            }
        }
    }

    interface Dependabot {
        val repositoryAccess: Organizations.Dependabot.RepositoryAccess

        interface RepositoryAccess {
            val defaultLevel: Organizations.Dependabot.RepositoryAccess.DefaultLevel

            @Serializable
            data class DependabotUpdateRepositoryAccessForOrgBody(
                @SerialName("repository_ids_to_add") val repositoryIdsToAdd: List<Long>? = null,
                @SerialName("repository_ids_to_remove") val repositoryIdsToRemove: List<Long>? = null,
            )

            sealed interface DependabotRepositoryAccessForOrgResult {
                data class OK(val value: DependabotRepositoryAccessDetails) : DependabotRepositoryAccessForOrgResult

                data class Forbidden(val value: BasicError) : DependabotRepositoryAccessForOrgResult

                data class NotFound(val value: BasicError) : DependabotRepositoryAccessForOrgResult
            }

            suspend fun dependabotRepositoryAccessForOrg(
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): DependabotRepositoryAccessForOrgResult

            sealed interface DependabotUpdateRepositoryAccessForOrgResult {
                data object NoContent : DependabotUpdateRepositoryAccessForOrgResult

                data class Forbidden(val value: BasicError) : DependabotUpdateRepositoryAccessForOrgResult

                data class NotFound(val value: BasicError) : DependabotUpdateRepositoryAccessForOrgResult
            }

            suspend fun dependabotUpdateRepositoryAccessForOrg(
                org: String,
                body: DependabotUpdateRepositoryAccessForOrgBody,
            ): DependabotUpdateRepositoryAccessForOrgResult

            interface DefaultLevel {
                @Serializable
                @JvmInline
                value class DependabotSetRepositoryAccessDefaultLevelBody(@SerialName("default_level") val defaultLevel: DefaultLevel) {
                    @Serializable
                    enum class DefaultLevel {
                        @SerialName("public") Public, @SerialName("internal") Internal;
                    }
                }

                sealed interface DependabotSetRepositoryAccessDefaultLevelResult {
                    data object NoContent : DependabotSetRepositoryAccessDefaultLevelResult

                    data class Forbidden(val value: BasicError) : DependabotSetRepositoryAccessDefaultLevelResult

                    data class NotFound(val value: BasicError) : DependabotSetRepositoryAccessDefaultLevelResult
                }

                suspend fun dependabotSetRepositoryAccessDefaultLevel(
                    org: String,
                    body: DependabotSetRepositoryAccessDefaultLevelBody,
                ): DependabotSetRepositoryAccessDefaultLevelResult
            }
        }
    }

    interface Settings {
        val billing: Organizations.Settings.Billing

        interface Billing {
            val budgets: Organizations.Settings.Billing.Budgets

            val premiumRequest: Organizations.Settings.Billing.PremiumRequest

            val usage: Organizations.Settings.Billing.Usage

            interface Budgets {
                @Serializable
                data class BillingDeleteBudgetOrgResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class BillingGetBudgetOrgResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )


                @Serializable
                data class BillingUpdateBudgetOrgBody(
                    @SerialName("budget_amount") val budgetAmount: Long? = null,
                    @SerialName("prevent_further_usage") val preventFurtherUsage: Boolean? = null,
                    @SerialName("budget_alerting") val budgetAlerting: BudgetAlerting? = null,
                    @SerialName("budget_scope") val budgetScope: BudgetScope? = null,
                    @SerialName("budget_entity_name") val budgetEntityName: String? = null,
                    @SerialName("budget_type") val budgetType: BudgetType? = null,
                    @SerialName("budget_product_sku") val budgetProductSku: String? = null,
                ) {
                    @Serializable
                    data class BudgetAlerting(
                        @SerialName("will_alert") val willAlert: Boolean? = null,
                        @SerialName("alert_recipients") val alertRecipients: List<String>? = null,
                    )

                    @Serializable
                    enum class BudgetScope {
                        @SerialName("enterprise")
                        Enterprise,
                        @SerialName("organization")
                        Organization,
                        @SerialName("repository")
                        Repository,
                        @SerialName("cost_center")
                        CostCenter;
                    }

                    @Serializable(with = BudgetType.Serializer::class)
                    sealed interface BudgetType {
                        @Serializable
                        enum class ProductPricing : BudgetType {
                            ProductPricing;
                        }

                        @Serializable
                        enum class SkuPricing : BudgetType {
                            SkuPricing;
                        }

                        object Serializer : KSerializer<BudgetType> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgBody.BudgetType", PolymorphicKind.SEALED) {
                                    element("ProductPricing", BillingUpdateBudgetOrgBody.BudgetType.ProductPricing.serializer().descriptor)
                                    element("SkuPricing", BillingUpdateBudgetOrgBody.BudgetType.SkuPricing.serializer().descriptor)
                                }

                            override fun deserialize(decoder: Decoder): BudgetType {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    ProductPricing::class to { decodeFromJsonElement(BillingUpdateBudgetOrgBody.BudgetType.ProductPricing.serializer(), it) },
                                    SkuPricing::class to { decodeFromJsonElement(BillingUpdateBudgetOrgBody.BudgetType.SkuPricing.serializer(), it) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: BudgetType) = when(value) {
                                is ProductPricing -> encoder.encodeSerializableValue(BillingUpdateBudgetOrgBody.BudgetType.ProductPricing.serializer(), value)
                                is SkuPricing -> encoder.encodeSerializableValue(BillingUpdateBudgetOrgBody.BudgetType.SkuPricing.serializer(), value)
                            }
                        }
                    }
                }


                @Serializable
                data class BillingUpdateBudgetOrgResponse(val message: String? = null, val budget: Budget? = null) {
                    @Serializable
                    data class Budget(
                        val id: String? = null,
                        @SerialName("budget_amount") val budgetAmount: Float? = null,
                        @SerialName("prevent_further_usage") val preventFurtherUsage: Boolean? = null,
                        @SerialName("budget_alerting") val budgetAlerting: BudgetAlerting? = null,
                        @SerialName("budget_scope") val budgetScope: BudgetScope? = null,
                        @SerialName("budget_entity_name") val budgetEntityName: String? = null,
                        @SerialName("budget_type") val budgetType: BudgetType? = null,
                        @SerialName("budget_product_sku") val budgetProductSku: String? = null,
                    ) {
                        @Serializable
                        data class BudgetAlerting(
                            @SerialName("will_alert") val willAlert: Boolean,
                            @SerialName("alert_recipients") val alertRecipients: List<String>,
                        )

                        @Serializable
                        enum class BudgetScope {
                            @SerialName("enterprise")
                            Enterprise,
                            @SerialName("organization")
                            Organization,
                            @SerialName("repository")
                            Repository,
                            @SerialName("cost_center")
                            CostCenter;
                        }

                        @Serializable(with = BudgetType.Serializer::class)
                        sealed interface BudgetType {
                            @Serializable
                            enum class ProductPricing : BudgetType {
                                ProductPricing;
                            }

                            @Serializable
                            enum class SkuPricing : BudgetType {
                                SkuPricing;
                            }

                            object Serializer : KSerializer<BudgetType> {
                                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                                override val descriptor: SerialDescriptor =
                                    buildSerialDescriptor("io.github.nomisrev.api.Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResponse.Budget.BudgetType", PolymorphicKind.SEALED) {
                                        element("ProductPricing", BillingUpdateBudgetOrgResponse.Budget.BudgetType.ProductPricing.serializer().descriptor)
                                        element("SkuPricing", BillingUpdateBudgetOrgResponse.Budget.BudgetType.SkuPricing.serializer().descriptor)
                                    }

                                override fun deserialize(decoder: Decoder): BudgetType {
                                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                    return json.attemptDeserialize(
                                        value,
                                        ProductPricing::class to { decodeFromJsonElement(BillingUpdateBudgetOrgResponse.Budget.BudgetType.ProductPricing.serializer(), it) },
                                        SkuPricing::class to { decodeFromJsonElement(BillingUpdateBudgetOrgResponse.Budget.BudgetType.SkuPricing.serializer(), it) },
                                    )
                                }

                                override fun serialize(encoder: Encoder, value: BudgetType) = when(value) {
                                    is ProductPricing -> encoder.encodeSerializableValue(BillingUpdateBudgetOrgResponse.Budget.BudgetType.ProductPricing.serializer(), value)
                                    is SkuPricing -> encoder.encodeSerializableValue(BillingUpdateBudgetOrgResponse.Budget.BudgetType.SkuPricing.serializer(), value)
                                }
                            }
                        }
                    }
                }


                @Serializable
                enum class Scope {
                    @SerialName("enterprise")
                    Enterprise,
                    @SerialName("organization")
                    Organization,
                    @SerialName("repository")
                    Repository,
                    @SerialName("cost_center")
                    CostCenter;
                }

                sealed interface BillingGetAllBudgetsOrgResult {
                    data class OK(val value: GetAllBudgets) : BillingGetAllBudgetsOrgResult

                    data class Forbidden(val value: BasicError) : BillingGetAllBudgetsOrgResult

                    data class NotFound(val value: BasicError) : BillingGetAllBudgetsOrgResult

                    data class InternalServerError(val value: BasicError) : BillingGetAllBudgetsOrgResult
                }

                suspend fun billingGetAllBudgetsOrg(
                    org: String,
                    page: Long = 1L,
                    perPage: Long = 10L,
                    scope: Scope? = null,
                ): BillingGetAllBudgetsOrgResult

                sealed interface BillingGetBudgetOrgResult {
                    data class OK(val value: GetBudget) : BillingGetBudgetOrgResult

                    data class BadRequest(val value: BasicError) : BillingGetBudgetOrgResult

                    data class Forbidden(val value: BasicError) : BillingGetBudgetOrgResult

                    data class NotFound(val value: BasicError) : BillingGetBudgetOrgResult

                    data class InternalServerError(val value: BasicError) : BillingGetBudgetOrgResult

                    data class ServiceUnavailable(val value: BillingGetBudgetOrgResponse) : BillingGetBudgetOrgResult
                }

                suspend fun billingGetBudgetOrg(
                    org: String,
                    budgetId: String,
                ): BillingGetBudgetOrgResult

                sealed interface BillingDeleteBudgetOrgResult {
                    data class OK(val value: DeleteBudget) : BillingDeleteBudgetOrgResult

                    data class BadRequest(val value: BasicError) : BillingDeleteBudgetOrgResult

                    data class Forbidden(val value: BasicError) : BillingDeleteBudgetOrgResult

                    data class NotFound(val value: BasicError) : BillingDeleteBudgetOrgResult

                    data class InternalServerError(val value: BasicError) : BillingDeleteBudgetOrgResult

                    data class ServiceUnavailable(val value: BillingDeleteBudgetOrgResponse) : BillingDeleteBudgetOrgResult
                }

                suspend fun billingDeleteBudgetOrg(
                    org: String,
                    budgetId: String,
                ): BillingDeleteBudgetOrgResult

                sealed interface BillingUpdateBudgetOrgResult {
                    data class OK(val value: BillingUpdateBudgetOrgResponse) : BillingUpdateBudgetOrgResult

                    data class BadRequest(val value: BasicError) : BillingUpdateBudgetOrgResult

                    data class Unauthorized(val value: BasicError) : BillingUpdateBudgetOrgResult

                    data class Forbidden(val value: BasicError) : BillingUpdateBudgetOrgResult

                    data class NotFound(val value: BasicError) : BillingUpdateBudgetOrgResult

                    data class UnprocessableEntity(val value: ValidationError) : BillingUpdateBudgetOrgResult

                    data class InternalServerError(val value: BasicError) : BillingUpdateBudgetOrgResult
                }

                suspend fun billingUpdateBudgetOrg(
                    org: String,
                    budgetId: String,
                    body: BillingUpdateBudgetOrgBody,
                ): BillingUpdateBudgetOrgResult
            }

            interface PremiumRequest {
                val usage: Organizations.Settings.Billing.PremiumRequest.UsageApi

                interface UsageApi {
                    @Serializable
                    data class BillingGetGithubBillingPremiumRequestUsageReportOrgResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface BillingGetGithubBillingPremiumRequestUsageReportOrgResult {
                        data class OK(val value: BillingPremiumRequestUsageReportOrg) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult

                        data class BadRequest(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult

                        data class Forbidden(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult

                        data class NotFound(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult

                        data class InternalServerError(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult

                        data class ServiceUnavailable(val value: BillingGetGithubBillingPremiumRequestUsageReportOrgResponse) : BillingGetGithubBillingPremiumRequestUsageReportOrgResult
                    }

                    suspend fun billingGetGithubBillingPremiumRequestUsageReportOrg(
                        org: String,
                        day: Long? = null,
                        model: String? = null,
                        month: Long? = null,
                        product: String? = null,
                        user: String? = null,
                        year: Long? = null,
                    ): BillingGetGithubBillingPremiumRequestUsageReportOrgResult
                }
            }

            interface Usage {
                val summary: Organizations.Settings.Billing.Usage.Summary

                @Serializable
                data class BillingGetGithubBillingUsageReportOrgResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface BillingGetGithubBillingUsageReportOrgResult {
                    data class OK(val value: BillingUsageReport) : BillingGetGithubBillingUsageReportOrgResult

                    data class BadRequest(val value: BasicError) : BillingGetGithubBillingUsageReportOrgResult

                    data class Forbidden(val value: BasicError) : BillingGetGithubBillingUsageReportOrgResult

                    data class InternalServerError(val value: BasicError) : BillingGetGithubBillingUsageReportOrgResult

                    data class ServiceUnavailable(val value: BillingGetGithubBillingUsageReportOrgResponse) : BillingGetGithubBillingUsageReportOrgResult
                }

                suspend fun billingGetGithubBillingUsageReportOrg(
                    org: String,
                    day: Long? = null,
                    month: Long? = null,
                    year: Long? = null,
                ): BillingGetGithubBillingUsageReportOrgResult

                interface Summary {
                    @Serializable
                    data class BillingGetGithubBillingUsageSummaryReportOrgResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface BillingGetGithubBillingUsageSummaryReportOrgResult {
                        data class OK(val value: BillingUsageSummaryReportOrg) : BillingGetGithubBillingUsageSummaryReportOrgResult

                        data class BadRequest(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportOrgResult

                        data class Forbidden(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportOrgResult

                        data class InternalServerError(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportOrgResult

                        data class ServiceUnavailable(val value: BillingGetGithubBillingUsageSummaryReportOrgResponse) : BillingGetGithubBillingUsageSummaryReportOrgResult
                    }

                    suspend fun billingGetGithubBillingUsageSummaryReportOrg(
                        org: String,
                        day: Long? = null,
                        month: Long? = null,
                        product: String? = null,
                        repository: String? = null,
                        sku: String? = null,
                        year: Long? = null,
                    ): BillingGetGithubBillingUsageSummaryReportOrgResult
                }
            }
        }
    }
}

internal class KtorOrganizations(private val client: HttpClient) : Organizations {
    override val actions: Organizations.Actions = KtorOrganizationsActions(client)

    override val dependabot: Organizations.Dependabot = KtorOrganizationsDependabot(client)

    override val settings: Organizations.Settings = KtorOrganizationsSettings(client)

    override suspend fun orgsList(perPage: Long, since: Long?): Organizations.OrgsListResult {
        val response = client.get("/organizations") {
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.OrgsListResult.OK(response.body())
            HttpStatusCode.NotModified -> Organizations.OrgsListResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsActions(private val client: HttpClient) : Organizations.Actions {
    override val cache: Organizations.Actions.Cache = KtorOrganizationsActionsCache(client)
}

internal class KtorOrganizationsActionsCache(private val client: HttpClient) : Organizations.Actions.Cache {
    override val retentionLimit: Organizations.Actions.Cache.RetentionLimit = KtorOrganizationsActionsCacheRetentionLimit(client)

    override val storageLimit: Organizations.Actions.Cache.StorageLimit = KtorOrganizationsActionsCacheStorageLimit(client)
}

internal class KtorOrganizationsActionsCacheRetentionLimit(private val client: HttpClient) : Organizations.Actions.Cache.RetentionLimit {
    override suspend fun actionsGetActionsCacheRetentionLimitForOrganization(org: String): Organizations.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForOrganizationResult {
        val response = client.get("/organizations/$org/actions/cache/retention-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Organizations.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheRetentionLimitForOrganization(org: String, body: ActionsCacheRetentionLimitForOrganization): Organizations.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForOrganizationResult {
        val response = client.put("/organizations/$org/actions/cache/retention-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Organizations.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForOrganizationResult.NoContent
            HttpStatusCode.BadRequest -> Organizations.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForOrganizationResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsActionsCacheStorageLimit(private val client: HttpClient) : Organizations.Actions.Cache.StorageLimit {
    override suspend fun actionsGetActionsCacheStorageLimitForOrganization(org: String): Organizations.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForOrganizationResult {
        val response = client.get("/organizations/$org/actions/cache/storage-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForOrganizationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Organizations.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheStorageLimitForOrganization(org: String, body: ActionsCacheStorageLimitForOrganization): Organizations.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForOrganizationResult {
        val response = client.put("/organizations/$org/actions/cache/storage-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Organizations.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForOrganizationResult.NoContent
            HttpStatusCode.BadRequest -> Organizations.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForOrganizationResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForOrganizationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForOrganizationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsDependabot(private val client: HttpClient) : Organizations.Dependabot {
    override val repositoryAccess: Organizations.Dependabot.RepositoryAccess = KtorOrganizationsDependabotRepositoryAccess(client)
}

internal class KtorOrganizationsDependabotRepositoryAccess(private val client: HttpClient) : Organizations.Dependabot.RepositoryAccess {
    override val defaultLevel: Organizations.Dependabot.RepositoryAccess.DefaultLevel = KtorOrganizationsDependabotRepositoryAccessDefaultLevel(client)

    override suspend fun dependabotRepositoryAccessForOrg(org: String, page: Long, perPage: Long): Organizations.Dependabot.RepositoryAccess.DependabotRepositoryAccessForOrgResult {
        val response = client.get("/organizations/$org/dependabot/repository-access") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Dependabot.RepositoryAccess.DependabotRepositoryAccessForOrgResult.OK(response.body())
            HttpStatusCode.Forbidden -> Organizations.Dependabot.RepositoryAccess.DependabotRepositoryAccessForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Dependabot.RepositoryAccess.DependabotRepositoryAccessForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun dependabotUpdateRepositoryAccessForOrg(org: String, body: Organizations.Dependabot.RepositoryAccess.DependabotUpdateRepositoryAccessForOrgBody): Organizations.Dependabot.RepositoryAccess.DependabotUpdateRepositoryAccessForOrgResult {
        val response = client.patch("/organizations/$org/dependabot/repository-access") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Organizations.Dependabot.RepositoryAccess.DependabotUpdateRepositoryAccessForOrgResult.NoContent
            HttpStatusCode.Forbidden -> Organizations.Dependabot.RepositoryAccess.DependabotUpdateRepositoryAccessForOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Dependabot.RepositoryAccess.DependabotUpdateRepositoryAccessForOrgResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsDependabotRepositoryAccessDefaultLevel(private val client: HttpClient) : Organizations.Dependabot.RepositoryAccess.DefaultLevel {
    override suspend fun dependabotSetRepositoryAccessDefaultLevel(org: String, body: Organizations.Dependabot.RepositoryAccess.DefaultLevel.DependabotSetRepositoryAccessDefaultLevelBody): Organizations.Dependabot.RepositoryAccess.DefaultLevel.DependabotSetRepositoryAccessDefaultLevelResult {
        val response = client.put("/organizations/$org/dependabot/repository-access/default-level") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Organizations.Dependabot.RepositoryAccess.DefaultLevel.DependabotSetRepositoryAccessDefaultLevelResult.NoContent
            HttpStatusCode.Forbidden -> Organizations.Dependabot.RepositoryAccess.DefaultLevel.DependabotSetRepositoryAccessDefaultLevelResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Dependabot.RepositoryAccess.DefaultLevel.DependabotSetRepositoryAccessDefaultLevelResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsSettings(private val client: HttpClient) : Organizations.Settings {
    override val billing: Organizations.Settings.Billing = KtorOrganizationsSettingsBilling(client)
}

internal class KtorOrganizationsSettingsBilling(private val client: HttpClient) : Organizations.Settings.Billing {
    override val budgets: Organizations.Settings.Billing.Budgets = KtorOrganizationsSettingsBillingBudgets(client)

    override val premiumRequest: Organizations.Settings.Billing.PremiumRequest = KtorOrganizationsSettingsBillingPremiumRequest(client)

    override val usage: Organizations.Settings.Billing.Usage = KtorOrganizationsSettingsBillingUsage(client)
}

internal class KtorOrganizationsSettingsBillingBudgets(private val client: HttpClient) : Organizations.Settings.Billing.Budgets {
    override suspend fun billingGetAllBudgetsOrg(org: String, page: Long, perPage: Long, scope: Organizations.Settings.Billing.Budgets.Scope?): Organizations.Settings.Billing.Budgets.BillingGetAllBudgetsOrgResult {
        val response = client.get("/organizations/$org/settings/billing/budgets") {
            parameter("page", page)
            parameter("per_page", perPage)
            scope?.let { parameter("scope", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Budgets.BillingGetAllBudgetsOrgResult.OK(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Budgets.BillingGetAllBudgetsOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Settings.Billing.Budgets.BillingGetAllBudgetsOrgResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Budgets.BillingGetAllBudgetsOrgResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun billingGetBudgetOrg(org: String, budgetId: String): Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult {
        val response = client.get("/organizations/$org/settings/billing/budgets/$budgetId")
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Organizations.Settings.Billing.Budgets.BillingGetBudgetOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun billingDeleteBudgetOrg(org: String, budgetId: String): Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult {
        val response = client.delete("/organizations/$org/settings/billing/budgets/$budgetId")
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Organizations.Settings.Billing.Budgets.BillingDeleteBudgetOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun billingUpdateBudgetOrg(org: String, budgetId: String, body: Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgBody): Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult {
        val response = client.patch("/organizations/$org/settings/billing/budgets/$budgetId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Budgets.BillingUpdateBudgetOrgResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsSettingsBillingPremiumRequest(private val client: HttpClient) : Organizations.Settings.Billing.PremiumRequest {
    override val usage: Organizations.Settings.Billing.PremiumRequest.UsageApi = KtorOrganizationsSettingsBillingPremiumRequestUsageApi(client)
}

internal class KtorOrganizationsSettingsBillingPremiumRequestUsageApi(private val client: HttpClient) : Organizations.Settings.Billing.PremiumRequest.UsageApi {
    override suspend fun billingGetGithubBillingPremiumRequestUsageReportOrg(org: String, day: Long?, model: String?, month: Long?, product: String?, user: String?, year: Long?): Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult {
        val response = client.get("/organizations/$org/settings/billing/premium_request/usage") {
            day?.let { parameter("day", it) }
            model?.let { parameter("model", it) }
            month?.let { parameter("month", it) }
            product?.let { parameter("product", it) }
            user?.let { parameter("user", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Organizations.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsSettingsBillingUsage(private val client: HttpClient) : Organizations.Settings.Billing.Usage {
    override val summary: Organizations.Settings.Billing.Usage.Summary = KtorOrganizationsSettingsBillingUsageSummary(client)

    override suspend fun billingGetGithubBillingUsageReportOrg(org: String, day: Long?, month: Long?, year: Long?): Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult {
        val response = client.get("/organizations/$org/settings/billing/usage") {
            day?.let { parameter("day", it) }
            month?.let { parameter("month", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult.Forbidden(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Organizations.Settings.Billing.Usage.BillingGetGithubBillingUsageReportOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorOrganizationsSettingsBillingUsageSummary(private val client: HttpClient) : Organizations.Settings.Billing.Usage.Summary {
    override suspend fun billingGetGithubBillingUsageSummaryReportOrg(org: String, day: Long?, month: Long?, product: String?, repository: String?, sku: String?, year: Long?): Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult {
        val response = client.get("/organizations/$org/settings/billing/usage/summary") {
            day?.let { parameter("day", it) }
            month?.let { parameter("month", it) }
            product?.let { parameter("product", it) }
            repository?.let { parameter("repository", it) }
            sku?.let { parameter("sku", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult.OK(response.body())
            HttpStatusCode.BadRequest -> Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult.Forbidden(response.body())
            HttpStatusCode.InternalServerError -> Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Organizations.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportOrgResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
