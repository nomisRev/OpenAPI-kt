package io.github.nomisrev.render.test.client.operations.body.multipart

import kotlin.ByteArray
import kotlin.String

public interface Files {
  public suspend fun post(`file`: ByteArray, purpose: String)
}
