package exploration

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.*
import io.ktor.utils.io.readAvailable
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.*

internal val corpusJson = Json { prettyPrint = true }
internal fun digest(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
    .digest(bytes).joinToString("") { "%02x".format(it) }
internal fun JsonObject.string(key: String): String = getValue(key).jsonPrimitive.content
private fun JsonObject.textOrNull(key: String): String? = get(key)?.jsonPrimitive?.content
private fun entryHash(entry: JsonObject) = digest("${entry.string("api")}\u0000${entry.string("version")}".toByteArray())
private fun writeJson(file: File, value: JsonElement) = file.writeText(corpusJson.encodeToString(JsonElement.serializer(), value))

internal suspend fun download(client: HttpClient, url: String, path: File, maxBytes: Int = 64 * 1024 * 1024): ByteArray {
    require(url.startsWith("https://api.apis.guru/")) { "Unexpected corpus URL: $url" }
    val requestUrl = URLBuilder(url).apply {
        encodedPath = encodedPath.encodeURLPath(encodeEncoded = false)
    }.build()
    if (path.exists()) return path.readBytes()
    repeat(3) { attempt ->
        try {
            val data = client.prepareGet(requestUrl) { header("User-Agent", "OpenAPI-kt-corpus/1.0") }.execute { response ->
                check(response.status.value in 200..299) { "HTTP ${response.status.value}: $url" }
                val channel = response.bodyAsChannel()
                val buffer = ByteArray(8192)
                val output = ByteArrayOutputStream()
                while (true) {
                    val count = channel.readAvailable(buffer)
                    if (count == -1) break
                    require(output.size().toLong() + count <= maxBytes) { "Document exceeds ${maxBytes} bytes" }
                    output.write(buffer, 0, count)
                }
                output.toByteArray()
            }
            // Both the catalog and OpenAPI documents must be JSON objects.
            Json.decodeFromString(JsonObject.serializer(), data.decodeToString())
            path.parentFile.mkdirs()
            val temporary = File(path.parentFile, "${path.nameWithoutExtension}.tmp")
            try {
                temporary.writeBytes(data)
                java.nio.file.Files.move(temporary.toPath(), path.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING)
            } finally {
                temporary.delete()
            }
            return data
        } catch (failure: Exception) {
            if (failure is CancellationException || attempt == 2) throw failure
            delay((attempt + 1) * 1000L)
        }
    }
    error("Unreachable")
}

internal data class CorpusOptions(
    val allVersions: Boolean = false,
    val limit: Int = 0,
    val filter: String = "",
    val jobs: Int = 2,
    val timeout: Long = 120,
    val skipCompile: Boolean = false,
    val fetchOnly: Boolean = false,
    val output: File = File("exploration/build/corpus"),
)

internal fun parseOptions(args: Array<String>): CorpusOptions {
    var options = CorpusOptions()
    var index = 0
    fun value(flag: String): String = args.getOrNull(index++) ?: error("Missing value for $flag")
    while (index < args.size) {
        when (val flag = args[index++]) {
            "--all-versions" -> options = options.copy(allVersions = true)
            "--limit" -> options = options.copy(limit = value(flag).toInt())
            "--filter" -> options = options.copy(filter = value(flag))
            "--jobs" -> options = options.copy(jobs = value(flag).toInt())
            "--timeout" -> options = options.copy(timeout = value(flag).toLong())
            "--skip-compile" -> options = options.copy(skipCompile = true)
            "--fetch-only" -> options = options.copy(fetchOnly = true)
            "--output" -> options = options.copy(output = File(value(flag)))
            else -> error("Unknown argument: $flag")
        }
    }
    require(options.jobs > 0 && options.timeout > 0 && options.limit >= 0) {
        "jobs/timeout must be positive and limit nonnegative"
    }
    return options
}

internal fun selectEntries(index: JsonObject, options: CorpusOptions): List<JsonObject> = index.flatMap { (api, metadata) ->
    if (options.filter !in api) emptyList() else {
        val info = metadata.jsonObject
        val versions = info.getValue("versions").jsonObject
        val selected = if (options.allVersions) versions.keys else listOf(info.string("preferred"))
        selected.map { version ->
            val spec = versions.getValue(version).jsonObject
            buildJsonObject {
                put("api", api); put("version", version)
                put("dialect", spec.string("openapiVer")); put("url", spec.string("swaggerUrl"))
            }
        }
    }
}.sortedBy(::entryHash).let { if (options.limit == 0) it else it.take(options.limit) }

internal fun runProcess(command: List<String>, directory: File, label: String, timeout: Long): String {
    val process = ProcessBuilder(command).redirectErrorStream(true)
        .redirectOutput(directory.resolve("$label.log")).start()
    try {
        if (!process.waitFor(timeout, TimeUnit.SECONDS)) return "timeout"
        return if (process.exitValue() == 0) "pass" else "fail"
    } finally {
        if (process.isAlive) {
            process.destroyForcibly()
            process.waitFor()
        }
    }
}

private data class Runner(val runtime: String, val compiler: String, val plugin: String)
private fun readArray(file: File): JsonArray = if (file.exists()) Json.parseToJsonElement(file.readText()).jsonArray else JsonArray(emptyList())

private suspend fun checkEntry(client: HttpClient, entry: JsonObject, options: CorpusOptions, out: File, runner: Runner?): JsonObject {
    val key = entryHash(entry).take(20)
    val result = entry.toMutableMap()
    fun put(name: String, value: String) { result[name] = JsonPrimitive(value) }
    put("key", key)
    val document = out.resolve("specs/$key.json")
    val data = try {
        download(client, entry.string("url"), document).also {
            put("sha256", digest(it)); result["bytes"] = JsonPrimitive(it.size); put("fetch", "pass")
        }
    } catch (failure: Exception) {
        if (failure is CancellationException) throw failure
        put("fetch", "fail"); put("message", failure.message.orEmpty())
        return JsonObject(result)
    }
    if (options.fetchOnly) return JsonObject(result)
    val actual = Json.parseToJsonElement(data.decodeToString()).jsonObject
    val dialect = actual["openapi"] ?: actual["swagger"] ?: JsonPrimitive("unknown")
    result["dialect"] = dialect
    if (!dialect.jsonPrimitive.content.startsWith("3.")) {
        put("status", "unsupported-dialect")
        return JsonObject(result)
    }
    val directory = out.resolve("results/$key")
    check(!directory.exists() || directory.deleteRecursively()) { "Cannot clear $directory" }
    check(directory.mkdirs()) { "Cannot create $directory" }
    val runtime = requireNotNull(runner).runtime
    put("worker", runProcess(listOf("java", "-Xmx1g", "-cp", runtime, "exploration.CorpusWorkerKt",
        document.path, directory.path), directory, "worker", options.timeout))
    result["phases"] = readArray(directory.resolve("phases.json"))
    put("last_phase", directory.resolve("active-phase.txt").takeIf { it.exists() }?.readText() ?: "startup")
    result["diagnostics"] = readArray(directory.resolve("diagnostics.json"))
    put("compile", "not-run")
    val sources = directory.resolve("sources").walkTopDown().filter { it.isFile && it.extension == "kt" }.sortedBy { it.path }.toList()
    result["files"] = JsonPrimitive(sources.size)
    if (result["worker"] == JsonPrimitive("pass") && !options.skipCompile && sources.isNotEmpty()) {
        val arguments = listOf("-no-stdlib", "-no-reflect", "-jvm-target", "11", "-classpath", runtime,
            "-Xplugin=${runner.plugin}", "-d", directory.resolve("classes").path) + sources.map { it.path }
        val argfile = directory.resolve("compiler.args")
        argfile.writeText(arguments.joinToString("\n") { "\"${it.replace("\\", "\\\\").replace("\"", "\\\"")}\"" })
        put("compile", runProcess(listOf("java", "-Xmx1g", "-cp", runner.compiler,
            "org.jetbrains.kotlin.cli.jvm.K2JVMCompiler", "@${argfile.path}"), directory, "compile", options.timeout))
    }
    return JsonObject(result)
}

internal fun summarize(out: File): JsonObject {
    val results = out.resolve("results.jsonl").readLines().map { Json.parseToJsonElement(it).jsonObject }
    val totals = linkedMapOf<String, Int>()
    val failures = linkedMapOf<String, Int>()
    val examples = mutableMapOf<String, String>()
    val compilerFailures = linkedMapOf<String, Int>()
    val compilerExamples = mutableMapOf<String, String>()
    fun MutableMap<String, Int>.add(key: String, count: Int = 1) { this[key] = getOrDefault(key, 0) + count }
    for (result in results) {
        when {
            "infrastructure_error" in result -> totals.add("infrastructure-error")
            result.textOrNull("fetch") != "pass" -> totals.add("fetch-fail")
            "worker" !in result -> totals.add(result.textOrNull("status") ?: "fetch-only")
            else -> {
                totals.add("worker-${result.string("worker")}")
                totals.add("compile-${result.string("compile")}")
                val example = "${result.string("api")} [${result.string("key")}]"
                if (result.string("compile") == "fail") {
                    val messages = out.resolve("results/${result.string("key")}/compile.log").readLines()
                        .filter { "error:" in it }.map { it.substringAfter("error:").trim() }.toSet()
                        .ifEmpty { setOf("Compiler failed without an error diagnostic; inspect compile.log") }
                    for (message in messages) {
                        compilerFailures.add(message); compilerExamples.putIfAbsent(message, example)
                    }
                }
                for (element in result.getValue("phases").jsonArray) {
                    val phase = element.jsonObject
                    totals.add("${phase.string("phase")}-${phase.string("status")}")
                    if (phase.string("status") == "fail") {
                        val signature = "${phase.string("phase")}: ${phase.string("exception")}: ${phase.string("message").substringBefore('\n').take(240)}"
                        failures.add(signature); examples.putIfAbsent(signature, example)
                    }
                }
                val diagnostics = result.getValue("diagnostics").jsonArray
                totals.add("generation-errors", diagnostics.count { it.jsonObject.string("severity") == "Error" })
                totals.add("generation-warnings", diagnostics.count { it.jsonObject.string("severity") == "Warning" })
            }
        }
    }
    return buildJsonObject {
        put("documents", results.size)
        put("counts", JsonObject(totals.mapValues { JsonPrimitive(it.value) }))
        put("failure_groups", JsonArray(failures.entries.sortedByDescending { it.value }.map { (signature, count) ->
            buildJsonObject { put("signature", signature); put("count", count); put("example", examples.getValue(signature)) }
        }))
        put("compiler_failure_groups", JsonArray(compilerFailures.entries.sortedByDescending { it.value }.map { (message, count) ->
            buildJsonObject { put("message", message); put("documents", count); put("example", compilerExamples.getValue(message)) }
        }))
    }.also { writeJson(out.resolve("summary.json"), it) }
}

fun main(args: Array<String>) = runBlocking {
    if (args.any { it == "--help" || it == "-h" }) {
        println("""
            Fetch a pinned API Guru snapshot and exercise each document in isolated JVMs.
            --all-versions     Select every historical version
            --limit N          Maximum documents; 0 means all (default)
            --filter TEXT      Substring of API ID
            --jobs N           Concurrent documents (default: 2)
            --timeout N        Seconds per worker/compiler process (default: 120)
            --skip-compile     Only generate sources
            --fetch-only       Only download documents
            --output PATH      Artifact directory (default: exploration/build/corpus)
        """.trimIndent())
        return@runBlocking
    }
    val options = parseOptions(args)
    val out = options.output.canonicalFile.apply { mkdirs() }
    HttpClient(CIO) {
        expectSuccess = true
        install(HttpTimeout) { requestTimeoutMillis = 60_000; connectTimeoutMillis = 60_000; socketTimeoutMillis = 60_000 }
    }.use { client ->
        val indexBytes = download(client, "https://api.apis.guru/v2/list.json", out.resolve("index.json"))
        val entries = selectEntries(Json.parseToJsonElement(indexBytes.decodeToString()).jsonObject, options)
        fun git(vararg arguments: String): String {
            val process = ProcessBuilder(listOf("git") + arguments).redirectError(ProcessBuilder.Redirect.INHERIT).start()
            val output = process.inputStream.bufferedReader().readText()
            check(process.waitFor() == 0) { "git ${arguments.joinToString(" ")} failed" }
            return output.trim()
        }
        writeJson(out.resolve("manifest.json"), buildJsonObject {
            put("index_sha256", digest(indexBytes)); put("selection", JsonArray(entries))
            put("command", JsonArray((listOf("exploration.CorpusKt") + args).map(::JsonPrimitive)))
            put("revision", git("rev-parse", "HEAD")); put("dirty", git("status", "--porcelain").isNotEmpty())
        })
        val runner = if (options.fetchOnly) null else File("exploration/build/runner").let {
            Runner(it.resolve("runtime.txt").readText(), it.resolve("compiler.txt").readText(), it.resolve("serialization.txt").readText())
        }
        // A fixed number of consumers bounds both downloads and isolated JVMs.
        coroutineScope {
            val queue = Channel<JsonObject>()
            val completed = Channel<JsonObject>()
            launch { try { entries.forEach { queue.send(it) } } finally { queue.close() } }
            val workers = List(options.jobs) {
                launch(Dispatchers.IO) {
                    for (entry in queue) {
                        val result = try { checkEntry(client, entry, options, out, runner) } catch (failure: Exception) {
                            if (failure is CancellationException) throw failure
                            JsonObject(entry + ("infrastructure_error" to JsonPrimitive(failure.message.orEmpty())))
                        }
                        completed.send(result)
                    }
                }
            }
            launch { workers.joinAll(); completed.close() }
            out.resolve("results.jsonl").bufferedWriter().use { report ->
                var count = 0
                for (result in completed) {
                    report.appendLine(result.toString()); report.flush()
                    val status = result.textOrNull("status") ?: result.textOrNull("worker") ?: result.textOrNull("fetch") ?: "infrastructure-error"
                    println("[${++count}/${entries.size}] ${result.string("api")} $status compile=${result.textOrNull("compile") ?: "-"}")
                }
            }
        }
        println(corpusJson.encodeToString(JsonObject.serializer(), summarize(out)))
    }
}
