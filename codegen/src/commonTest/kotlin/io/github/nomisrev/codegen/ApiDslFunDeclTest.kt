package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.api.KFile
import io.github.nomisrev.codegen.api.simple
import io.github.nomisrev.codegen.emit.emitFile
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiDslFunDeclTest {
  @Test
  fun interface_with_non_suspend_function() {
    val file =
      KFile(name = "Service.kt", pkg = "com.example.api") {
        apiInterface("Service") { funDecl("ping") { returns(simple("kotlin.Unit")) } }
      }

    val expected =
      """
      package com.example.api

      interface Service {
          fun ping(): Unit
      }
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
