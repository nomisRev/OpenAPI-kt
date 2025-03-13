package io.github.nomisrev.openapi

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
