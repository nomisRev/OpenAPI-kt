package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod

fun Operation.getOrCreateOperationId(path: String, method: HttpMethod): String =
  operationId ?: generateSyntheticOperationId(path, method)

fun generateSyntheticOperationId(path: String, method: HttpMethod): String {
  val params = path.split("/")
    .takeLastWhile { it.startsWith("{") && it.endsWith("}") }
    .map { it.substring(1, it.length - 1) }

  return if (params.isEmpty()) method.value.lowercase()
  else params.joinToString(
    prefix = "${method.value.lowercase()}By",
    separator = "And"
  ) { it.replaceFirstChar { it.uppercase() } }
}
