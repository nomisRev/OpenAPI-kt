package io.github.nomisrev.openapi.render

@IgnorableReturnValue
context(ctx: Renderer, builder: StringBuilder)
fun indented(block: StringBuilder.() -> Unit) =
    buildString(block).lineSequence().joinTo(builder, "\n") {
        when {
            it.isBlank() -> it
            else -> ctx.indent + it
        }
    }

context(builder: StringBuilder)
operator fun String?.unaryPlus() {
    if (this != null) builder.appendLine(this)
}

context(builder: StringBuilder)
fun newLine() {
    builder.appendLine()
}

context(builder: StringBuilder)
fun append(line: String?) {
    if (line != null) builder.append(line)
}

context(appendable: AA)
fun <A, AA : Appendable> Collection<A>.joinTo(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((A) -> CharSequence)? = null
) {
    if (isEmpty()) return
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
