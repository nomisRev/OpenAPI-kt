package io.github.nomisrev.render.test.client.response.separate.methods.single.status.ref

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Pet(
  public val id: Long,
  public val name: String,
)
