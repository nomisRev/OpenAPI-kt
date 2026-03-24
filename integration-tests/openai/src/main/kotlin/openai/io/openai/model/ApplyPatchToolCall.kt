package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A tool call that applies file diffs by creating, deleting, or updating files.
 */
@Serializable
public data class ApplyPatchToolCall(
  @Required
  public val type: Type = Type.ApplyPatchCall,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val status: ApplyPatchCallStatus,
  public val operation: Operation,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  /**
   * One of the create_file, delete_file, or update_file operations applied via apply_patch.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Operation {
    @Serializable
    @JvmInline
    @SerialName("ApplyPatchCreateFileOperation")
    public value class ApplyPatchCreateFileOperation(
      public val `value`: io.openai.model.ApplyPatchCreateFileOperation,
    ) : Operation

    @Serializable
    @JvmInline
    @SerialName("ApplyPatchDeleteFileOperation")
    public value class ApplyPatchDeleteFileOperation(
      public val `value`: io.openai.model.ApplyPatchDeleteFileOperation,
    ) : Operation

    @Serializable
    @JvmInline
    @SerialName("ApplyPatchUpdateFileOperation")
    public value class ApplyPatchUpdateFileOperation(
      public val `value`: io.openai.model.ApplyPatchUpdateFileOperation,
    ) : Operation
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("apply_patch_call")
    ApplyPatchCall("apply_patch_call"),
    ;
  }
}
