package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Forces the model to call the apply_patch tool when executing a tool call.
 */
@Serializable
public data class SpecificApplyPatchParam(
  @Required
  public val type: Type = Type.ApplyPatch,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("apply_patch")
    ApplyPatch("apply_patch"),
    ;
  }
}
