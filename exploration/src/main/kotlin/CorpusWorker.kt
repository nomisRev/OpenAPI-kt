package exploration

import io.github.nomisrev.openapi.*
import io.github.nomisrev.openapi.parser.OpenAPI
import java.io.File
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*

/** One document per process: the driver can kill a stuck or memory-hungry transformation. */
fun main(args: Array<String>) = work(File(args[0]), File(args[1]))

/**
 * Runs the phases of one document, records them in `phases.json`, and throws the failure of the phase that fails.
 * A document whose generation reports an `Error` fails the `diagnostics` phase and emits nothing, like the Gradle task.
 */
internal fun work(input: File, outputDirectory: File) = runBlocking {
    val output = outputDirectory.apply { mkdirs() }
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
    phase("diagnostics") {
        // Not `throwOnErrors`: its first line is the same for every document, and failures are grouped by first line.
        val errors = result.diagnostics.filter { it.severity == GenerationDiagnosticSeverity.Error }
        check(errors.isEmpty()) {
            errors.first().message + if (errors.size > 1) " (+${errors.size - 1} more errors)" else ""
        }
    }
    phase("emit") {
        // Compared ignoring case: `Users.kt` and `USERS.kt` are one file on a case-insensitive file system.
        val names = result.files.map { "${it.packageName}.${it.name}".lowercase() }
        check(names.distinct().size == names.size) {
            "Duplicate generated files: ${names.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }
        result.files.forEach { it.writeTo(output.resolve("sources")) }
    }
}
