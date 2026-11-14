package io.github.nomisrev.render

import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.serverNames
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The type name of each server of a list (`Outer`, or `Outer.Inner` when servers told apart by their scheme share
 * `Outer`). The real cases are the server lists of the API Guru documents named in each test.
 */
class ServerNamesTest {
    private fun server(url: String, description: String? = null, vararg variables: String) =
        Server(
            url = url,
            description = description,
            variables = variables.takeIf { it.isNotEmpty() }?.associateWith { Server.Variable(default = "d") },
        )

    private fun List<Server>.names(): List<String> = serverNames().names.map { it.path.joinToString(".") }

    private val identifier = Regex("[\\p{L}][\\p{L}\\p{N}]*")

    @Test
    fun serversThatDifferOnlyInTheSchemeAreNestedByScheme() {
        val servers = listOf(server("http://api.example.com"), server("https://api.example.com"))
        assertEquals(listOf("ApiExample.Http", "ApiExample.Https"), servers.names())
        assertEquals(listOf("ApiExample.Https", "ApiExample.Http"), servers.reversed().names())
        assertEquals(emptyList(), servers.serverNames().warnings)
    }

    @Test
    fun theSchemeOfAServerWithVariablesIsNestedToo() {
        val servers = listOf(
            server("http://{region}.x.com", "The multi-region endpoint", "region"),
            server("https://{region}.x.com", "The multi-region endpoint", "region"),
        )
        assertEquals(listOf("TheMultiRegionEndpoint.Http", "TheMultiRegionEndpoint.Https"), servers.names())
    }

    @Test
    fun schemesOtherThanHttp() {
        assertEquals(
            listOf("Echo.Ws", "Echo.Wss", "Echo.Https"),
            listOf(server("ws://e.com", "Echo"), server("wss://e.com", "Echo"), server("https://e.com", "Echo")).names(),
        )
        assertEquals(
            listOf("Echo.Relative", "Echo.Https"),
            listOf(server("//e.com", "Echo"), server("https://e.com", "Echo")).names(),
        )
        assertEquals(
            listOf("Echo.Scheme", "Echo.Https"),
            listOf(server("{scheme}://e.com", "Echo", "scheme"), server("https://e.com", "Echo")).names(),
        )
    }

    @Test
    fun linodeV4AndV4beta() {
        val servers = listOf(server("https://api.linode.com/v4"), server("https://api.linode.com/v4beta"))
        assertEquals(listOf("ApiLinodeV4", "ApiLinodeV4beta"), servers.names())
    }

    @Test
    fun ebayProductionTwice() {
        val servers = listOf(
            server("https://api.ebay.com{basePath}", "Production", "basePath"),
            server("https://apiz.ebay.com{basePath}", "Production", "basePath"),
        )
        assertEquals(listOf("ProductionApi", "ProductionApiz"), servers.names())
    }

    @Test
    fun shotstackServeIsTheOneWithTheExtraWord() {
        val servers = listOf(
            server("https://api.shotstack.io/{version}", null, "version"),
            server("https://api.shotstack.io/serve/{version}", null, "version"),
        )
        assertEquals(listOf("ApiShotstack", "ApiShotstackServe"), servers.names())
    }

    @Test
    fun vtexWorkspace() {
        val servers = listOf(
            server("https://vtex.local"),
            server("https://{account}.myvtex.com", null, "account"),
            server("https://{workspace}--{account}.myvtex.com", null, "workspace", "account"),
        )
        assertEquals(listOf("VtexLocal", "MyvtexCom", "MyvtexComWorkspace"), servers.names())
    }

    @Test
    fun nowpaymentsIoAndIoo() {
        val servers = listOf(server("https://api.nowpayments.io"), server("https://api.nowpayments.ioo"))
        assertEquals(listOf("ApiNowpaymentsIo", "ApiNowpaymentsIoo"), servers.names())
    }

    @Test
    fun amazonS3SplitsByTheDifferingWordsThenNestsTheScheme() {
        val multi = "The Amazon S3 multi-region endpoint"
        val general = "The general Amazon S3 endpoint for US East (N. Virginia)"
        val china = "The Amazon S3 endpoint for China (Beijing) and China (Ningxia)"
        val servers = listOf(
            server("http://s3{dash-or-dot}{region}.amazonaws.com", multi, "region", "dash-or-dot"),
            server("https://s3{dash-or-dot}{region}.amazonaws.com", multi, "region", "dash-or-dot"),
            server("http://s3.amazonaws.com", general),
            server("https://s3.amazonaws.com", general),
            server("http://s3.{region}.amazonaws.com", multi, "region"),
            server("https://s3.{region}.amazonaws.com", multi, "region"),
            server("http://s3.{region}.amazonaws.com.cn", china, "region"),
            server("https://s3.{region}.amazonaws.com.cn", china, "region"),
        )
        assertEquals(
            listOf(
                "TheAmazonS3MultiRegionEndpointDashOrDot.Http", "TheAmazonS3MultiRegionEndpointDashOrDot.Https",
                "TheGeneralAmazonS3EndpointForUSEastNVirginia.Http", "TheGeneralAmazonS3EndpointForUSEastNVirginia.Https",
                "TheAmazonS3MultiRegionEndpoint.Http", "TheAmazonS3MultiRegionEndpoint.Https",
                "TheAmazonS3EndpointForChinaBeijingAndChinaNingxia.Http",
                "TheAmazonS3EndpointForChinaBeijingAndChinaNingxia.Https",
            ),
            servers.names(),
        )
        assertEquals(emptyList(), servers.serverNames().warnings)
    }

    @Test
    fun charactersOutsideLettersAndDigitsAreWordBreaks() {
        val servers = listOf(
            server("https://a.example.com", "Production (gated)"),
            server("http://localhost:5000"),
            server("https://b.example.com", "The endpoint for China (Beijing) and China (Ningxia)"),
            server("https://c.example.com", "Global endpoint (default)."),
        )
        assertEquals(
            listOf("ProductionGated", "Localhost5000", "TheEndpointForChinaBeijingAndChinaNingxia", "GlobalEndpointDefault"),
            servers.names(),
        )
    }

    @Test
    fun aNameThatStartsWithADigitIsLedByServer() {
        val servers = listOf(server("https://a.example.com", "1Password"), server("https://b.example.com", "Other"))
        assertEquals(listOf("Server1Password", "Other"), servers.names())
    }

    @Test
    fun anEmptyOrBlankDescriptionIsNamedAfterTheUrl() {
        val servers = listOf(
            server("https://mock.example.com", "Mock"),
            server("https://api.example.com", ""),
            server("https://other.example.com", "   "),
            server("https://third.example.com", " server"),
            server("https://fourth.example.com", "-"),
        )
        assertEquals(listOf("Mock", "ApiExample", "OtherExample", "ThirdExample", "FourthExample"), servers.names())
    }

    @Test
    fun noWordAtAllIsDefault() {
        assertEquals(listOf("Default", "Other"), listOf(server("/"), server("https://other", "Other")).names())
    }

    @Test
    fun aServerNamedCustomDoesNotClashWithTheBuiltInCase() {
        val servers = listOf(server("https://a.example.com", "Custom"), server("https://b.example.com", "Backup"))
        val names = servers.serverNames()
        assertEquals(listOf("CustomServer", "Backup"), names.names.map { it.path.joinToString(".") })
        assertEquals(1, names.warnings.size)
        // The rename is itself a name that can be taken.
        val taken = listOf(server("https://a.example.com", "Custom"), server("https://b.example.com", "CustomServer"))
        assertEquals(listOf("CustomServer", "CustomServer2"), taken.names())
    }

    @Test
    fun theSameServerTwiceIsNumberedAndWarned() {
        val servers = listOf(server("https://x.example.com"), server("https://x.example.com"), server("https://y.example.com"))
        val names = servers.serverNames()
        assertEquals(listOf("XExample", "XExample2", "YExample"), names.names.map { it.path.joinToString(".") })
        assertEquals(1, names.warnings.size)
        assertTrue("'https://x.example.com'" in names.warnings.single() && "XExample2" in names.warnings.single())
    }

    @Test
    fun sameUrlsWithTheSameWordsFallBackToNumbers() {
        val servers = listOf(server("https://x.example.com/a/b", "Api"), server("https://x.example.com/b/a", "Api"))
        assertEquals(listOf("Api", "Api2"), servers.names())
    }

    @Test
    fun namesThatDoNotClashAreUnchanged() {
        val servers = listOf(
            server("https://api.example.com", "Production server"),
            server("https://staging-api.example.com", "Staging server"),
            server("https://backup-api.example.com", "Backup"),
        )
        assertEquals(listOf("Production", "Staging", "Backup"), servers.names())
    }

    @Test
    fun everyNameIsAnIdentifier() {
        val servers = listOf(
            server("https://a.example.com", "Production (gated)"),
            server("http://localhost:5000"),
            server("https://c.example.com", "1Password"),
            server("https://d.example.com", "$%&"),
            server("https://e.example.com", "Custom"),
        )
        for (name in servers.serverNames().names) {
            for (part in name.path) assertTrue(identifier.matches(part), part)
        }
    }
}
