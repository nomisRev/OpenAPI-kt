package io.github.nomisrev.render.test.client.operations.body.json

public interface Pets {
  public suspend fun post(body: CreatePetRequest)
}
