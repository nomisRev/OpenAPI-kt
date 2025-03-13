package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import java.util.concurrent.atomic.AtomicReference

interface OpenAPIContext : Naming, APIInterceptor {
  val `package`: String
  val isK2: Boolean

  fun addAdditionalFileSpec(fileSpec: FileSpec)

  fun additionalFiles(): List<FileSpec>
}

fun OpenAPIContext(
  config: GenerationConfig,
  interceptor: APIInterceptor = APIInterceptor.openAIStreaming(config.`package`)
): OpenAPIContext =
  object : OpenAPIContext, Naming by Naming(config.`package`), APIInterceptor by interceptor {
    override val `package`: String = config.`package`
    override val isK2: Boolean = config.isK2
    private val files = AtomicReference<List<FileSpec>>(emptyList())

    override fun additionalFiles(): List<FileSpec> = files.get()

    override fun addAdditionalFileSpec(fileSpec: FileSpec) {
      files.updateAndGet { it + fileSpec }
    }
  }

fun <A> OpenAPIContext(
  config: GenerationConfig,
  interceptor: APIInterceptor = APIInterceptor.openAIStreaming(config.`package`),
  block: OpenAPIContext.() -> A
): A = block(OpenAPIContext(config, interceptor))
