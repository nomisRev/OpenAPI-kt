package io.github.nomisrev.openapi.roundtrip

import io.github.nomisrev.openapi.parser.OpenAPI
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File
import kotlin.test.fail

/**
 * Everything the harness needs to know about one generated client.
 *
 * @property json the `Json` the generated client is configured with. Generated clients install Ktor's
 * `ContentNegotiation { json() }`, i.e. `io.ktor.serialization.kotlinx.json.DefaultJson`
 * (`encodeDefaults`, `isLenient`, `allowSpecialFloatingPointValues`, `allowStructuredMapKeys`; NOT
 * `ignoreUnknownKeys`), so the project's test passes that object itself instead of a copy.
 */
class RoundTripConfig(
    val project: String,
    val specFile: File,
    val modelPackage: String,
    val apiPackage: String,
    val json: Json,
    val baselineFile: File,
    val reportDir: File,
    val updateBaseline: Boolean,
) {
    companion object {
        /** Reads the `roundtrip.*` system properties the shared Gradle script `gradle/example-roundtrip.gradle.kts` sets. */
        fun fromSystemProperties(json: Json): RoundTripConfig {
            fun prop(name: String) = System.getProperty("roundtrip.$name") ?: error("System property roundtrip.$name is not set")
            return RoundTripConfig(
                project = prop("project"),
                specFile = File(prop("spec")),
                modelPackage = prop("modelPackage"),
                apiPackage = prop("apiPackage"),
                json = json,
                baselineFile = File(prop("baseline")),
                reportDir = File(prop("reportDir")),
                updateBaseline = prop("updateBaseline").toBoolean(),
            )
        }
    }
}

class RoundTripResult(
    val config: RoundTripConfig,
    val collected: CollectedExamples,
    val findings: List<Finding>,
)

/**
 * Round-trips every example whose schema is a named component schema through the generated model:
 * decode with the client's `Json`, re-encode, compare as JSON trees. Writes the reports, compares with the baseline
 * and fails on any difference (see [RoundTripConfig.updateBaseline]).
 */
fun runExampleRoundTrip(config: RoundTripConfig) {
    val result = roundTrip(config)
    writeReports(result)
    Baseline.check(result)
}

fun roundTrip(config: RoundTripConfig): RoundTripResult {
    val text = config.specFile.readText()
    val yaml = config.specFile.extension.lowercase().let { it == "yaml" || it == "yml" }
    val rawSpec = loadRawSpec(text, yaml)
    val collected = collectExamples(rawSpec)
    val openApi = if (yaml) OpenAPI.fromYaml(text) else OpenAPI.fromJson(text)
    val loader = Thread.currentThread().contextClassLoader ?: RoundTripper::class.java.classLoader
    val models = GeneratedModels(openApi, config.modelPackage, config.apiPackage, loader)
    val roundTripper = RoundTripper(config.json, models, rawSpec)
    val findings = collected.cases.map(roundTripper::run)
    return RoundTripResult(config, collected, findings)
}

internal object Baseline {
    private val pretty = Json { prettyPrint = true; prettyPrintIndent = "  " }

    /** Baseline entries: everything that is not `ok`, sorted, so a diff of the file is a diff of the findings. */
    fun encode(project: String, findings: List<Finding>): String {
        val element = buildJsonObject {
            put("project", project)
            put("findings", buildJsonArray {
                for (f in findings.filter { it.classification != Classification.Ok }.sortedWith(order)) {
                    add(buildJsonObject {
                        put("id", f.id)
                        put("classification", f.classification.label)
                        put("class", f.className)
                        put("signature", f.signature)
                        put("detail", f.detail)
                    })
                }
            })
        }
        return pretty.encodeToString(JsonElement.serializer(), element) + "\n"
    }

    private val order = compareBy<Finding>({ it.id }, { it.classification.label })

    private fun decode(file: File): Map<String, String> {
        if (!file.exists()) return emptyMap()
        val root = Json.parseToJsonElement(file.readText()) as JsonObject
        return (root["findings"] as JsonArray).associate { entry ->
            val o = entry as JsonObject
            fun s(k: String) = (o[k] as? JsonPrimitive)?.takeIf { it.isString }?.content ?: ""
            key(s("id"), s("classification"), s("class"), s("detail")) to
                "${s("classification")} ${s("id")}: ${s("signature")} -- ${s("detail")}"
        }
    }

    private fun key(id: String, classification: String, className: String, detail: String) =
        "$id|$classification|$className|$detail"

    fun check(result: RoundTripResult) {
        val config = result.config
        val current = result.findings.filter { it.classification != Classification.Ok }.associate {
            key(it.id, it.classification.label, it.className ?: "", it.detail) to
                "${it.classification.label} ${it.id}: ${it.signature} -- ${it.detail}"
        }
        val encoded = encode(config.project, result.findings)
        if (config.updateBaseline) {
            config.baselineFile.parentFile?.mkdirs()
            config.baselineFile.writeText(encoded)
            println("example-roundtrip: wrote ${current.size} finding(s) to ${config.baselineFile.path}")
            return
        }
        val known = decode(config.baselineFile)
        val appeared = current.filterKeys { it !in known }.values.sorted()
        val disappeared = known.filterKeys { it !in current }.values.sorted()
        if (appeared.isEmpty() && disappeared.isEmpty()) return
        fail(buildString {
            appendLine("The example round-trip findings of '${config.project}' differ from ${config.baselineFile.path}.")
            if (!config.baselineFile.exists()) appendLine("The baseline file does not exist.")
            if (appeared.isNotEmpty()) {
                appendLine()
                appendLine("NEW findings (${appeared.size}) - a regression, or a changed finding:")
                appeared.take(MAX_LISTED).forEach { appendLine("  + $it") }
                if (appeared.size > MAX_LISTED) appendLine("  ... and ${appeared.size - MAX_LISTED} more")
            }
            if (disappeared.isNotEmpty()) {
                appendLine()
                appendLine("RESOLVED findings (${disappeared.size}) - fixed (or no longer collected); the baseline is stale:")
                disappeared.take(MAX_LISTED).forEach { appendLine("  - $it") }
                if (disappeared.size > MAX_LISTED) appendLine("  ... and ${disappeared.size - MAX_LISTED} more")
            }
            appendLine()
            appendLine("Report: ${config.reportDir.resolve(config.project + ".md").path}")
            append("To accept the current findings: ./gradlew :${config.project}:test -PupdateRoundTripBaseline=true")
        })
    }

    private const val MAX_LISTED = 40
}
