package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod

fun String.segments(): List<String> =
  replace(Regex("\\{.*?\\}"), "").split("/").filter { it.isNotEmpty() }

private fun HttpMethod.simpleName(): String =
  when (value.uppercase()) {
    "GET" -> "Get"
    "POST" -> "Post"
    "PUT" -> "Put"
    "PATCH" -> "Patch"
    "DELETE" -> "Delete"
    "HEAD" -> "Head"
    "OPTIONS" -> "Options"
    else -> value.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
  }
