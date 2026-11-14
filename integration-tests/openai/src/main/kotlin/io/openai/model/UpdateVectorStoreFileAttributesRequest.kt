package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public data class UpdateVectorStoreFileAttributesRequest(
  public val attributes: VectorStoreFileAttributes?,
)
