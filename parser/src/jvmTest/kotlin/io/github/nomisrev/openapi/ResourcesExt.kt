package io.github.nomisrev.openapi

import java.io.BufferedReader

fun resourceText(path: String): String =
  requireNotNull(
    OpenAPIJsonEnd2EndTest::class
      .java
      .classLoader
      .getResourceAsStream(path)
      ?.bufferedReader()
      ?.use(BufferedReader::readText)
  ) {
    "Resource not found: $path"
  }