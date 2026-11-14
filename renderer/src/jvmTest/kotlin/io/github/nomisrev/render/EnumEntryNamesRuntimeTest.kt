package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.`enum`.entry.names.Api
import io.github.nomisrev.render.test.client.`enum`.entry.names.ApiServer
import io.github.nomisrev.render.test.client.`enum`.entry.names.Items
import io.github.nomisrev.render.test.client.`enum`.entry.names.Operator
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * An enum query parameter whose values share a Kotlin name sends the value that was asked for, and the default is the
 * entry of the default value, not of the value that has the same name (golden `kotlinTestData/client/enum-entry-names`).
 */
class EnumEntryNamesRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
        }
    )

    @Test
    fun theDefaultsAreTheEntriesOfTheirOwnValues() = runBlocking {
        api.items.get()

        assertEquals("ordering=-last_activity&operator=%3C%3D&kind=", requests.last().url.encodedQuery)
    }

    @Test
    fun everyValueGoesOverTheWireAsItself() = runBlocking {
        api.items.get(
            ordering = Items.Get.Ordering.LastActivity,
            operator = Operator.GreaterThanOrEqual,
            kind = Items.Get.Kind.Spiders,
        )
        assertEquals("ordering=last_activity&operator=%3E%3D&kind=spiders", requests.last().url.encodedQuery)

        api.items.get(ordering = Items.Get.Ordering.MinusLastActivity, operator = Operator.LessThan)
        assertEquals("ordering=-last_activity&operator=%3C&kind=", requests.last().url.encodedQuery)
    }

    @Test
    fun theServerVariableDefaultAndItsOtherValue() {
        assertEquals("https://aws-kms.example.com", ApiServer().url)
        assertEquals("https://aws:kms.example.com", ApiServer(ApiServer.Kind.AwsColonKms).url)
    }
}
