package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class UpdateVectorStoreFileAttributesRequest(
  public val attributes: VectorStoreFileAttributes,
)
