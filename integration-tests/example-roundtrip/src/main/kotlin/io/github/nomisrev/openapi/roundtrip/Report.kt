package io.github.nomisrev.openapi.roundtrip

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

private const val IDS_PER_GROUP = 8

internal fun writeReports(result: RoundTripResult) {
    val config = result.config
    config.reportDir.mkdirs()
    config.reportDir.resolve("${config.project}.md").writeText(markdown(result))
    config.reportDir.resolve("${config.project}.json").writeText(json(result))
}

private fun totals(findings: List<Finding>): Map<Classification, Int> =
    Classification.entries.associateWith { c -> findings.count { it.classification == c } }

private fun markdown(result: RoundTripResult): String = buildString {
    val findings = result.findings
    appendLine("# Example round-trip: ${result.config.project}")
    appendLine()
    appendLine("Spec: `${result.config.specFile.name}`; models in `${result.config.modelPackage}`.")
    appendLine()
    appendLine("Examples round-tripped: **${findings.size}**. Examples skipped because they are not an instance of a named component schema:")
    if (result.collected.skipped.isEmpty()) appendLine("none") else {
        appendLine()
        result.collected.skipped.forEach { (reason, n) -> appendLine("- $reason: $n") }
    }
    appendLine()
    appendLine("| classification | examples |")
    appendLine("| --- | ---: |")
    totals(findings).forEach { (c, n) -> appendLine("| ${c.label} | $n |") }

    for (classification in Classification.entries.filter { it != Classification.Ok }) {
        val group = findings.filter { it.classification == classification }
        if (group.isEmpty()) continue
        appendLine()
        appendLine("## ${classification.label} (${group.size})")
        val bySignature = group.groupBy(Finding::signature).entries.sortedWith(compareBy({ -it.value.size }, { it.key }))
        for ((signature, members) in bySignature) {
            appendLine()
            appendLine("### `${signature.replace("`", "'")}` (${members.size})")
            appendLine()
            val sorted = members.sortedBy(Finding::id)
            sorted.take(IDS_PER_GROUP).forEach { f ->
                appendLine("- `${f.id}`${f.className?.let { " -> `$it`" } ?: ""}")
                if (f.detail.isNotEmpty()) appendLine("  - ${f.detail.replace("\n", " ")}")
            }
            if (sorted.size > IDS_PER_GROUP) appendLine("- ... and ${sorted.size - IDS_PER_GROUP} more")
        }
    }
}

private fun json(result: RoundTripResult): String {
    val element = buildJsonObject {
        put("project", result.config.project)
        put("examples", result.findings.size)
        put("totals", buildJsonObject { totals(result.findings).forEach { (c, n) -> put(c.label, n) } })
        put("skipped", buildJsonObject { result.collected.skipped.forEach { (reason, n) -> put(reason, n) } })
        put("findings", buildJsonArray {
            for (f in result.findings.sortedBy(Finding::id)) add(buildJsonObject {
                put("id", f.id)
                put("classification", f.classification.label)
                put("class", f.className)
                put("signature", f.signature)
                put("detail", f.detail)
            })
        })
    }
    return Json { prettyPrint = true }.encodeToString(JsonElement.serializer(), element) + "\n"
}
