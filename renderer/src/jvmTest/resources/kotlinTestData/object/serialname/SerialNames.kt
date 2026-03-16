package io.github.nomisrev.render.test.object_.serialname

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SerialNames(
  @SerialName("snake_case")
  public val snakeCase: String,
  @SerialName("kebab-case")
  public val kebabCase: Long,
  public val alreadyCamel: Boolean,
)
