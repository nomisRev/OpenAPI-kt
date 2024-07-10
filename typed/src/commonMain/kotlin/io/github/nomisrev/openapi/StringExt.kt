package io.github.nomisrev.openapi

fun String.segments(): List<String> =
  replace(Regex("\\{.*?\\}"), "").split("/").filter { it.isNotEmpty() }
