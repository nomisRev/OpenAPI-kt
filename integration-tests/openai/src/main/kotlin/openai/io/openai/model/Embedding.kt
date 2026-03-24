package io.openai.model

import kotlin.Float
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an embedding vector returned by embedding endpoint.
 *
 */
@Serializable
public data class Embedding(
  public val index: Long,
  public val embedding: List<Float>,
  public val `object`: Object,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("embedding")
    Embedding("embedding"),
    ;
  }
}
