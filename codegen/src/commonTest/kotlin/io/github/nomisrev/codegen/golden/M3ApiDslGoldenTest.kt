package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.api.generic
import io.github.nomisrev.codegen.api.ktFile
import io.github.nomisrev.codegen.api.simple
import io.github.nomisrev.codegen.emit.emitFile
import kotlin.test.Test
import kotlin.test.assertEquals

class M3ApiDslGoldenTest {
  @Test
  fun pets_api_interface_with_suspend_functions() {
    val file = ktFile(name = "PetsApi.kt", pkg = "com.example.api") {
      apiInterface("PetsApi") {
        suspendFun("listPets") {
          param("limit", simple("kotlin.Int", nullable = true), default = "null")
          returns(generic("kotlin.collections.List", simple("com.example.models.Pet")))
        }
        suspendFun("getPet") {
          param("id", simple("kotlin.String"))
          returns(simple("com.example.models.Pet"))
        }
      }
    }

    val actual = emitFile(file)

    val expected = (
      """
      package com.example.api

      import com.example.models.Pet

      interface PetsApi {
          suspend fun listPets(limit: Int? = null): List<Pet>
          suspend fun getPet(id: String): Pet
      }
      """.trimIndent() + "\n"
    )

    assertEquals(expected, actual)
  }
}
