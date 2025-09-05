package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.util.ImportContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ImportContextRenderTest {
  @Test
  fun render_simple_or_fqn_various_cases() {
    val ctx = ImportContext(currentPackage = "com.example", imported = listOf("com.other.Foo"))

    // kotlin.jvm.* not in imported -> FQN
    assertEquals("kotlin.jvm.JvmInline", ctx.renderSimpleOrFqn("kotlin.jvm.JvmInline"))

    // same-package -> simple name
    assertEquals("Bar", ctx.renderSimpleOrFqn("com.example.Bar"))

    // no-dot name -> as-is
    assertEquals("Baz", ctx.renderSimpleOrFqn("Baz"))

    // other kotlin.* default imports -> simple name
    assertEquals("String", ctx.renderSimpleOrFqn("kotlin.String"))

    // explicitly imported -> simple name
    assertEquals("Foo", ctx.renderSimpleOrFqn("com.other.Foo"))

    // everything else -> FQN
    assertEquals("a.b.C", ctx.renderSimpleOrFqn("a.b.C"))
  }
}
