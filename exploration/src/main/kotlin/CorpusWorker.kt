package exploration

import io.github.nomisrev.openapi.*
import io.github.nomisrev.openapi.parser.OpenAPI
import java.io.File
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*

/** One document per process: the driver can kill a stuck or memory-hungry transformation. */
fun main(args: Array<String>) = runBlocking {
    val input = File(args[0])
    val output = File(args[1]).apply { mkdirs() }
    val results = mutableListOf<JsonObject>()
    fun save() = output.resolve("phases.json").writeText(JsonArray(results).toString())
    fun <T> phase(name: String, block: () -> T): T {
        output.resolve("active-phase.txt").writeText(name)
        val start = System.nanoTime()
        try {
            val value = block()
            results += buildJsonObject {
                put("phase", name); put("status", "pass")
                put("milliseconds", (System.nanoTime() - start) / 1_000_000)
            }
            save()
            return value
        } catch (failure: Throwable) {
            output.resolve("failure.txt").writeText(failure.stackTraceToString())
            results += buildJsonObject {
                put("phase", name); put("status", "fail")
                put("exception", failure.javaClass.name)
                put("message", failure.message.orEmpty())
            }
            save()
            throw failure
        }
    }
    val api = phase("parse") { OpenAPI.fromJson(input.readText()) }
    val tree = phase("typed") { runBlocking { api.toApiTree() } }
    val result = phase("render") {
        tree.renderWithDiagnostics(RenderConfig(targets = setOf(KmpTarget.JVM)))
    }
    output.resolve("diagnostics.json").writeText(JsonArray(result.diagnostics.map {
        buildJsonObject { put("severity", it.severity.name); put("message", it.message) }
    }).toString())
    phase("emit") {
        // Compared ignoring case: `Users.kt` and `USERS.kt` are one file on a case-insensitive file system.
        val names = result.files.map { "${it.packageName}.${it.name}".lowercase() }
        check(names.distinct().size == names.size) {
            "Duplicate generated files: ${names.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }
        result.files.forEach { it.writeTo(output.resolve("sources")) }
    }
}
