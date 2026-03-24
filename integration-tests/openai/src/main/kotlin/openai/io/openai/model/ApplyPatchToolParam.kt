package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Allows the assistant to create, delete, or update files using unified diffs.
 */
@JvmInline
@Serializable
public value class ApplyPatchToolParam(
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
