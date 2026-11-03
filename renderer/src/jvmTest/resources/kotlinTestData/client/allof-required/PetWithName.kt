package io.github.nomisrev.render.test.client.allof.required

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class PetWithName(
  public val id: Long,
  public val name: String,
)
