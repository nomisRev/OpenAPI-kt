package io.github.nomisrev.openapi

fun String.pathSegments(): List<String> =
  replace(Regex("\\{.*?\\}"), "")
    .split("/")
    .filter { it.isNotEmpty() }
