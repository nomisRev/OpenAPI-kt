package io.github.nomisrev.render.test.client.allof.required

import kotlinx.serialization.Serializable

@Serializable
public data class Pets(
  public val pet: Pet,
  public val withName: PetWithName,
  public val withExtra: PetWithExtra,
  public val required: Required,
)
