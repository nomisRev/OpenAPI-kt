package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.format.IndentWriter
import kotlin.test.Test
import kotlin.test.assertEquals

class IndentWriterTest {
  @Test
  fun popIndent_is_noop_at_zero_and_decrements_when_positive() {
    val w = IndentWriter()
    // depth == 0 pop should be no-op
    w.popIndent()
    w.append("x").newline()

    // depth > 0 branch
    w.pushIndent()
    w.append("y").newline()
    w.popIndent()
    w.append("z")

    val expected = buildString {
      append("x\n")
      append("    y\n")
      append("z")
    }

    assertEquals(expected, w.toString())
  }

  @Test
  fun append_handles_newlines_and_atLineStart_indentation() {
    val w = IndentWriter()
    w.pushIndent()
    w.append("a\nb") // newline should reset atLineStart and indent next line

    assertEquals("    a\n    b", w.toString())
  }
}
