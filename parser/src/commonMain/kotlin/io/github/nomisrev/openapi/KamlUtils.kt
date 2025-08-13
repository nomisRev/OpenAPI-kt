package io.github.nomisrev.openapi

import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode

internal fun YamlMap.getOrNull(key: String): YamlNode? =
  entries.firstNotNullOfOrNull { (scalar, node) -> if (scalar.content == key) node else null }
