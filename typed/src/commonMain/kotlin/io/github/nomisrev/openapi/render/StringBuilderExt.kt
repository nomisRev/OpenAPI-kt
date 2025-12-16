package io.github.nomisrev.openapi.render

context(builder: StringBuilder)
operator fun String?.unaryPlus() {
    if (this != null) builder.appendLine(this)
}

context(builder: StringBuilder)
fun append(line: String?) {
    if (line != null) builder.append(line)
}

context(builder: StringBuilder)
fun <A> Iterable<A>.joinTo(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((A) -> CharSequence)? = null
) {
    joinTo(builder, separator, prefix, postfix, limit, truncated, transform)
}

fun String.stringValue(): String {
    var max = 0
    var count = 0
    for (c in this)
        if (c != '$' || count++ <= max) Unit
        else {
            max = count
        }
    val dollars = "$".repeat(count + 1)
    return "$dollars\"$this\""
}
