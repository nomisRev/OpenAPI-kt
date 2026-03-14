package io.github.nomisrev.api

import io.github.nomisrev.model.MarketplacePurchase
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.MarketplaceListingPlan
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface MarketplaceListing {
    val accounts: MarketplaceListing.Accounts

    val plans: MarketplaceListing.Plans

    val stubbed: MarketplaceListing.Stubbed

    interface Accounts {
        sealed interface AppsGetSubscriptionPlanForAccountResult {
            data class OK(val value: MarketplacePurchase) : AppsGetSubscriptionPlanForAccountResult

            data class Unauthorized(val value: BasicError) : AppsGetSubscriptionPlanForAccountResult

            data class NotFound(val value: BasicError) : AppsGetSubscriptionPlanForAccountResult
        }

        suspend fun appsGetSubscriptionPlanForAccount(
            accountId: Long,
        ): AppsGetSubscriptionPlanForAccountResult
    }

    interface Plans {
        val accounts: MarketplaceListing.Plans.AccountsApi

        sealed interface AppsListPlansResult {
            data class OK(val value: List<MarketplaceListingPlan>) : AppsListPlansResult

            data class Unauthorized(val value: BasicError) : AppsListPlansResult

            data class NotFound(val value: BasicError) : AppsListPlansResult
        }

        suspend fun appsListPlans(
            page: Long = 1L,
            perPage: Long = 30L,
        ): AppsListPlansResult

        interface AccountsApi {
            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable
            enum class Sort {
                @SerialName("created") Created, @SerialName("updated") Updated;
            }

            sealed interface AppsListAccountsForPlanResult {
                data class OK(val value: List<MarketplacePurchase>) : AppsListAccountsForPlanResult

                data class Unauthorized(val value: BasicError) : AppsListAccountsForPlanResult

                data class NotFound(val value: BasicError) : AppsListAccountsForPlanResult

                data class UnprocessableEntity(val value: ValidationError) : AppsListAccountsForPlanResult
            }

            suspend fun appsListAccountsForPlan(
                planId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                direction: Direction? = null,
            ): AppsListAccountsForPlanResult
        }
    }

    interface Stubbed {
        val accounts: MarketplaceListing.Stubbed.AccountsApi

        val plans: MarketplaceListing.Stubbed.PlansApi

        interface AccountsApi {
            sealed interface AppsGetSubscriptionPlanForAccountStubbedResult {
                data class OK(val value: MarketplacePurchase) : AppsGetSubscriptionPlanForAccountStubbedResult

                data class Unauthorized(val value: BasicError) : AppsGetSubscriptionPlanForAccountStubbedResult

                data object NotFound : AppsGetSubscriptionPlanForAccountStubbedResult
            }

            suspend fun appsGetSubscriptionPlanForAccountStubbed(
                accountId: Long,
            ): AppsGetSubscriptionPlanForAccountStubbedResult
        }

        interface PlansApi {
            val accounts: MarketplaceListing.Stubbed.PlansApi.AccountsApi2

            sealed interface AppsListPlansStubbedResult {
                data class OK(val value: List<MarketplaceListingPlan>) : AppsListPlansStubbedResult

                data class Unauthorized(val value: BasicError) : AppsListPlansStubbedResult
            }

            suspend fun appsListPlansStubbed(
                page: Long = 1L,
                perPage: Long = 30L,
            ): AppsListPlansStubbedResult

            interface AccountsApi2 {
                @Serializable
                enum class Direction {
                    @SerialName("asc") Asc, @SerialName("desc") Desc;
                }


                @Serializable
                enum class Sort {
                    @SerialName("created") Created, @SerialName("updated") Updated;
                }

                sealed interface AppsListAccountsForPlanStubbedResult {
                    data class OK(val value: List<MarketplacePurchase>) : AppsListAccountsForPlanStubbedResult

                    data class Unauthorized(val value: BasicError) : AppsListAccountsForPlanStubbedResult
                }

                suspend fun appsListAccountsForPlanStubbed(
                    planId: Long,
                    page: Long = 1L,
                    perPage: Long = 30L,
                    sort: Sort = Sort.Created,
                    direction: Direction? = null,
                ): AppsListAccountsForPlanStubbedResult
            }
        }
    }
}

internal class KtorMarketplaceListing(private val client: HttpClient) : MarketplaceListing {
    override val accounts: MarketplaceListing.Accounts = KtorMarketplaceListingAccounts(client)

    override val plans: MarketplaceListing.Plans = KtorMarketplaceListingPlans(client)

    override val stubbed: MarketplaceListing.Stubbed = KtorMarketplaceListingStubbed(client)
}

internal class KtorMarketplaceListingAccounts(private val client: HttpClient) : MarketplaceListing.Accounts {
    override suspend fun appsGetSubscriptionPlanForAccount(accountId: Long): MarketplaceListing.Accounts.AppsGetSubscriptionPlanForAccountResult {
        val response = client.get("/marketplace_listing/accounts/$accountId")
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Accounts.AppsGetSubscriptionPlanForAccountResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Accounts.AppsGetSubscriptionPlanForAccountResult.Unauthorized(response.body())
            HttpStatusCode.NotFound -> MarketplaceListing.Accounts.AppsGetSubscriptionPlanForAccountResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarketplaceListingPlans(private val client: HttpClient) : MarketplaceListing.Plans {
    override val accounts: MarketplaceListing.Plans.AccountsApi = KtorMarketplaceListingPlansAccountsApi(client)

    override suspend fun appsListPlans(page: Long, perPage: Long): MarketplaceListing.Plans.AppsListPlansResult {
        val response = client.get("/marketplace_listing/plans") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Plans.AppsListPlansResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Plans.AppsListPlansResult.Unauthorized(response.body())
            HttpStatusCode.NotFound -> MarketplaceListing.Plans.AppsListPlansResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarketplaceListingPlansAccountsApi(private val client: HttpClient) : MarketplaceListing.Plans.AccountsApi {
    override suspend fun appsListAccountsForPlan(planId: Long, page: Long, perPage: Long, sort: MarketplaceListing.Plans.AccountsApi.Sort, direction: MarketplaceListing.Plans.AccountsApi.Direction?): MarketplaceListing.Plans.AccountsApi.AppsListAccountsForPlanResult {
        val response = client.get("/marketplace_listing/plans/$planId/accounts") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            direction?.let { parameter("direction", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Plans.AccountsApi.AppsListAccountsForPlanResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Plans.AccountsApi.AppsListAccountsForPlanResult.Unauthorized(response.body())
            HttpStatusCode.NotFound -> MarketplaceListing.Plans.AccountsApi.AppsListAccountsForPlanResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> MarketplaceListing.Plans.AccountsApi.AppsListAccountsForPlanResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarketplaceListingStubbed(private val client: HttpClient) : MarketplaceListing.Stubbed {
    override val accounts: MarketplaceListing.Stubbed.AccountsApi = KtorMarketplaceListingStubbedAccountsApi(client)

    override val plans: MarketplaceListing.Stubbed.PlansApi = KtorMarketplaceListingStubbedPlansApi(client)
}

internal class KtorMarketplaceListingStubbedAccountsApi(private val client: HttpClient) : MarketplaceListing.Stubbed.AccountsApi {
    override suspend fun appsGetSubscriptionPlanForAccountStubbed(accountId: Long): MarketplaceListing.Stubbed.AccountsApi.AppsGetSubscriptionPlanForAccountStubbedResult {
        val response = client.get("/marketplace_listing/stubbed/accounts/$accountId")
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Stubbed.AccountsApi.AppsGetSubscriptionPlanForAccountStubbedResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Stubbed.AccountsApi.AppsGetSubscriptionPlanForAccountStubbedResult.Unauthorized(response.body())
            HttpStatusCode.NotFound -> MarketplaceListing.Stubbed.AccountsApi.AppsGetSubscriptionPlanForAccountStubbedResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarketplaceListingStubbedPlansApi(private val client: HttpClient) : MarketplaceListing.Stubbed.PlansApi {
    override val accounts: MarketplaceListing.Stubbed.PlansApi.AccountsApi2 = KtorMarketplaceListingStubbedPlansApiAccountsApi2(client)

    override suspend fun appsListPlansStubbed(page: Long, perPage: Long): MarketplaceListing.Stubbed.PlansApi.AppsListPlansStubbedResult {
        val response = client.get("/marketplace_listing/stubbed/plans") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Stubbed.PlansApi.AppsListPlansStubbedResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Stubbed.PlansApi.AppsListPlansStubbedResult.Unauthorized(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorMarketplaceListingStubbedPlansApiAccountsApi2(private val client: HttpClient) : MarketplaceListing.Stubbed.PlansApi.AccountsApi2 {
    override suspend fun appsListAccountsForPlanStubbed(planId: Long, page: Long, perPage: Long, sort: MarketplaceListing.Stubbed.PlansApi.AccountsApi2.Sort, direction: MarketplaceListing.Stubbed.PlansApi.AccountsApi2.Direction?): MarketplaceListing.Stubbed.PlansApi.AccountsApi2.AppsListAccountsForPlanStubbedResult {
        val response = client.get("/marketplace_listing/stubbed/plans/$planId/accounts") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            direction?.let { parameter("direction", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> MarketplaceListing.Stubbed.PlansApi.AccountsApi2.AppsListAccountsForPlanStubbedResult.OK(response.body())
            HttpStatusCode.Unauthorized -> MarketplaceListing.Stubbed.PlansApi.AccountsApi2.AppsListAccountsForPlanStubbedResult.Unauthorized(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
