package io.github.nomisrev.codegen.format

internal class IndentWriter(
  private val indent: String = "    ",
  private val lineSeparator: String = "\n",
) {
  private val sb = StringBuilder()
  private var depth = 0
  private var atLineStart = true

  fun pushIndent() {
    depth++
  }

  fun popIndent() {
    if (depth > 0) depth--
  }

  fun append(text: String): IndentWriter {
    var i = 0
    while (i < text.length) {
      val ch = text[i]
      if (atLineStart) {
        repeat(depth) { sb.append(indent) }
        atLineStart = false
      }
      if (ch == '\n') {
        sb.append(lineSeparator)
        atLineStart = true
      } else {
        sb.append(ch)
      }
      i++
    }
    return this
  }

  fun newline(): IndentWriter {
    sb.append(lineSeparator)
    atLineStart = true
    return this
  }

  override fun toString(): String = sb.toString()
}
