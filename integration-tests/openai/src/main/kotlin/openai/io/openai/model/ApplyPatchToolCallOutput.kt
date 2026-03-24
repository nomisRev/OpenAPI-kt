package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The output emitted by an apply patch tool call.
 */
@Serializable
public data class ApplyPatchToolCallOutput(
  @Required
  public val type: Type = Type.ApplyPatchCallOutput,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val status: ApplyPatchCallOutputStatus,
  public val output: String? = null,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("apply_patch_call_output")
    ApplyPatchCallOutput("apply_patch_call_output"),
    ;
  }
}
