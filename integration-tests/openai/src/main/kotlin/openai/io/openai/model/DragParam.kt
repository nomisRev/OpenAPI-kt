package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A drag action.
 */
@Serializable
public data class DragParam(
  @Required
  public val type: Type = Type.Drag,
  public val path: List<CoordParam>,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("drag")
    Drag("drag"),
    ;
  }
}
