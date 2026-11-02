package io.github.nomisrev.render.test.object_.type.array.nullable.required

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class RequiredNullableMessage(
  public val message: String?,
)
