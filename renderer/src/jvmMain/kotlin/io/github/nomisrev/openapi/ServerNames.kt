package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Server

/**
 * The type of a server in the generated `<Api>Server` interface: [outer] directly in it, and [inner] nested in [outer]
 * when servers told apart only by their scheme share it (`ApiServer.ApiExample.Https`).
 */
internal data class ServerName(val outer: String, val inner: String? = null) {
    val path: List<String> get() = listOfNotNull(outer, inner)
}

/** The [names] of a server list, in its order, and a warning for each name that is a last resort. */
internal data class ServerNames(val names: List<ServerName>, val warnings: List<String>)

private val NonAlphanumeric = Regex("[^\\p{L}\\p{N}]+")
private val ServerSuffix = Regex("\\s+[Ss]erver\\s*$")
private val VariableText = Regex("\\{[^}]*\\}")

/** `https:` of `https://host`, or `{scheme}:` of `{scheme}://host`; a URL without scheme, and `//host`, have none. */
private val SchemePrefix = Regex("^([A-Za-z][A-Za-z0-9+.\\-]*|\\{[^}]*\\}):(?=//)")

private const val CustomCase = "Custom"

/** The servers of one clash group whose URLs differ at most in the scheme. */
private class ServerUnit(val members: List<Int>) {
    val first: Int get() = members.first()
    var outer: String = ""
    var nested: Boolean = false
}

/**
 * The name of each server's type. Servers whose names would be one are told apart by what differs in their URL:
 *
 * 1. Servers whose URLs differ otherwise append the URL words (host labels, path segments, variable names) the other
 *    servers of the clashing group do not have: `ApiLinodeV4` and `ApiLinodeV4beta`. A server without such a word
 *    keeps the plain name.
 * 2. Servers whose URLs differ only in the scheme share one name and are nested by scheme: `ApiServer.ApiExample.Http`
 *    and `ApiServer.ApiExample.Https`. Without a literal scheme the nested name is the scheme variable's (`{scheme}`
 *    is `Scheme`) or `Relative` (`//host`, `/v1`). This is done after 1, whatever the order of the servers.
 * 3. What still collides (the same URL twice, URLs with the same words) is suffixed with the first number from 2 that
 *    is free, the server declared first keeping the name, and a warning says so.
 *
 * A name is an identifier without backticks ([baseServerName]). `Custom` is the built-in case, so a server that would
 * be named `Custom` is `CustomServer`. A name that does not collide keeps its name.
 */
internal fun List<Server>.serverNames(): ServerNames {
    val bases = map { it.baseServerName() }
    val outers = arrayOfNulls<String>(size)
    val inners = arrayOfNulls<String>(size)
    val warnings = mutableListOf<String>()

    val units = mutableListOf<ServerUnit>()
    for (group in indices.groupBy { bases[it].lowercase() }.values) {
        val groupUnits = group.groupBy { this[it].schemeless() }.values.map { ServerUnit(it) }
        val words = groupUnits.map { unit -> this[unit.first].schemelessWords() }
        for ([index, unit] in groupUnits.withIndex()) {
            val others = words.filterIndexed { other, _ -> other != index }.flatten().mapTo(mutableSetOf()) { it.lowercase() }
            val extras = if (groupUnits.size == 1) emptyList() else words[index].filter { it.lowercase() !in others }
            unit.outer = bases[unit.first] + extras.joinToString("") { it.sanitizedPascalCase() }
            unit.nested = unit.members.map { this[it].schemeLabel().lowercase() }.distinct().size > 1
        }
        units += groupUnits
    }

    // A unit that is not nested but has several servers holds the same URL twice: each is a name of its own.
    val entries = units.flatMap { unit ->
        if (unit.nested || unit.members.size == 1) listOf(unit.first to unit) else unit.members.map { it to unit }
    }.sortedBy { it.first }

    val taken = mutableSetOf<String>()
    for ([first, unit] in entries) {
        val wanted = unit.outer
        val isCustom = wanted.equals(CustomCase, ignoreCase = true)
        var name = if (isCustom) wanted + "Server" else wanted
        if (name.lowercase() in taken) {
            name += generateSequence(2) { it + 1 }.first { "$name$it".lowercase() !in taken }
        }
        taken += name.lowercase()
        if (name != wanted) {
            val reason = if (isCustom && name == wanted + "Server") {
                "'Custom' is the case for any other URL"
            } else {
                "an earlier server has the same Kotlin name"
            }
            warnings += "Server '${this[first].describe()}' is generated as '$name': $reason."
        }
        if (unit.nested) for (index in unit.members) outers[index] = name else outers[first] = name
    }
    for (unit in units.filter { it.nested }) {
        val seen = mutableSetOf(outers[unit.first]!!.lowercase())
        for (index in unit.members) {
            val label = this[index].schemeLabel()
            var inner = label
            if (inner.lowercase() in seen) {
                inner += generateSequence(2) { it + 1 }.first { "$label$it".lowercase() !in seen }
                warnings += "Server '${this[index].describe()}' is generated as '${outers[index]}.$inner': " +
                    "an earlier server has the same URL."
            }
            seen += inner.lowercase()
            inners[index] = inner
        }
    }
    return ServerNames(indices.map { ServerName(outers[it]!!, inners[it]) }, warnings)
}

private fun Server.describe(): String = if (description.isNullOrBlank()) url else "$url ($description)"

/**
 * The name of this server alone: its description without a trailing "server", else the first two words of its URL.
 * Characters other than letters and digits are word breaks, so it is an identifier, led by `Server` when it starts with
 * a digit, and `Default` when there is nothing to name it after.
 */
internal fun Server.baseServerName(): String {
    val fromDescription = description.orEmpty().replace(ServerSuffix, "").sanitizedPascalCase()
    val name = fromDescription.ifEmpty {
        url.replace(SchemePrefix, "").removePrefix("//")
            .split("/", ".", "-")
            .map { it.replace(VariableText, "") }
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.sanitizedPascalCase() }
    }.ifEmpty { "Default" }
    return if (name.first().isDigit()) "Server$name" else name
}

private fun String.sanitizedPascalCase(): String = replace(NonAlphanumeric, " ").toPascalCase()

/** The URL without its scheme: the key under which servers are told apart by their scheme alone. */
private fun Server.schemeless(): String = url.replace(SchemePrefix, "")

/** The distinct words of the URL without its scheme: host labels, path segments, port and variable names. */
private fun Server.schemelessWords(): List<String> =
    schemeless().split(NonAlphanumeric).filter { it.isNotEmpty() }.distinctBy { it.lowercase() }

/** `Https` for `https://`, `Scheme` for `{scheme}://`, `Relative` for a URL without a scheme. */
private fun Server.schemeLabel(): String {
    val scheme = SchemePrefix.find(url)?.groupValues?.get(1) ?: return "Relative"
    val text = if (scheme.startsWith("{")) scheme.removeSurrounding("{", "}") else scheme.lowercase()
    val label = text.sanitizedPascalCase().ifEmpty { "Relative" }
    return if (label.first().isDigit()) "Scheme$label" else label
}
