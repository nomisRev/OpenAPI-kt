package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import java.util.concurrent.atomic.AtomicReference

interface OpenAPIContext : Naming, APIInterceptor {
  val `package`: String

  fun addAdditionalFileSpec(fileSpec: FileSpec)

  fun additionalFiles(): List<FileSpec>
}

fun OpenAPIContext(
  `package`: String,
  interceptor: APIInterceptor = APIInterceptor.openAIStreaming(`package`)
): OpenAPIContext =
  object : OpenAPIContext, Naming by Naming(`package`), APIInterceptor by interceptor {
    override val `package`: String = `package`
    private val files = AtomicReference<List<FileSpec>>(emptyList())

    override fun additionalFiles(): List<FileSpec> = files.get()

    override fun addAdditionalFileSpec(fileSpec: FileSpec) {
      files.updateAndGet { it + fileSpec }
    }
  }
