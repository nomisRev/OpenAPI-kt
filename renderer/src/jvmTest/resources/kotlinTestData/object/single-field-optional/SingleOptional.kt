package io.github.nomisrev.render.test.object_.single.`field`.optional

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class SingleOptional(
  public val message: String? = null,
)
