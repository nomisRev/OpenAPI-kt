package io.github.nomisrev.openapi.parser

import kotlinx.serialization.Serializable

@Serializable
@Suppress("EnumEntryName")
public enum class Style {
  simple,
  form,
  matrix,
  label,
  spaceDelimited,
  pipeDelimited,
  deepObject,
}
