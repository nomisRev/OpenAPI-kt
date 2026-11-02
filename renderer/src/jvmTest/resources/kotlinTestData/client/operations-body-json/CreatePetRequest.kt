package io.github.nomisrev.render.test.client.operations.body.json

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class CreatePetRequest(
  public val name: String,
)
