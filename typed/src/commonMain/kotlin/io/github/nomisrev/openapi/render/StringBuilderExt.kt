package io.github.nomisrev.openapi.render

context(builder: StringBuilder)
operator fun String?.unaryPlus() {
    if (this != null) builder.appendLine(this)
}

context(builder: StringBuilder)
fun append(line: String?) {
    if (line != null) builder.append(line)
}

context(appendable: AA)
fun <A, AA: Appendable> Iterable<A>.joinTo(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((A) -> CharSequence)? = null
) {
    joinTo(appendable, separator, prefix, postfix, limit, truncated, transform)
}

fun <A, B> Map<A, B>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: (Map.Entry<A, B>) -> String
): String = entries.joinToString(separator, prefix, postfix, limit, truncated, transform)

context(appendable: AA)
fun <A, B, AA : Appendable> Map<A, B>.joinTo(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: (Map.Entry<A, B>) -> String
) {
    entries.joinTo(appendable, separator, prefix, postfix, limit, truncated, transform)
}

fun String.stringValue(): String {
    var max = 0
    var count = 0
    for (c in this)
        if (c != '$' || count++ <= max) Unit
        else {
            max = count
        }
    val dollars = if(count > 0) "$".repeat(count + 1) else ""
    return "$dollars\"$this\""
}

