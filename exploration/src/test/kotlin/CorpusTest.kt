package exploration

import de.infix.testBalloon.framework.core.testSuite
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import java.nio.file.Files
import kotlin.test.*
import kotlinx.serialization.json.*

val corpusTests by testSuite {
    test("catalog paths encode spaces without double encoding and reuse cached bytes") {
        for (version in listOf("version unknown", "version%20unknown")) {
            val directory = Files.createTempDirectory("corpus-test").toFile()
            var requests = 0
            HttpClient(MockEngine { request ->
                requests++
                assertEquals("https://api.apis.guru/v2/specs/example/version%20unknown/swagger.json", request.url.toString())
                assertEquals("OpenAPI-kt-corpus/1.0", request.headers["User-Agent"])
                respond("{}")
            }).use { client ->
                try {
                    val target = directory.resolve("spec.json")
                    repeat(2) {
                        assertContentEquals("{}".toByteArray(), download(client, "https://api.apis.guru/v2/specs/example/$version/swagger.json", target))
                    }
                    assertEquals("{}", target.readText())
                    assertEquals(1, requests)
                } finally { directory.deleteRecursively() }
            }
        }
    }
    test("failed downloads retry and never cache invalid JSON or oversized documents") {
        for (body in listOf("invalid", "{\"long\":true}")) {
            val directory = Files.createTempDirectory("corpus-test").toFile()
            var requests = 0
            HttpClient(MockEngine { requests++; respond(body) }).use { client ->
                try {
                    val target = directory.resolve("spec.json")
                    assertFails { download(client, "https://api.apis.guru/spec.json", target, maxBytes = 8) }
                    assertEquals(3, requests)
                    assertFalse(target.exists())
                } finally { directory.deleteRecursively() }
            }
        }
    }
    test("HTTP errors retry and unexpected hosts are rejected") {
        val directory = Files.createTempDirectory("corpus-test").toFile()
        var requests = 0
        HttpClient(MockEngine {
            requests++
            if (requests == 1) respond("{}", HttpStatusCode.ServiceUnavailable) else respond("{}")
        }).use { client ->
            try {
                val target = directory.resolve("spec.json")
                assertFailsWith<IllegalArgumentException> { download(client, "https://example.com/spec.json", target) }
                assertEquals(0, requests)
                download(client, "https://api.apis.guru/spec.json", target)
                assertEquals(2, requests)
            } finally { directory.deleteRecursively() }
        }
    }
    test("selection keeps preferred versions, applies filters, and uses stable hashes") {
        val index = Json.parseToJsonElement("""{
            "example": {"preferred":"v2", "versions": {
                "v1":{"openapiVer":"2.0","swaggerUrl":"https://api.apis.guru/v1.json"},
                "v2":{"openapiVer":"3.0.0","swaggerUrl":"https://api.apis.guru/v2.json"}}},
            "other": {"preferred":"v1", "versions": {
                "v1":{"openapiVer":"3.1.0","swaggerUrl":"https://api.apis.guru/other.json"}}}
        }""").jsonObject
        val preferred = selectEntries(index, CorpusOptions())
        assertEquals(2, preferred.size)
        assertEquals("v2", preferred.single { it.string("api") == "example" }.string("version"))
        val all = selectEntries(index, CorpusOptions(allVersions = true))
        assertEquals(3, all.size)
        val hashes = all.map { digest("${it.string("api")}\u0000${it.string("version")}".toByteArray()) }
        assertEquals(hashes.sorted(), hashes)
        assertEquals(all.take(1), selectEntries(index, CorpusOptions(allVersions = true, limit = 1)))
        assertEquals(1, selectEntries(index, CorpusOptions(filter = "other")).size)
    }
    test("shards split the selection into disjoint parts that together are the whole selection") {
        val index = JsonObject((1..10).associate { n ->
            "api$n" to Json.parseToJsonElement("""{"preferred":"v1","versions":{"v1":{"openapiVer":"3.0.0","swaggerUrl":"https://api.apis.guru/$n.json"}}}""")
        })
        val whole = selectEntries(index, CorpusOptions(limit = 7))
        val shards = (1..3).map { selectEntries(index, CorpusOptions(limit = 7, shard = it, shards = 3)) }
        assertEquals(whole.toSet(), shards.flatten().toSet())
        assertEquals(whole.size, shards.sumOf { it.size })
        assertTrue(shards.all { it.size in 2..3 })
    }
    test("invalid CLI bounds are rejected") {
        for (args in listOf(arrayOf("--jobs", "0"), arrayOf("--timeout", "0"), arrayOf("--limit", "-1"), arrayOf("--unknown"), arrayOf("--output"),
            arrayOf("--shard", "0/4"), arrayOf("--shard", "5/4"), arrayOf("--shard", "1"), arrayOf("--shard", "a/b"))) {
            assertFails { parseOptions(args) }
        }
        assertEquals(4, parseOptions(arrayOf("--jobs", "4", "--fetch-only")).jobs)
        val sharded = parseOptions(arrayOf("--shard", "2/4"))
        assertEquals(2 to 4, sharded.shard to sharded.shards)
        assertTrue(parseOptions(arrayOf("--summarize-only")).summarizeOnly)
    }
    test("summary counts compiler diagnostics once per document and groups phase failures") {
        val directory = Files.createTempDirectory("corpus-test").toFile()
        try {
            directory.resolve("results/key").mkdirs()
            directory.resolve("results/key/compile.log").writeText("a: error: broken\nb: error: broken\n")
            directory.resolve("results.jsonl").writeText("""{"api":"example","key":"key","fetch":"pass","worker":"pass","compile":"fail","phases":[{"phase":"render","status":"fail","exception":"Oops","message":"bad\ntrace"}],"diagnostics":[{"severity":"Error"},{"severity":"Warning"}]}""" + "\n" +
                """{"api":"other","fetch":"pass","status":"unsupported-dialect"}""" + "\n")
            val summary = summarize(directory)
            assertEquals(2, summary.getValue("documents").jsonPrimitive.int)
            assertEquals(1, summary.getValue("counts").jsonObject.getValue("generation-errors").jsonPrimitive.int)
            assertEquals("render: Oops: bad", summary.getValue("failure_groups").jsonArray.single().jsonObject.string("signature"))
            assertEquals(1, summary.getValue("compiler_failure_groups").jsonArray.single().jsonObject.getValue("documents").jsonPrimitive.int)
        } finally { directory.deleteRecursively() }
    }
}
