package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.TypeSpec
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Head
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpMethod.Companion.Put

interface APIInterceptor {
  fun OpenAPIContext.intercept(api: API): API = api

  fun OpenAPIContext.intercept(model: Model): Model = model

  fun OpenAPIContext.modifyInterface(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
    typeSpec

  fun OpenAPIContext.modifyImplementation(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
    typeSpec

  companion object {
    val NoOp: APIInterceptor =
      object : APIInterceptor {
        override fun OpenAPIContext.intercept(api: API): API = api

        override fun OpenAPIContext.modifyInterface(
          api: API,
          typeSpec: TypeSpec.Builder
        ): TypeSpec.Builder = typeSpec

        override fun OpenAPIContext.modifyImplementation(
          api: API,
          typeSpec: TypeSpec.Builder
        ): TypeSpec.Builder = typeSpec
      }
  }
}

fun HttpMethod.name(): String =
  when (value) {
    Get.value -> "Get"
    Post.value -> "Post"
    Put.value -> "Put"
    Patch.value -> "Patch"
    Delete.value -> "Delete"
    Head.value -> "Head"
    Options.value -> "Options"
    else -> TODO("Custom HttpMethod not yet supported")
  }
