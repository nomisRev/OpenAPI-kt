package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An internal identifier for an item to reference.
 */
@Serializable
public data class ItemReferenceParam(
  public val type: Type? = null,
  public val id: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("item_reference")
    ItemReference("item_reference"),
    ;
  }
}
