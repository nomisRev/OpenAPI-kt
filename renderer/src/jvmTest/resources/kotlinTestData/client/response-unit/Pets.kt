package io.github.nomisrev.render.test.client.response.unit

import kotlin.String

public interface Pets {
  public fun petId(petId: String): PetId

  public interface PetId {
    public suspend fun delete()
  }
}
