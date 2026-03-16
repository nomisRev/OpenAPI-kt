package io.github.nomisrev.render.test.client.operations.body.json

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class CreatePetRequest(
  public val name: String,
)
